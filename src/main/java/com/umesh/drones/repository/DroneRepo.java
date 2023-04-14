package com.umesh.drones.repository;

import com.umesh.drones.entity.Drone;
import com.umesh.drones.util.DroneState;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepo extends CrudRepository<Drone, Long> {

  public List<Drone> findByState(DroneState droneState);

}
