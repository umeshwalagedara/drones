package com.umesh.drones.service;

import com.umesh.drones.dto.MedicationDTO;
import com.umesh.drones.entity.Medication;
import com.umesh.drones.repository.MedicationRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicationService {

  @Autowired
  private MedicationRepo medicationRepo;

  public List<MedicationDTO> findByDroneId(final Long id) {
    List<Medication> medicationList =  medicationRepo.findByDroneId(id);
    return medicationList.stream().map(this::mapToDTO).collect(Collectors.toList());
  }

  public MedicationDTO mapToDTO(Medication medication) {
    MedicationDTO dto = new MedicationDTO();
    dto.setId(medication.getId());
    dto.setName(medication.getName());
    dto.setWeight(medication.getWeight());
    dto.setCode(medication.getCode());
    dto.setImage(medication.getImage());
    return dto;
  }

}
