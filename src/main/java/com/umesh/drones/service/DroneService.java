package com.umesh.drones.service;

import com.umesh.drones.dto.DroneDTO;
import com.umesh.drones.entity.Drone;
import com.umesh.drones.repository.DroneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroneService {

  @Autowired
  private DroneRepo droneRepo;

  public DroneDTO registerDrone(DroneDTO droneDTO) {
    Drone drone = mapToEntity(droneDTO);
    drone = droneRepo.save(drone);
    return mapToDTO(drone);

  }


  private Drone mapToEntity(DroneDTO droneDTO){
    Drone drone = Drone.builder()
        .serialNumber(droneDTO.getSerialNumber())
        .batteryCapacity(droneDTO.getBatteryCapacity())
        .medications(droneDTO.getMedications())
        .model(droneDTO.getModel())
        .state(droneDTO.getState())
        .weightLimit(droneDTO.getWeightLimit())
        .build();

    return drone;
  }

  private DroneDTO mapToDTO(Drone drone){

    return null;

  }

}
