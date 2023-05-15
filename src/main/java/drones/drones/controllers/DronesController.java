package drones.drones.controllers;

import drones.drones.entities.Drone;
import drones.drones.services.DroneService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/drones")
public class DronesController {
    private final DroneService dronesService;

    public DronesController(DroneService dronesService) {
        this.dronesService = dronesService;
    }

    @GetMapping("")
    public List<Drone> getAll() {
        return dronesService.getAll();
    }

    @GetMapping("/{id}")
    public Drone getById(@PathVariable int id) {
        try {
            return dronesService.getById(id).orElse(null);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Drone not found", exception);
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        try {
            dronesService.delete(id);
            return "Successfully deleted.";
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Drone not found", exception);
        }
    }

    @PostMapping("/")
    public Drone save(@RequestBody Drone drone) {
        try {
            return dronesService.create(drone);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during drone creation: " + exception.getMessage(), exception);
        }
    }

    @PutMapping("/{id}")
    public Drone update(@PathVariable("id") int id, @RequestBody Drone drone){
        try {
            return dronesService.update(id, drone);
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @PostMapping("/{droneId}/load/{medicationId}")
    public Drone loadMedication(@PathVariable int droneId, @PathVariable int medicationId) {
        try {
            return dronesService.loadDroneWithMedication(droneId, medicationId);
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during drone loading: " + exception.getMessage(), exception);
        }
    }

    @PostMapping("/{droneId}/unload/{medicationId}")
    public Drone unloadMedication(@PathVariable int droneId, @PathVariable int medicationId) {
        try {
            return dronesService.unloadDroneWithMedication(droneId, medicationId);
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during drone unloading: " + exception.getMessage(), exception);
        }
    }

    @PostMapping("/{droneId}/create_route/{distance}")
    public Drone createRoute(@PathVariable int droneId, @PathVariable int distance) {
        try {
            return dronesService.createRoute(droneId, distance);
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during the route creation: " + exception.getMessage(), exception);
        }
    }

    @PostMapping("/{droneId}/clear_route")
    public Drone clearRoute(@PathVariable int droneId) {
        try {
            return dronesService.clearRoute(droneId);
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during the route clearing: " + exception.getMessage(), exception);
        }
    }

    @PostMapping("/{droneId}/start")
    public Drone start(@PathVariable int droneId) {
        try {
            return dronesService.startRoute(droneId);
        } catch (EntityNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during the drone launch: " + exception.getMessage(), exception);
        }
    }


}
