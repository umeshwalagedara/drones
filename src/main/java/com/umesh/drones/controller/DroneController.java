package com.umesh.drones.controller;

import com.umesh.drones.dto.DroneDTO;
import com.umesh.drones.dto.MedicationDTO;
import com.umesh.drones.entity.DroneAuditLog;
import com.umesh.drones.repository.DroneAuditLogRepo;
import com.umesh.drones.service.DroneService;
import com.umesh.drones.service.MedicationService;
import com.umesh.drones.util.ResponseMessage;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drones")
public class DroneController {

  private static final Logger LOGGER = Logger.getLogger(DroneController.class.getName());

  @Autowired
  private DroneService droneService;
  @Autowired
  private MedicationService medicationService;

  @PostMapping
  public ResponseEntity registerDrone(@RequestBody DroneDTO droneDTO) {
    try{
      DroneDTO registeredDrone = droneService.registerDrone(droneDTO);
      return ResponseEntity.ok(registeredDrone);
    }catch (Exception e){
      LOGGER.severe(e.getMessage());
      ResponseMessage errorResponse = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), System.currentTimeMillis() );
      return ResponseEntity.status(500).body(errorResponse);
    }
  }

  @PostMapping("/{droneId}/medications")
  public ResponseEntity loadDrone(@PathVariable Long droneId, @RequestBody List<MedicationDTO> medications) {

    try{
      droneService.loadDrone(droneId, medications);
      ResponseMessage successResponse  = new ResponseMessage(HttpStatus.OK.value(), "Drone loaded successfully.", System.currentTimeMillis());
      return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }catch (Exception e){
      LOGGER.severe(e.getMessage());
      ResponseMessage errorResponse = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), System.currentTimeMillis() );
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }


  @GetMapping("/{droneId}/battery")
  public ResponseEntity getBatteryLevel(@PathVariable Long droneId) {
    try {
      double batteryLevel = droneService.getBatteryLevel(droneId);
      return ResponseEntity.ok(batteryLevel);

    } catch (Exception e) {
      LOGGER.severe(e.getMessage());
      ResponseMessage errorResponse = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
          e.getMessage(), System.currentTimeMillis());
      return ResponseEntity.status(500).body(errorResponse);
    }
  }


  @GetMapping("/available")
  public ResponseEntity getAvailableDrones() {
    try{
      List<DroneDTO> availableDrones = droneService.getAvailableDrones();
      return ResponseEntity.ok(availableDrones);
    }catch (Exception e){
      LOGGER.severe(e.getMessage());
      ResponseMessage errorResponse = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), System.currentTimeMillis());
      return ResponseEntity.status(500).body(errorResponse);
    }
  }

  @GetMapping("/{droneId}/medications")
  public ResponseEntity getLoadedMedications(@PathVariable Long droneId) {

    try{
      List<MedicationDTO> loadedMedications = droneService.getLoadedMedications(droneId);
      return ResponseEntity.ok(loadedMedications);
    }catch (Exception e){
      LOGGER.severe(e.getMessage());
      ResponseMessage errorResponse = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), System.currentTimeMillis());
      return ResponseEntity.status(500).body(errorResponse);
    }

  }

  /////////////////////////////////////////TODO- to be removed /////////////////////////

  @GetMapping
  public ResponseEntity<List<DroneDTO>> getDrones() {
    List<DroneDTO> droneDTOS  = droneService.getDrones();
    return ResponseEntity.ok(droneDTOS);
  }

  @Autowired
  private DroneAuditLogRepo droneAuditLogRepo;

  @GetMapping("/audit")
  public Iterable<DroneAuditLog> getAll(){
       return droneAuditLogRepo.findAll();
  }



}
