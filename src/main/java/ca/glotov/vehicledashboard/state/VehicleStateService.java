package ca.glotov.vehicledashboard.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleStateService {

    private final VehicleStateRepository repository;

    @Transactional(readOnly = true)
    public VehicleStateDto getState() {
        return VehicleStateDto.from(load());
    }

    @Transactional
    public VehicleStateDto setMotorSpeed(int setting) {
        VehicleState entity = load();
        entity.setMotorSpeedSetting(setting);
        return VehicleStateDto.from(entity);
    }

    @Transactional
    public VehicleStateDto toggleChargingButton() {
        VehicleState entity = load();
        entity.setChargingButton(!entity.isChargingButton());
        return VehicleStateDto.from(entity);
    }

    private VehicleState load() {
        return repository.findById(VehicleState.SINGLETON_ID)
                .orElseThrow(() -> new IllegalStateException(
                        "Vehicle state singleton row missing; seeder did not run?"));
    }
}
