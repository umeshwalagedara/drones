package com.umesh.drones.service;

import com.umesh.drones.entity.Drone;
import com.umesh.drones.entity.DroneAuditLog;
import com.umesh.drones.repository.DroneAuditLogRepo;
import com.umesh.drones.util.CommonConstants;
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
    for (Drone drone : drones) {
      DroneAuditLog auditLog = new DroneAuditLog();

      if (drone.getBatteryCapacity() < 25) {
        auditLog.setMessage(CommonConstants.LOW_BATTERY_LEVEL);
      }else if(drone.getBatteryCapacity() > 25 && drone.getBatteryCapacity() < 50){
        auditLog.setMessage(CommonConstants.BATTERY_LEVEL_HALF);
      }else if(drone.getBatteryCapacity() > 50){
        auditLog.setMessage(CommonConstants.BATTERY_LEVEL_SUFFICIENT);
      }

      auditLog.setDroneId(drone.getId());
      auditLog.setEventDate(new Date());
      droneAuditLogRepo.save(auditLog);
    }
  }

}
