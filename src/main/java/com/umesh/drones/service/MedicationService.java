package com.umesh.drones.service;

import com.umesh.drones.dto.MedicationDTO;
import com.umesh.drones.entity.Medication;
import com.umesh.drones.repository.MedicationRepo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicationService {

  @Autowired
  private MedicationRepo medicationRepo;

  public void addNewMedication(List<MedicationDTO> medicationDTOS){

    List<Medication> medicationList = new ArrayList<>();

    for(MedicationDTO dto: medicationDTOS){
      Medication medication = new Medication();

      if(dto.getId() == 0){


      }else{
        // already added
      }


    }

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
