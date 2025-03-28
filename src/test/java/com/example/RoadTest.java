package com.example;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoadTest {
    private Road road;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        road = new Road("north", 1);
        testVehicle = new Vehicle("car1", "north", "south");
    }

    @Test
    void testConstructor() {
        assertEquals("north", road.getDirection());
        assertEquals(1, road.getPriority());
        assertEquals(0, road.getVehiclesOnRoad().size());
        assertEquals(0, road.getRedDuration());
        assertNotNull(road.getTrafficLight());
    }

    @Test
    void testAddVehicleOnRoad() {
        road.addVehicleOnRoad(testVehicle);
        
        assertEquals(1, road.getVehiclesOnRoad().size());
        assertEquals(testVehicle, road.getVehiclesOnRoad().get(0));
    }

    @Test
    void testDeleteVehicleFromRoad() {
        // Test with empty road
        assertDoesNotThrow(() -> road.deleteVehicleFromRoad());
        
        // Test with vehicles
        road.addVehicleOnRoad(testVehicle);
        road.deleteVehicleFromRoad();
        
        assertEquals(0, road.getVehiclesOnRoad().size());
        assertEquals(1, road.getPriority()); // Should decrement
    }

    @Test
    void testIncrementRedDuration() {
        road.incrementRedDuration(road.getRedDuration());
        assertEquals(1, road.getRedDuration());
        
        road.incrementRedDuration(road.getRedDuration());
        assertEquals(2, road.getRedDuration());
    }

    @Test
    void testSetRedDuration() {
        road.setRedDuration(5);
        assertEquals(5, road.getRedDuration());
        
        road.setRedDuration(0);
        assertEquals(0, road.getRedDuration());
    }

    @Test
    void testTrafficLightManagement() {
        TrafficLight newLight = new TrafficLight();
        newLight.setLightStatus(TrafficLight.LIGHT.GREEN);
        
        road.setTrafficLight(newLight);
        assertEquals(newLight, road.getTrafficLight());
        assertEquals(TrafficLight.LIGHT.GREEN, road.getTrafficLight().getLightStatus());
    }

    @Test
    void testToString() {
        // Test empty road
        assertEquals("", road.toString());
        
        // Test with vehicles
        road.addVehicleOnRoad(testVehicle);
        String expected = testVehicle.toString() + "\n";
        assertEquals(expected, road.toString());
        
        // Test with multiple vehicles
        Vehicle testVehicle2 = new Vehicle("car2", "north", "east");
        road.addVehicleOnRoad(testVehicle2);
        expected += testVehicle2.toString() + "\n";
        assertEquals(expected, road.toString());
    }

    @Test
    void testEdgeCases() {
        
        // Test multiple deletions on empty road
        assertDoesNotThrow(() -> {
            road.deleteVehicleFromRoad();
            road.deleteVehicleFromRoad();
        });
        
        // Test setting negative red duration
        road.setRedDuration(-1);
        assertEquals(-1, road.getRedDuration());
    }
}
