package com.umesh.drones.repository;

import com.umesh.drones.entity.Medication;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepo extends CrudRepository<Medication, Long> {

  public List<Medication> findByDroneId(Long droneId);

}
