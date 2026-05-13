package ca.glotov.vehicledashboard.state;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class VehicleStateSimulator {

    private static final int RPM_PER_SETTING = 200;
    private static final int MAX_RPM = 800;
    private static final int RPM_EASE_PER_TICK = 100;
    private static final int TEMP_EASE_PER_TICK = 1;
    private static final int TEMP_BASE = 25;
    private static final int CHARGE_RATE_PCT_PER_TICK = 5;
    private static final int CHARGING_POWER_DRAW_KW = 300;
    private static final int MOTOR_STATUS_RPM_THRESHOLD = 600;
    private static final int BATTERY_LOW_THRESHOLD_PCT = 20;

    private static final int PARKING_BRAKE_FLIP_CHANCE_PERCENT = 1;
    private static final int CHECK_ENGINE_FLIP_CHANCE_PERCENT = 1;
    private static final int GEAR_RATIO_CHANGE_CHANCE_PERCENT = 3;
    private static final String[] GEAR_RATIOS = {"N", "1:1", "1:2", "1:4"};

    private final VehicleStateRepository repository;
    private final Random random = new Random();

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void tick() {
        VehicleState s = repository.findById(VehicleState.SINGLETON_ID).orElse(null);
        if (s == null) return;

        int targetRpm = Math.min(s.getMotorSpeedSetting() * RPM_PER_SETTING, MAX_RPM);
        s.setMotorRpm(easeToward(s.getMotorRpm(), targetRpm, RPM_EASE_PER_TICK));

        int targetTemp = TEMP_BASE + s.getMotorRpm() / 20;
        s.setBatteryTempCelsius(easeToward(s.getBatteryTempCelsius(), targetTemp, TEMP_EASE_PER_TICK));

        int batteryDelta = -s.getMotorSpeedSetting();
        if (s.isChargingButton()) batteryDelta += CHARGE_RATE_PCT_PER_TICK;
        s.setBatteryPercentage(clamp(s.getBatteryPercentage() + batteryDelta, 0, 100));

        int power = s.getMotorRpm();
        if (s.isChargingButton()) power -= CHARGING_POWER_DRAW_KW;
        s.setPowerConsumption(clamp(power, -1000, 1000));

        s.setMotorStatus(s.getMotorRpm() > MOTOR_STATUS_RPM_THRESHOLD);
        s.setBatteryLow(s.getBatteryPercentage() < BATTERY_LOW_THRESHOLD_PCT);

        if (rollPercent(PARKING_BRAKE_FLIP_CHANCE_PERCENT)) {
            s.setParkingBrake(!s.isParkingBrake());
        }
        if (rollPercent(CHECK_ENGINE_FLIP_CHANCE_PERCENT)) {
            s.setCheckEngine(!s.isCheckEngine());
        }
        if (rollPercent(GEAR_RATIO_CHANGE_CHANCE_PERCENT)) {
            s.setGearRatio(GEAR_RATIOS[random.nextInt(GEAR_RATIOS.length)]);
        }
    }

    private boolean rollPercent(int percent) {
        return random.nextInt(100) < percent;
    }

    private static int easeToward(int current, int target, int maxStep) {
        if (current < target) return Math.min(current + maxStep, target);
        if (current > target) return Math.max(current - maxStep, target);
        return current;
    }

    private static int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
