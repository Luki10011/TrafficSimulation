package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VehicleTest {
    private Vehicle vehicle;
    private final String testId = "car123";
    private final String testStart = "north";
    private final String testEnd = "south";

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle(testId, testStart, testEnd);
    }

    @Test
    void constructor_InitializesFieldsCorrectly() {
        // Constructor should initialize fields correctly
        assertEquals(testId, vehicle.getVehicleId());
        assertEquals(testStart, vehicle.getStartRoad());
        assertEquals(testEnd, vehicle.getEndRoad());
        assertEquals(testStart, vehicle.getCurrentRoad());
    }

    @Test
    void constructor_ThrowsExceptionWhenStartAndEndSame() {
        // Constructor should throw exception when start and end roads are same
        assertThrows(AssertionError.class, () -> 
            new Vehicle("car2", "east", "east"),
            "Should throw when start and end roads are identical"
        );
    }

    @Test
    @DisplayName("getVehicleId should return correct value")
    void getVehicleId_ReturnsCorrectValue() {
        assertEquals(testId, vehicle.getVehicleId());
    }

    @Test
    @DisplayName("getStartRoad should return correct value")
    void getStartRoad_ReturnsCorrectValue() {
        assertEquals(testStart, vehicle.getStartRoad());
    }

    @Test
    @DisplayName("getEndRoad should return correct value")
    void getEndRoad_ReturnsCorrectValue() {
        assertEquals(testEnd, vehicle.getEndRoad());
    }

    @Test
    @DisplayName("getCurrentRoad should return start road initially")
    void getCurrentRoad_ReturnsStartRoadInitially() {
        assertEquals(testStart, vehicle.getCurrentRoad());
    }

    @Test
    @DisplayName("toString should return proper string representation")
    void toString_ReturnsProperFormat() {
        String expected = String.format(
            "Id: %s\nStartRoad: %s\nEndRoad: %s\n", 
            testId, testStart, testEnd
        );
        assertEquals(expected, vehicle.toString());
    }

}