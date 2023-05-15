package drones.drones.services;

import drones.drones.entities.EventLog;
import drones.drones.entities.Medication;
import drones.drones.repositories.EventLogRepository;
import drones.drones.repositories.MedicationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class EventLogServiceImpl implements EventLogService {
    private final EventLogRepository eventLogRepository;

    public EventLogServiceImpl(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    public List<EventLog> getAll() {
        return eventLogRepository.findAll();
    }

    public EventLog create(EventLog eventLog) {
        return eventLogRepository.save(eventLog);
    }

    public EventLog logMessageForType(String message, String type) {
        EventLog newLog = new EventLog();
        newLog.setAction(message);
        newLog.setEventType(type);
        return eventLogRepository.save(newLog);
    }
}
