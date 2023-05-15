package drones.drones.services;

import drones.drones.entities.EventLog;

import java.util.List;

public interface EventLogService {
    List<EventLog> getAll();
    EventLog create(EventLog eventLog);
    EventLog logMessageForType(String message, String type);
}
