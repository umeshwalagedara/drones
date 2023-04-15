package com.umesh.drones.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "medication")
public class Medication {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Pattern(regexp = "^[A-Z_0-9_-]+$")
  private String name;

  private Double weight;

  @Pattern(regexp = "^[A-Z_0-9_]+$")
  private String code;

  private byte[] image;

  private Long droneId;

  public void setId(final Long id) {
    this.id = id;
  }

  public void setName(final String name) {
    if (!name.matches("[A-Za-z0-9_-]+")) {
      throw new IllegalArgumentException("Name contains invalid characters.");
    }
    this.name = name;
  }

  public void setWeight(final Double weight) {
    this.weight = weight;
  }

  public void setCode(final String code) {
    if (!code.matches("[A-Z_0-9_]+")) {
      throw new IllegalArgumentException("Code contains invalid characters.");
    }
    this.code = code;
  }

  public void setImage(final byte[] image) {
    this.image = image;
  }


  public void setDroneId(final Long droneId) {
    this.droneId = droneId;
  }
}
