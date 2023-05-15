package drones.drones.repositories;

import drones.drones.entities.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Integer> {
    List<Drone> findByStateInAndBatteryLessThan(Collection<Drone.State> states, int battery);
    List<Drone> findByStateIn(Collection<Drone.State> states);
    List<Drone> findByState(Drone.State state);
}
