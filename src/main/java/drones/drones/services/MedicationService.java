package drones.drones.services;

import drones.drones.entities.Medication;

import java.util.List;
import java.util.Optional;

public interface MedicationService {
    Optional<Medication> getById(int id);
    void delete(int id);
    List<Medication> getAll();
    Medication create(Medication medication);
    Medication update(int id, Medication medication);
}
