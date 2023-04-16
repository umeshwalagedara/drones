package com.umesh.drones.service;

import com.umesh.drones.dto.DroneDTO;
import com.umesh.drones.dto.MedicationDTO;
import com.umesh.drones.entity.Drone;
import com.umesh.drones.entity.Medication;
import com.umesh.drones.repository.DroneRepo;
import com.umesh.drones.repository.MedicationRepo;
import com.umesh.drones.util.CommonConstants;
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

      // Not allowing to register a drone in other than idle state
      if(drone.getState() != DroneState.IDLE){
        throw new IllegalArgumentException(CommonConstants.ERROR_DRONE_NOT_IN_IDLE);
      }

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
    Drone drone = droneRepo.findById(droneId).orElseThrow(() -> new IllegalArgumentException(CommonConstants.ERROR_INVALID_DRONE_ID + droneId));
    List<MedicationDTO> medicationDTOList = medicationService.findByDroneId(drone.getId());
    return medicationDTOList;
  }


  @Transactional
  public void loadDrone(final Long droneId, List<MedicationDTO> medicationDTOS) throws Exception {

    Optional<Drone> droneOptional = droneRepo.findById(droneId);
    Drone drone = droneOptional.orElseThrow( () -> new IllegalArgumentException(CommonConstants.ERROR_INVALID_DRONE_ID + droneId));

      if(drone.getState() != DroneState.IDLE){
        throw new IllegalStateException(CommonConstants.ERROR_DRONE_NOT_IN_IDLE_FOR_LOADING);
      }

      if(drone.getBatteryCapacity() < 25){
        throw new IllegalStateException(CommonConstants.ERROR_LOW_BATTERY_CAPACITY);
      }

      double totalWeight = medicationDTOS.stream().mapToDouble(MedicationDTO::getWeight).sum();
      if (totalWeight > drone.getWeightLimit()) {
        throw new IllegalArgumentException(CommonConstants.ERROR_WEIGHT_LIMIT_EXCEEDS);
      }

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

      try {
        // set the drone to LOADING state
        drone.setState(DroneState.LOADING);
        droneRepo.save(drone);

        medicationRepo.saveAll(medicationList);
        drone.setState(DroneState.LOADED);
        droneRepo.save(drone);

      } catch (Exception e) {
        LOGGER.severe(CommonConstants.ERROR_LOADING_DRONE + e.getMessage());
        drone.setState(DroneState.IDLE);
        droneRepo.save(drone);
        throw new Exception(CommonConstants.ERROR_LOADING_DRONE);
      }

  }

}
