package com.umesh.drones.dto;


import com.umesh.drones.entity.Medication;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoadedDroneDTO extends DroneDTO{

  private List<Medication> medications = new ArrayList<>();

}
