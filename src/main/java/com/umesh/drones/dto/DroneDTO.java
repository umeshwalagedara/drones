package com.umesh.drones.dto;

import com.umesh.drones.entity.Medication;
import com.umesh.drones.util.DroneModel;
import com.umesh.drones.util.DroneState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DroneDTO {

  private Long id;
  private String serialNumber;
  private DroneModel model;
  private Double weightLimit;
  private Double batteryCapacity;
  private DroneState state;
  private List<Medication> medications = new ArrayList<>();

}
