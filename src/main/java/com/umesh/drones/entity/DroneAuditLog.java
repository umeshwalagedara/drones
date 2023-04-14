package com.umesh.drones.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "drone_audit_log")
public class DroneAuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private Long droneId;
  private Date eventDate;
  private String message;


}
