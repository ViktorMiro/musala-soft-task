package drones.drones.services;

import drones.drones.entities.Drone;

import java.util.List;
import java.util.Optional;

public interface DroneService {

    Optional<Drone> getById(int id);
    void delete(int id);
    List<Drone> getAll();
    Drone create(Drone drone);
    Drone update(int id, Drone drone);
    void chargeDrones();
    Drone loadDroneWithMedication(int droneId, int medicationId) throws Exception;
    Drone unloadDroneWithMedication(int droneId, int medicationId);
    Drone createRoute(int droneId, int distance) throws Exception;
    Drone clearRoute(int droneId);
    void loadDrones();
    Drone startRoute(int droneId) throws Exception;
    void moveDrones();
    void unloadDrones();
}
