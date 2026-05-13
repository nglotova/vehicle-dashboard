package ca.glotov.vehicledashboard.state;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SetMotorSpeedRequest(
        @NotNull @Min(0) @Max(4) Integer motorSpeedSetting
) {}
