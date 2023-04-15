package com.umesh.drones.service;

import com.umesh.drones.dto.DroneDTO;
import com.umesh.drones.dto.MedicationDTO;
import com.umesh.drones.entity.Drone;
import com.umesh.drones.entity.Medication;
import com.umesh.drones.repository.DroneRepo;
import com.umesh.drones.repository.MedicationRepo;
import com.umesh.drones.util.DroneState;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DroneService {

  private static final Logger LOGGER = Logger.getLogger(DroneService.class.getName());

  @Autowired
  private DroneRepo droneRepo;
  @Autowired
  private MedicationService medicationService;
  @Autowired
  private MedicationRepo medicationRepo;



  public DroneDTO registerDrone(DroneDTO droneDTO) throws Exception {
      Drone drone = mapToEntity(droneDTO);
      drone = droneRepo.save(drone);
      return mapToDTO(drone);
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
    List<MedicationDTO> medicationDTOList = medicationService.findByDroneId(drone.getId());
    return medicationDTOList;
  }


  @Transactional
  public void loadDrone(final Long droneId, List<MedicationDTO> medicationDTOS) throws Exception {

    Optional<Drone> droneOptional = droneRepo.findById(droneId);
    Drone drone = droneOptional.orElseThrow( () -> new IllegalArgumentException("Invalid drone id: " + droneId));

      if(drone.getState() != DroneState.IDLE){
        throw new IllegalStateException("Drone must be in IDLE state to be loaded");
      }

      if(drone.getBatteryCapacity() < 25){
        throw new IllegalStateException("Low Battery Capacity for loading");
      }

      double totalWeight = medicationDTOS.stream().mapToDouble(MedicationDTO::getWeight).sum();
      if (totalWeight > drone.getWeightLimit()) {
        throw new IllegalArgumentException("Total weight of medications exceeds drone's weight limit");
      }

      try {
        // set the drone to LOADING state
        drone.setState(DroneState.LOADING);
        droneRepo.save(drone);

        List<Medication> medicationList = new ArrayList<>();
        for(MedicationDTO dto: medicationDTOS){
          Medication medication = new Medication();
          medication.setName(dto.getName());
          medication.setImage(dto.getImage());
          medication.setWeight(dto.getWeight());
          medication.setCode(dto.getCode());
          medication.setDroneId(drone.getId());
          medicationList.add(medication);
        }

        medicationRepo.saveAll(medicationList);
        drone.setState(DroneState.LOADED);
        droneRepo.save(drone);

      } catch (Exception e) {
        LOGGER.severe("Error Loading the Drone" + e.getMessage());
        drone.setState(DroneState.IDLE);
        droneRepo.save(drone);
        throw new Exception(" Error Loading the Drone ");
      }

  }

}
