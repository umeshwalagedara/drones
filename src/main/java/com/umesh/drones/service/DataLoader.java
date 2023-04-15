package com.umesh.drones.service;

import com.umesh.drones.entity.Drone;
import com.umesh.drones.repository.DroneRepo;
import com.umesh.drones.repository.MedicationRepo;
import com.umesh.drones.util.DroneModel;
import com.umesh.drones.util.DroneState;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DataLoader implements CommandLineRunner {

  @Autowired
  private DroneRepo droneRepo;
  @Autowired
  private MedicationRepo medicationRepo;

  @Override
  public void run(final String... args) throws Exception {

    Drone drone1 = Drone.builder()
        .serialNumber("DR01").weightLimit(400.0).state(DroneState.IDLE).model(DroneModel.Lightweight)
        .batteryCapacity(40.5)
            .build();

    Drone drone2 = Drone.builder()
        .serialNumber("DR02").weightLimit(500.0).state(DroneState.IDLE).model(DroneModel.Middleweight)
        .batteryCapacity(70.0)
        .build();

    Drone drone3 = Drone.builder()
        .serialNumber("DR03").weightLimit(500.0).state(DroneState.IDLE).model(DroneModel.Middleweight)
        .batteryCapacity(50.0)
        .build();

    Drone drone4 = Drone.builder()
        .serialNumber("DR04").weightLimit(150.0).state(DroneState.IDLE).model(DroneModel.Lightweight)
        .batteryCapacity(90.0)
        .build();

    Drone drone5 = Drone.builder()
        .serialNumber("DR05").weightLimit(500.0).state(DroneState.IDLE).model(DroneModel.Cruiserweight)
        .batteryCapacity(90.0)
        .build();

    Drone drone6 = Drone.builder()
        .serialNumber("DR06").weightLimit(500.0).state(DroneState.IDLE).model(DroneModel.Heavyweight)
        .batteryCapacity(90.0)
        .build();

    Drone drone7 = Drone.builder()
        .serialNumber("DR07").weightLimit(500.0).state(DroneState.IDLE).model(DroneModel.Heavyweight)
        .batteryCapacity(78.0)
        .build();

    Drone drone8 = Drone.builder()
        .serialNumber("DR08").weightLimit(400.0).state(DroneState.IDLE).model(DroneModel.Cruiserweight)
        .batteryCapacity(18.0)
        .build();

    Drone drone9 = Drone.builder()
        .serialNumber("DR09").weightLimit(150.0).state(DroneState.IDLE).model(DroneModel.Lightweight)
        .batteryCapacity(65.0)
        .build();

    Drone drone10 = Drone.builder()
        .serialNumber("DR010").weightLimit(150.0).state(DroneState.IDLE).model(DroneModel.Middleweight)
        .batteryCapacity(12.0)
        .build();

    droneRepo.saveAll(Arrays.asList(drone1, drone2, drone3,drone4, drone5, drone6, drone7, drone8, drone9, drone10));

  }
}
