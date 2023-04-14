package com.umesh.drones.entity;

import com.umesh.drones.util.DroneModel;
import com.umesh.drones.util.DroneState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "drones")
public class Drone {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(length = 100)
  private String serialNumber;

  @Enumerated(EnumType.STRING)
  private DroneModel model;

  private Double weightLimit;
  private Double batteryCapacity;

  @Enumerated(EnumType.STRING)
  private DroneState state;

  @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Medication> medications = new ArrayList<>();

  public void setId(final Long id) {
    this.id = id;
  }

  public void setSerialNumber(final String serialNumber) {
    if(serialNumber!=null && serialNumber.length() > 100){
      throw new IllegalArgumentException("Serial Number is too lengthy.");
    }
    this.serialNumber = serialNumber;
  }

  public void setModel(final DroneModel model) {
    this.model = model;
  }

  public void setWeightLimit(final Double weightLimit) {
    if (weightLimit > 500) {
      throw new IllegalArgumentException("Weight is too heavy.");
    }
    this.weightLimit = weightLimit;
  }

  public void setBatteryCapacity(final Double batteryCapacity) {
    this.batteryCapacity = batteryCapacity;
  }

  public void setState(final DroneState state) {
    this.state = state;
  }

  public void setMedications(final List<Medication> medications) {
    this.medications = medications;
  }
}
