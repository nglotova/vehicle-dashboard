package ca.glotov.vehicledashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VehicleDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleDashboardApplication.class, args);
	}

}
