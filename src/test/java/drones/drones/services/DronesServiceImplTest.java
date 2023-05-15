package drones.drones.services;

import drones.drones.entities.Drone;
import drones.drones.repositories.DroneRepository;
import drones.drones.repositories.MedicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DronesServiceImplTest {

    @Mock
    private DroneRepository droneRepository;
    @Mock
    private MedicationRepository medicationRepository;
    @Mock
    private EventLogService eventLogService;

    @InjectMocks
    private DronesServiceImpl dronesService;

    @Test
    void getById_returnDrone() {
        int id = 111;
        Drone droneMock = mock(Drone.class);

        Mockito.doReturn(Optional.of(droneMock)).when(droneRepository).findById(id);

        Optional<Drone> drone = dronesService.getById(id);

        assertEquals(droneMock, drone.get());
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void getById_returnEmpty() {
        int id = 111;

        Mockito.doReturn(Optional.empty()).when(droneRepository).findById(id);

        Optional<Drone> drone = dronesService.getById(id);

        assertTrue(drone.isEmpty());
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void delete() {
        int id = 111;

        Mockito.doNothing().when(droneRepository).deleteById(id);
        dronesService.delete(id);

        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void getAll() {
        List<Drone> mock = new LinkedList<>();
        Mockito.doReturn(mock).when(droneRepository).findAll();
        List<Drone> result = dronesService.getAll();

        assertEquals(mock, result);
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void create() {
        Drone droneMock = mock(Drone.class);

        Mockito.doReturn(droneMock).when(droneRepository).save(droneMock);

        Drone drone = dronesService.create(droneMock);

        assertEquals(droneMock, drone);
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void update_exception() {
        int id = 111;
        Drone droneMock = mock(Drone.class);

        Mockito.doReturn(Optional.empty()).when(droneRepository).findById(id);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> dronesService.update(id, droneMock));

        String expectedMessage = "Drone not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update_successful() {
        int id = 111;
        int battery = 100;
        int weightLimit = 500;
        String serialNumber = "testserial";
        Drone.Model model = Drone.Model.LIGHTWEIGHT;
        Drone.State state = Drone.State.IDLE;
        Drone droneMock = mock(Drone.class);

        Mockito.doReturn(battery).when(droneMock).getBattery();
        Mockito.doReturn(model).when(droneMock).getModel();
        Mockito.doReturn(state).when(droneMock).getState();
        Mockito.doReturn(weightLimit).when(droneMock).getWeightLimit();
        Mockito.doReturn(serialNumber).when(droneMock).getSerialNumber();

        Mockito.doReturn(Optional.of(droneMock)).when(droneRepository).findById(id);
        Mockito.doReturn(droneMock).when(droneRepository).save(droneMock);

        Drone drone = dronesService.update(id, droneMock);

        assertEquals(droneMock, drone);
        verify(droneMock).setBattery(battery);
        verify(droneMock).setModel(model);
        verify(droneMock).setState(state);
        verify(droneMock).setWeightLimit(weightLimit);
        verify(droneMock).setSerialNumber(serialNumber);
        verifyNoMoreInteractions(droneRepository, droneMock);
    }

    @Test
    void chargeDrones_noDrones() {
        List<Drone> emptyList = new LinkedList<>();
        DronesServiceImpl dronesServiceMock = Mockito.spy(dronesService);

        Mockito.doReturn(emptyList).when(droneRepository).findByStateInAndBatteryLessThan(anyCollection(), anyInt());

        dronesServiceMock.chargeDrones();

        verify(dronesServiceMock).chargeDrones();
        verifyNoMoreInteractions(droneRepository, dronesServiceMock);
    }

    @Test
    void chargeDrones() {
        List<Drone> drones = new LinkedList<>();
        Drone droneMock = mock(Drone.class);
        drones.add(droneMock);

        DronesServiceImpl dronesServiceMock = Mockito.spy(dronesService);

        Mockito.doReturn(drones).when(droneRepository).findByStateInAndBatteryLessThan(anyCollection(), anyInt());
        Mockito.doNothing().when(dronesServiceMock).chargeDrone(any(Drone.class), any(StringBuilder.class));
        Mockito.doReturn(null).when(eventLogService).logMessageForType(anyString(), anyString());

        dronesServiceMock.chargeDrones();

        verify(dronesServiceMock).chargeDrones();
        verifyNoMoreInteractions(droneRepository, dronesServiceMock, eventLogService);
    }

    @Test
    void chargeDrone_before100() {
        Drone droneMock = mock(Drone.class);
        StringBuilder log = new StringBuilder();
        int droneBatteryBefore = 50;
        int droneBatteryExpected = 60;
        String expectedLog = " | Drone with id - 0 charged 50 -> 60 | ";

        Mockito.doReturn(droneBatteryBefore).when(droneMock).getBattery();
        Mockito.doReturn(droneMock).when(droneRepository).save(droneMock);

        dronesService.chargeDrone(droneMock, log);

        String resultLog = log.toString();
        assertEquals(expectedLog, resultLog);
        verify(droneMock).setBattery(droneBatteryExpected);
        verifyNoMoreInteractions(droneRepository, eventLogService);
    }

    @Test
    void chargeDrone_closeTo100() {
        Drone droneMock = mock(Drone.class);
        StringBuilder log = new StringBuilder();
        int droneBatteryBefore = 99;
        int droneBatteryExpected = 100;
        String expectedLog = " | Drone with id - 0 charged 99 -> 100 | ";

        Mockito.doReturn(droneBatteryBefore).when(droneMock).getBattery();
        Mockito.doReturn(droneMock).when(droneRepository).save(droneMock);

        dronesService.chargeDrone(droneMock, log);

        String resultLog = log.toString();
        assertEquals(expectedLog, resultLog);
        verify(droneMock).setBattery(droneBatteryExpected);
        verifyNoMoreInteractions(droneRepository, eventLogService);
    }

}