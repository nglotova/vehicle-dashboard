package ca.glotov.vehicledashboard.state;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle_state")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleState {

    public static final long SINGLETON_ID = 1L;

    @Id
    private Long id;

    //Top Row Icons
    private boolean parkingBrake;
    private boolean checkEngine;
    private boolean motorStatus;
    private boolean batteryLow;

    //Gauges
    private int powerConsumption;
    private int motorRpm;

    //Middle Row Components
    private String gearRatio;
    private int batteryPercentage;
    private int batteryTempCelsius;
    private int motorSpeedSetting;

    //Bottom Row Button
    private boolean chargingButton;
}
