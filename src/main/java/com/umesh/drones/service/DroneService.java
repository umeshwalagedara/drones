package com.umesh.drones.service;

import com.umesh.drones.dto.DroneDTO;
import com.umesh.drones.dto.MedicationDTO;
import com.umesh.drones.entity.Drone;
import com.umesh.drones.entity.Medication;
import com.umesh.drones.repository.DroneRepo;
import com.umesh.drones.util.DroneState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroneService {

  @Autowired
  private DroneRepo droneRepo;
  @Autowired
  private MedicationService medicationService;

  /**
   *
   * @param droneDTO
   * @return
   */
  public DroneDTO registerDrone(DroneDTO droneDTO) throws Exception {

    try{
      Drone drone = mapToEntity(droneDTO);
      drone = droneRepo.save(drone);
      return mapToDTO(drone);
    }catch (Exception e){
        throw new Exception(e);
    }

  }

  public List<DroneDTO> getDrones() {
    List<DroneDTO> droneDTOS = new ArrayList<>();
    Iterable<Drone> iterable = droneRepo.findAll();
    List<Drone> droneList = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    for(Drone d : droneList){
      DroneDTO droneDTO = mapToDTO(d);
      droneDTOS.add(droneDTO);
    }

  return droneDTOS;
  }


  public List<Drone> getAllDrones() {
    List<DroneDTO> droneDTOS = new ArrayList<>();
    Iterable<Drone> iterable = droneRepo.findAll();
    List<Drone> droneList = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    return droneList;
  }

  /**
   * Convert a droneDTO to a drone object
   * The setters will have validations to check if the dto contains valid data
   * @param droneDTO
   * @return
   */
  private Drone mapToEntity(DroneDTO droneDTO){
    Drone drone = new Drone();
    drone.setBatteryCapacity(droneDTO.getBatteryCapacity());
    drone.setModel(droneDTO.getModel());
    drone.setState(droneDTO.getState());
    drone.setSerialNumber(droneDTO.getSerialNumber());
    drone.setWeightLimit(droneDTO.getWeightLimit());

    return drone;
  }

  private DroneDTO mapToDTO(Drone drone){
    DroneDTO droneDTO = DroneDTO.builder()
        .id(drone.getId())
        .batteryCapacity(drone.getBatteryCapacity())
        .model(drone.getModel())
        .serialNumber(drone.getSerialNumber())
        .state(drone.getState())
        .weightLimit(drone.getWeightLimit())
        .build();

    return droneDTO;
  }

  public double getBatteryLevel(final Long droneId) {
    Optional<Drone> drone =  droneRepo.findById(droneId);
    return  drone.get().getBatteryCapacity();
  }


  public List<DroneDTO> getAvailableDrones() {
    List<Drone> availableDrones = droneRepo.findByState(DroneState.IDLE);
    return availableDrones.stream().map(this::mapToDTO).collect(Collectors.toList());
  }


  public List<MedicationDTO> getLoadedMedications(final Long droneId) {
    Drone drone = droneRepo.findById(droneId).orElseThrow(() -> new IllegalArgumentException("Invalid drone id: " + droneId));
    List<MedicationDTO> medicationDTOS = new ArrayList<>();
    for(Medication medication: drone.getMedications()){
      medicationDTOS.add(medicationService.mapToDTO(medication));
    }

    return medicationDTOS;
  }


  public Drone getDroneById(final Long id) {
    Optional<Drone> droneOptional = droneRepo.findById(id);
    Drone drone = droneOptional.orElseThrow( () -> new IllegalArgumentException("Invalid drone id: " + id));
    return drone;
  }


  public void loadDrone(final Long droneId, List<MedicationDTO> medicationDTOS) {

      List<Medication> medicationList = medicationService.addNewMedication(medicationDTOS);
      Optional<Drone> droneOptional = droneRepo.findById(droneId);
      Drone drone = droneOptional.orElseThrow( () -> new IllegalArgumentException("Invalid drone id: " + droneId));

      if(drone.getState() == DroneState.IDLE){
        throw new IllegalStateException("Drone must be in IDLE state to be loaded");
      }

      double totalWeight = medicationList.stream().mapToDouble(Medication::getWeight).sum();
      if (totalWeight > drone.getWeightLimit()) {
        throw new IllegalArgumentException("Total weight of medications exceeds drone's weight limit");
      }

      drone.setMedications(medicationList);
      drone.setState(DroneState.LOADED);
      droneRepo.save(drone);

  }

}
