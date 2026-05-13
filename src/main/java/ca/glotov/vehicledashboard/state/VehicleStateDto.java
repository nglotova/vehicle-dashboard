package ca.glotov.vehicledashboard.state;

public record VehicleStateDto(
        boolean parkingBrake,
        boolean checkEngine,
        boolean motorStatus,
        boolean batteryLow,
        int powerConsumption,
        int motorRpm,
        String gearRatio,
        int batteryPercentage,
        int batteryTempCelsius,
        int motorSpeedSetting,
        boolean chargingButton
) {
    public static VehicleStateDto from(VehicleState entity) {
        return new VehicleStateDto(
                entity.isParkingBrake(),
                entity.isCheckEngine(),
                entity.isMotorStatus(),
                entity.isBatteryLow(),
                entity.getPowerConsumption(),
                entity.getMotorRpm(),
                entity.getGearRatio(),
                entity.getBatteryPercentage(),
                entity.getBatteryTempCelsius(),
                entity.getMotorSpeedSetting(),
                entity.isChargingButton()
        );
    }
}
