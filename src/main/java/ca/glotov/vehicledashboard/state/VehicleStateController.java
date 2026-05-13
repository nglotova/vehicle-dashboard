package ca.glotov.vehicledashboard.state;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/state")
@RequiredArgsConstructor
public class VehicleStateController {

    private final VehicleStateService service;

    @GetMapping
    public VehicleStateDto getState() {
        return service.getState();
    }

    @PatchMapping("/motor-speed")
    public VehicleStateDto setMotorSpeed(@Valid @RequestBody SetMotorSpeedRequest request) {
        return service.setMotorSpeed(request.motorSpeedSetting());
    }

    @PatchMapping("/charging-button")
    public VehicleStateDto toggleChargingButton() {
        return service.toggleChargingButton();
    }
}
