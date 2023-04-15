package com.umesh.drones;

import com.umesh.drones.dto.DroneDTO;
import com.umesh.drones.dto.MedicationDTO;
import com.umesh.drones.entity.Drone;
import com.umesh.drones.repository.DroneRepo;
import com.umesh.drones.service.DroneService;
import com.umesh.drones.util.DroneModel;
import com.umesh.drones.util.DroneState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class DronesApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private DroneService droneService;

	@Autowired
	private DroneRepo droneRepo;

	@Test
	public void testRegisterDrone() throws Exception {
		DroneDTO droneDTO = new DroneDTO();
		droneDTO.setSerialNumber("DR_01");
		droneDTO.setWeightLimit(150.0);
		droneDTO.setBatteryCapacity(65.0);
		droneDTO.setModel(DroneModel.Cruiserweight);
		droneDTO.setState(DroneState.IDLE);


		DroneDTO result = droneService.registerDrone(droneDTO);

		assertThat(result.getSerialNumber()).isEqualTo(droneDTO.getSerialNumber());
		assertThat(result.getBatteryCapacity()).isEqualTo(droneDTO.getBatteryCapacity());
		assertThat(result.getModel()).isEqualTo(droneDTO.getModel());
		assertThat(result.getWeightLimit()).isEqualTo(droneDTO.getWeightLimit());

		Drone drone = droneRepo.findById(result.getId()).orElse(null);
		assertThat(drone).isNotNull();
		assertThat(drone.getSerialNumber()).isEqualTo(droneDTO.getSerialNumber());
		assertThat(drone.getBatteryCapacity()).isEqualTo(droneDTO.getBatteryCapacity());
		assertThat(drone.getModel()).isEqualTo(droneDTO.getModel());
		assertThat(drone.getWeightLimit()).isEqualTo(droneDTO.getWeightLimit());

	}


	@Test
	public void testRegisterDroneWithInvalidSerialNo() {
		DroneDTO droneDTO = new DroneDTO();
		// Set invalid serial number to throw exception
		droneDTO.setSerialNumber("10000000001000000000100000000010000000001000000000100000000010000000001000000000100000000010000000001");
		droneDTO.setBatteryCapacity(90.0);
		droneDTO.setWeightLimit(400.0);
		droneDTO.setModel(DroneModel.Middleweight);
		droneDTO.setState(DroneState.IDLE);
		Exception exception = assertThrows(Exception.class, () -> { droneService.registerDrone(droneDTO); });

	}

	@Test
	public void testRegisterDroneWithInvalidBatteryCapacity() {
		DroneDTO droneDTO = new DroneDTO();
		// Set invalid battery capacity to throw exception
		droneDTO.setSerialNumber("DR01");
		droneDTO.setBatteryCapacity(124.0);
		droneDTO.setWeightLimit(400.0);
		droneDTO.setModel(DroneModel.Middleweight);
		droneDTO.setState(DroneState.IDLE);
		Exception exception = assertThrows(Exception.class, () -> { droneService.registerDrone(droneDTO); });
	}

	@Test
	public void testRegisterDroneWithInvalidWeightLimit() {
		DroneDTO droneDTO = new DroneDTO();
		// Set invalid weight limit to throw exception
		droneDTO.setSerialNumber("DR01");
		droneDTO.setBatteryCapacity(90.0);
		droneDTO.setWeightLimit(600.0);
		droneDTO.setModel(DroneModel.Middleweight);
		droneDTO.setState(DroneState.IDLE);
		Exception exception = assertThrows(Exception.class, () -> { droneService.registerDrone(droneDTO); });
	}


	@Test
	public void testGetAllDrones() {

		List<Drone> drones = droneService.getAllDrones();
		int droneCount = drones.size();

		// Add sample drones to repository
		Drone drone1 = Drone.builder()
				.serialNumber("DR01").batteryCapacity(90.0).model(DroneModel.Middleweight).state(DroneState.IDLE)
				.weightLimit(300.0).build();
		Drone drone2 = Drone.builder()
				.serialNumber("DR02").batteryCapacity(90.0).model(DroneModel.Middleweight).state(DroneState.IDLE)
				.weightLimit(300.0).build();

		droneRepo.saveAll(Arrays.asList(drone1, drone2));

		// Get all drones using the service method
		List<Drone> allDronesNow = droneService.getAllDrones();

		// Check if the size of the returned list matches the number of drones in the repository
		int count = allDronesNow.size() - droneCount;

		assertEquals(count, 2);

	}

	@Test
	public void testGetBatteryLevel() {
		// Create a new drone and save it to the repository
		Drone drone = new Drone();
		drone.setWeightLimit(400.0);
		drone.setState(DroneState.IDLE);
		drone.setBatteryCapacity(90.0);
		drone.setModel(DroneModel.Cruiserweight);
		drone.setState(DroneState.IDLE);
		drone = droneRepo.save(drone);

		// Call the method being tested
		double batteryLevel = droneService.getBatteryLevel(drone.getId());

		// Assert the result
		assertEquals(90.0, batteryLevel, 0.0);
	}

	@Test
	public void testGetAvailableDrones() throws Exception {

		List<DroneDTO> result = droneService.getAvailableDrones();
		int initialAvailableDroneCount = result.size();

		// create some dummy drone data
		Drone drone1 = Drone.builder()
				.serialNumber("DR011").batteryCapacity(90.0).model(DroneModel.Middleweight).state(DroneState.IDLE)
				.weightLimit(300.0).build();
		Drone drone2 = Drone.builder()
				.serialNumber("DR021").batteryCapacity(90.0).model(DroneModel.Middleweight).state(DroneState.LOADING)
				.weightLimit(300.0).build();
		Drone drone3 = Drone.builder()
				.serialNumber("DR022").batteryCapacity(90.0).model(DroneModel.Middleweight).state(DroneState.IDLE)
				.weightLimit(300.0).build();

		droneRepo.saveAll(Arrays.asList(drone1, drone2, drone3));


		// call the service method again
		List<DroneDTO> newResult = droneService.getAvailableDrones();
		int availableDronesNow = newResult.size();

		// verify the result is as expected      as the newly added IDLE drones are 2
		assertEquals(2, availableDronesNow - initialAvailableDroneCount);

	}


		@Test
		public void testLoadDrone() throws Exception {

			Drone drone = new Drone();
			drone.setState(DroneState.IDLE);
			drone.setBatteryCapacity(60.0);
			drone.setWeightLimit(200.0);
			drone.setModel(DroneModel.Middleweight);
			drone.setSerialNumber("BR01");
			drone = droneRepo.save(drone);

			List<MedicationDTO> medicationDTOS = new ArrayList<>();
			MedicationDTO medicationDTO = new MedicationDTO();
			medicationDTO.setCode("CODE");
			medicationDTO.setName("Panadol");
			medicationDTO.setWeight(25.0);
			medicationDTO.setImage(null);
			medicationDTOS.add(medicationDTO);

			// calling test method
			droneService.loadDrone(drone.getId(), medicationDTOS);

			// Assert
			Optional<Drone> updatedDroneOptional = droneRepo.findById(drone.getId());
			assertEquals(DroneState.LOADED, updatedDroneOptional.get().getState());
		}

	@Test
	public void testLoadDroneWithInvalidDroneId() throws Exception {
		// Arrange
		Long invalidDroneId = -1L;
		List<MedicationDTO> medicationDTOS = new ArrayList<>();
		MedicationDTO medicationDTO = new MedicationDTO();
		medicationDTO.setCode("CODE");
		medicationDTO.setName("Panadol");
		medicationDTO.setWeight(25.0);
		medicationDTO.setImage(null);
		medicationDTOS.add(medicationDTO);

		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> droneService.loadDrone(invalidDroneId, medicationDTOS));
	}

	@Test
	public void testLoadDroneWithInvalidBatteryCapacity() throws Exception {

		Drone drone = new Drone();
		drone.setState(DroneState.IDLE);
		drone.setBatteryCapacity(10.0);
		drone.setWeightLimit(200.0);
		drone.setModel(DroneModel.Middleweight);
		drone.setSerialNumber("BR01");
		drone = droneRepo.save(drone);

		List<MedicationDTO> medicationDTOS = new ArrayList<>();
		MedicationDTO medicationDTO = new MedicationDTO();
		medicationDTO.setCode("CODE");
		medicationDTO.setName("Panadol");
		medicationDTO.setWeight(25.0);
		medicationDTO.setImage(null);
		medicationDTOS.add(medicationDTO);


		// Assert
		final Drone finalDrone = drone;
		assertThrows(IllegalStateException.class, () -> droneService.loadDrone(finalDrone.getId(), medicationDTOS));
	}


	@Test
	public void testLoadDroneWithInvalidWeight() throws Exception {

		Drone drone = new Drone();
		drone.setState(DroneState.IDLE);
		drone.setBatteryCapacity(10.0);
		drone.setWeightLimit(200.0);
		drone.setModel(DroneModel.Middleweight);
		drone.setSerialNumber("BR011");
		drone = droneRepo.save(drone);

		List<MedicationDTO> medicationDTOS = new ArrayList<>();
		MedicationDTO medicationDTO = new MedicationDTO();
		medicationDTO.setCode("CODE");
		medicationDTO.setName("Panadol");
		medicationDTO.setWeight(50.0);
		medicationDTO.setImage(null);
		medicationDTOS.add(medicationDTO);

		MedicationDTO medicationDTO1 = new MedicationDTO();
		medicationDTO1.setCode("CODE");
		medicationDTO1.setName("Panadol");
		medicationDTO1.setWeight(175.0);
		medicationDTO1.setImage(null);
		medicationDTOS.add(medicationDTO1);


		// Assert
		final Drone finalDrone = drone;
		assertThrows(IllegalStateException.class, () -> droneService.loadDrone(finalDrone.getId(), medicationDTOS));
	}







}
