package com.umesh.drones.repository;

import com.umesh.drones.entity.Drone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepo extends CrudRepository<Drone, Long> {

}
