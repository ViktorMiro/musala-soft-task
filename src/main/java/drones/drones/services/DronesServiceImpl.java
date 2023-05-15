package drones.drones.services;

import drones.drones.entities.Drone;
import drones.drones.entities.Medication;
import drones.drones.entities.Route;
import drones.drones.repositories.DroneRepository;
import drones.drones.repositories.MedicationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DronesServiceImpl implements DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final EventLogService eventLogService;

    public DronesServiceImpl(DroneRepository droneRepository, MedicationRepository medicationRepository, EventLogService eventLogService) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
        this.eventLogService = eventLogService;
    }

    public Optional<Drone> getById(int id) {
        return droneRepository.findById(id);
    }

    public void delete(int id) {
        droneRepository.deleteById(id);
    }

    public List<Drone> getAll() {
        return droneRepository.findAll();
    }

    public Drone create(Drone drone) {
        return droneRepository.save(drone);
    }

    public Drone update(int id, Drone drone) {
        Drone existingDrone = droneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Drone not found"));
        existingDrone.setBattery(drone.getBattery());
        existingDrone.setModel(drone.getModel());
        existingDrone.setSerialNumber(drone.getSerialNumber());
        existingDrone.setState(drone.getState());
        existingDrone.setWeightLimit(drone.getWeightLimit());
        return droneRepository.save(existingDrone);
    }

    public void chargeDrones() {
        Collection<Drone.State> states = new ArrayList<>();
        states.add(Drone.State.IDLE);
        states.add(Drone.State.LOADED);
        List<Drone> dronesToCharge = droneRepository.findByStateInAndBatteryLessThan(states, 100);
        if (dronesToCharge.isEmpty()) return;

        StringBuilder log = new StringBuilder("Drones charging: ");
        for (Drone drone: dronesToCharge) {
            chargeDrone(drone, log);
        }
        eventLogService.logMessageForType(log.toString(), "Charging");
    }

    protected void chargeDrone(Drone drone, StringBuilder log) {
        int droneBattery = drone.getBattery();
        int newBattery = Math.min(droneBattery+ 10, 100);
        drone.setBattery(newBattery);
        droneRepository.save(drone);
        log.append(String.format(" | Drone with id - %d charged %d -> %d | ", drone.getId(), droneBattery, newBattery));
    }

    public Drone loadDroneWithMedication(int droneId, int medicationId) throws Exception {
        Drone drone = droneRepository.findById(droneId).orElseThrow(() -> new EntityNotFoundException("Drone not found"));
        if (drone.getState() != Drone.State.IDLE
            && drone.getState() != Drone.State.LOADED) throw new Exception("The drone is busy, please, choose another one or wait");
        Medication medication = medicationRepository.findById(medicationId).orElseThrow(() -> new EntityNotFoundException("Medication not found"));

        if (drone.getCurrentWeight() + medication.getWeight() > drone.getWeightLimit()) throw new Exception("Not enough space in the drone");
        drone.getMedications().add(medication);
        drone.setCurrentWeight(drone.getCurrentWeight() + medication.getWeight());
        drone.setState(Drone.State.LOADING);
        droneRepository.save(drone);

        return drone;
    }

    public Drone unloadDroneWithMedication(int droneId, int medicationId) {
        Drone drone = droneRepository.findById(droneId).orElseThrow(() -> new EntityNotFoundException("Drone not found"));
        Medication medication = medicationRepository.findById(medicationId).orElseThrow(() -> new EntityNotFoundException("Medication not found"));

        Collection<Medication> medications = drone.getMedications();
        boolean result = medications.remove(medication);
        if (result) drone.setCurrentWeight(drone.getCurrentWeight() - medication.getWeight());
        droneRepository.save(drone);

        return drone;
    }

    public Drone createRoute(int droneId, int distance) throws Exception {
        if (distance > 50) throw new Exception("Distance more than 50 km is not supported for now");
        Drone drone = droneRepository.findById(droneId).orElseThrow(() -> new EntityNotFoundException("Drone not found"));
        if (drone.getState() != Drone.State.IDLE
                && drone.getState() != Drone.State.LOADED
                && drone.getState() != Drone.State.LOADING) throw new Exception("The drone is busy, please, choose another one or wait");
        int battery = drone.getBattery();
        if (battery < 25) throw new Exception("The drone battery is below 25%, please, choose another one or wait");
        if (battery < distance*2) throw new Exception("The drone doesn't have enough charge for the route, please, choose another one or wait");

        Route route = (drone.getRoute() == null) ? new Route() : drone.getRoute();
        route.setDrone(drone);
        route.setPassedDistance(0);
        route.setTotalDistance(distance);

        drone.setRoute(route);
        droneRepository.save(drone);

        return drone;
    }

    public Drone clearRoute(int droneId) {
        Drone drone = droneRepository.findById(droneId).orElseThrow(() -> new EntityNotFoundException("Drone not found"));
        drone.setRoute(null);
        droneRepository.save(drone);

        return drone;
    }

    public void loadDrones() {
        List<Drone> dronesToCharge = droneRepository.findByState(Drone.State.LOADING);
        if (dronesToCharge.isEmpty()) return;
        for (Drone drone: dronesToCharge) {
            drone.setState(Drone.State.LOADED);
            droneRepository.save(drone);
        }
    }

    public Drone startRoute(int droneId) throws Exception {
        Drone drone = droneRepository.findById(droneId).orElseThrow(() -> new EntityNotFoundException("Drone not found"));
        if (drone.getRoute() == null) throw new Exception("The drone doesn't have a route");
        if (drone.getMedications().isEmpty()) throw new Exception("The drone is empty");
        if (drone.getState() != Drone.State.LOADED) throw new Exception("The drone doesn't ready yet");

        drone.setState(Drone.State.DELIVERING);
        droneRepository.save(drone);

        return drone;
    }

    public void moveDrones() {
        Collection<Drone.State> states = new ArrayList<>();
        states.add(Drone.State.DELIVERING);
        states.add(Drone.State.RETURNING);
        List<Drone> dronesToMove = droneRepository.findByStateIn(states);

        if (dronesToMove.isEmpty()) return;

        StringBuilder log = new StringBuilder("Drones moving: ");
        for (Drone drone: dronesToMove) {
            moveDrone(drone, log);
        }
        eventLogService.logMessageForType(log.toString(), "Charging");
    }

    protected void moveDrone(Drone drone, StringBuilder log) {
        Route route = drone.getRoute();
        int droneBattery = drone.getBattery();
        int distanceToGo = Math.min(route.getTotalDistance() - route.getPassedDistance(), 10);
        drone.setBattery(droneBattery - distanceToGo);
        route.setPassedDistance(route.getPassedDistance() + distanceToGo);

        if (route.getTotalDistance() == route.getPassedDistance()) {
            if (drone.getState() == Drone.State.RETURNING) {
                drone.setState(Drone.State.IDLE);
                drone.setRoute(null);
            } else {
                drone.setState(Drone.State.DELIVERED);
            }
        }

        droneRepository.save(drone);
        log.append(String.format(" | Drone with id - %d is moving for %d kms | ", drone.getId(), distanceToGo));
    }

    public void unloadDrones() {
        List<Drone> dronesToUnload = droneRepository.findByState(Drone.State.DELIVERED);
        if (dronesToUnload.isEmpty()) return;

        for (Drone drone: dronesToUnload) {
            drone.getRoute().setPassedDistance(0);
            drone.setMedications(null);
            drone.setCurrentWeight(0);
            drone.setState(Drone.State.RETURNING);
            droneRepository.save(drone);
        }
    }
}
