package drones.drones.controllers;

import drones.drones.entities.EventLog;
import drones.drones.services.EventLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/event-logs")
public class EventLogsController {
    private final EventLogService eventLogService;

    public EventLogsController(EventLogService eventLogService) {
        this.eventLogService = eventLogService;
    }

    @GetMapping("")
    public List<EventLog> getAll() {
        return eventLogService.getAll();
    }
}
