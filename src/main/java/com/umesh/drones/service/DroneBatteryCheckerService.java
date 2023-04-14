package com.umesh.drones.service;

import com.umesh.drones.entity.Drone;
import com.umesh.drones.entity.DroneAuditLog;
import com.umesh.drones.repository.DroneAuditLogRepo;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DroneBatteryCheckerService {

  @Autowired
  private DroneService droneService;
  @Autowired
  private DroneAuditLogRepo droneAuditLogRepo;


  @Scheduled(fixedDelay = 60000) // run every minute
  public void checkBatteryLevels() {
    List<Drone> drones = droneService.getAllDrones();
    DroneAuditLog auditLog = new DroneAuditLog();
    for (Drone drone : drones) {
      if (drone.getBatteryCapacity() < 25) {
        auditLog.setMessage("Low battery level");
      }else if(drone.getBatteryCapacity() > 25 && drone.getBatteryCapacity() < 50){
        auditLog.setMessage("Battery reached half mark.");
      }else if(drone.getBatteryCapacity() > 50){
        auditLog.setMessage("Battery is sufficiently charged.");
      }
      auditLog.setDroneId(drone.getId());
      auditLog.setEventDate(new Date());
      droneAuditLogRepo.save(auditLog);
    }
  }

}