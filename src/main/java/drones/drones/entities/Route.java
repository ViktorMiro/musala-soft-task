package drones.drones.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "route")
public class Route {
    @Id
    @Column(name = "drone_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "drone_id")
    @JsonIgnore
    @MapsId
    private Drone drone;
    private int totalDistance;
    private int passedDistance;

    public Route() {
    }

    public int getId() {
        return id;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getPassedDistance() {
        return passedDistance;
    }

    public void setPassedDistance(int passedDistance) {
        this.passedDistance = passedDistance;
    }
}
