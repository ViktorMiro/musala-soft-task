package drones.drones.scheduled;

import drones.drones.services.DroneService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TickHandler {
    private final DroneService dronesService;

    public TickHandler(DroneService dronesService) {
        this.dronesService = dronesService;
    }

    @Scheduled(fixedRate = 3000)
    public void reportCurrentTime() {
        dronesService.chargeDrones();
        dronesService.loadDrones();
        dronesService.moveDrones();
        dronesService.unloadDrones();
    }
}
