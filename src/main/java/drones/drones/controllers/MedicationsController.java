package drones.drones.controllers;

import drones.drones.entities.Medication;
import drones.drones.services.MedicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/medications")
public class MedicationsController {
    private final MedicationService medicationService;

    public MedicationsController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping("")
    public List<Medication> getAll() {
        return medicationService.getAll();
    }

    @GetMapping("/{id}")
    public Medication getById(@PathVariable int id) {
        try {
            return medicationService.getById(id).orElse(null);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medication not found", exception);
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        try {
            medicationService.delete(id);
            return "Successfully deleted.";
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medication not found", exception);
        }
    }

    @PostMapping("/")
    public Medication save(@RequestBody Medication medication) {
        try {
            return medicationService.create(medication);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during medication creation: " + exception.getMessage(), exception);
        }
    }

    @PutMapping("/{id}")
    public Medication update(@PathVariable("id") int id, @RequestBody Medication medication){
        try {
            return medicationService.update(id, medication);
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }
}
