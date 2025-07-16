package tech.ada.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Vehicle {

    private static final Map<VehicleStatus, Set<VehicleStatus>> VEHICLE_STATUS = new HashMap<>() {
    };

    static {
        VEHICLE_STATUS.put(VehicleStatus.AVAILABLE, Set.of(VehicleStatus.RENTED, VehicleStatus.UNDER_MAINTENANCE));
        VEHICLE_STATUS.put(VehicleStatus.RENTED, Set.of(VehicleStatus.AVAILABLE, VehicleStatus.UNDER_MAINTENANCE));
        VEHICLE_STATUS.put(VehicleStatus.UNDER_MAINTENANCE, Set.of(VehicleStatus.AVAILABLE));
    }

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(1);
    private Long id;
    private String model;
    private VehicleStatus status;
    private int year;
    private String engine;

    public Vehicle(String model, int year, String engine) {
        this.id = ATOMIC_LONG.getAndIncrement();
        this.status = VehicleStatus.AVAILABLE;
        this.model = model;
        this.year = year;
        this.engine = engine;
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public int getYear() {
        return year;
    }

    public String getEngine() {
        return engine;
    }

    public boolean isRented() {
        return this.getStatus().equals(VehicleStatus.RENTED);
    }



    public void setStatus(VehicleStatus incomingStatus) {
        Set<VehicleStatus> possibleStatus = VEHICLE_STATUS.get(this.status);

        if (incomingStatus.equals(this.status)) {
            return;
        }

        if (possibleStatus.contains(incomingStatus)) {
            this.status = incomingStatus;
        } else {
            throw new RuntimeException("Validation error, possible status are: " + possibleStatus);
        }

//        if (this.status.equals(VehicleStatus.AVAILABLE) && incomingStatus == VehicleStatus.RENTED) {
//            this.status = VehicleStatus.RENTED;
//        } else if (incomingStatus == VehicleStatus.AVAILABLE && (this.status.equals(VehicleStatus.UNDER_MAINTENANCE) || this.status.equals(VehicleStatus.RENTED))) {
//            this.status = VehicleStatus.AVAILABLE;
//        } else if (incomingStatus == VehicleStatus.UNDER_MAINTENANCE) {
//            this.status = incomingStatus;
//        }
    }
}
