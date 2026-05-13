package ca.glotov.vehicledashboard.state;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleStateSeeder {

    private final VehicleStateRepository repository;

    @PostConstruct
    public void seed() {
        if (repository.existsById(VehicleState.SINGLETON_ID)) {
            return;
        }
        repository.save(VehicleState.builder()
                .id(VehicleState.SINGLETON_ID)
                .parkingBrake(true)
                .checkEngine(false)
                .motorStatus(false)
                .batteryLow(false)
                .powerConsumption(0)
                .motorRpm(0)
                .gearRatio("N")
                .batteryPercentage(100)
                .batteryTempCelsius(25)
                .motorSpeedSetting(0)
                .chargingButton(false)
                .build());
    }
}
