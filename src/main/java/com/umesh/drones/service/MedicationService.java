package com.umesh.drones.service;

import com.umesh.drones.dto.MedicationDTO;
import com.umesh.drones.entity.Medication;
import com.umesh.drones.repository.MedicationRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicationService {

  @Autowired
  private MedicationRepo medicationRepo;

  public List<Medication> addNewMedication(List<MedicationDTO> medicationDTOS){

      List<Medication> medicationList = new ArrayList<>();
      for(MedicationDTO dto: medicationDTOS){

        Medication medication = new Medication();
        if(dto.getId() == 0){
          medication.setName(dto.getName());
          medication.setImage(dto.getImage());
          medication.setWeight(dto.getWeight());
          medication.setCode(dto.getCode());
          medication = medicationRepo.save(medication);
          medicationList.add(medication);

        }else{
          Optional<Medication> med =  medicationRepo.findById(dto.getId());
          Medication medication1 = med.orElseThrow(() -> new IllegalArgumentException("Invalid medication id: " + dto.getId()));
          medicationList.add(medication1);

          // already added will not be allowed to modify
        }
      }
      return medicationList;
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
