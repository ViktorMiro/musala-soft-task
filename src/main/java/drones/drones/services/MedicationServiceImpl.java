package drones.drones.services;

import drones.drones.entities.Medication;
import drones.drones.repositories.MedicationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationServiceImpl implements MedicationService {
    private final MedicationRepository medicationRepository;

    public MedicationServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Optional<Medication> getById(int id) {
        return medicationRepository.findById(id);
    }

    public void delete(int id) {
        medicationRepository.deleteById(id);
    }

    public List<Medication> getAll() {
        return medicationRepository.findAll();
    }

    public Medication create(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication update(int id, Medication medication) {
        Medication existingMedication = medicationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medication not found"));
        existingMedication.setCode(medication.getCode());
        existingMedication.setImage(medication.getImage());
        existingMedication.setName(medication.getName());
        existingMedication.setWeight(medication.getWeight());
        return medicationRepository.save(existingMedication);
    }
}
