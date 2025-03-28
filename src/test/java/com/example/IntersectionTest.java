package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.TrafficLight.LIGHT;

class IntersectionTest {
    private Intersection intersection;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        intersection = new Intersection();
        testVehicle = new Vehicle("car1", "north", "south");
    }

    @Test
    void testConstructorInitialization() {
        assertNotNull(intersection.getRoads());
        assertEquals(4, intersection.getRoads().size());
        assertTrue(intersection.getRoads().containsKey("north"));
        assertEquals(LIGHT.GREEN, intersection.getRoads().get("north").getTrafficLight().getLightStatus());
        assertEquals(0, intersection.getLeftVehicles().size());
    }

    @Test
    void testAddVehicle() {
        intersection.addVehicle(testVehicle);
        
        assertEquals(1, intersection.getRoads().get("north").getVehiclesOnRoad().size());
        assertEquals(testVehicle, intersection.getRoads().get("north").getVehiclesOnRoad().get(0));
    }

    @Test
    void testStepWithGreenLight() {
        // Add vehicle to north road (which has green light)
        intersection.addVehicle(testVehicle);
        
        intersection.step();
        
        // Vehicle should be processed and added to leftVehicles
        assertEquals(1, intersection.getLeftVehicles().size());
        assertEquals("car1", intersection.getLeftVehicles().get(0));
        assertEquals(0, intersection.getRoads().get("north").getVehiclesOnRoad().size());
    }

    @Test
    void testStepWithRedLight() {
        // Add vehicle to east road (which has red light)
        Vehicle eastVehicle = new Vehicle("car2", "east", "west");
        intersection.addVehicle(eastVehicle);
        
        intersection.step();
        
        // Vehicle should remain on the road
        assertEquals(0, intersection.getLeftVehicles().size());
        assertEquals(1, intersection.getRoads().get("east").getVehiclesOnRoad().size());
    }

    @Test
    void testGetBusiestRoadWithVehicles() {
        // Add vehicles to north and east roads
        intersection.addVehicle(new Vehicle("car1", "north", "south"));
        intersection.addVehicle(new Vehicle("car2", "north", "south"));
        intersection.addVehicle(new Vehicle("car3", "east", "west"));
        
        // North should be busiest (2 vehicles vs 1)
        assertEquals("north", intersection.getBusiestRoad());
    }

    @Test
    void testGetBusiestRoadNoVehicles() {
        // With no vehicles, it should cycle through roads
        String first = intersection.getBusiestRoad();
        String second = intersection.getBusiestRoad();
        
        assertNotEquals(first, second);
    }

   

    @Test
    void testCalculateGreenDuration() {
        // Add some vehicles
        intersection.addVehicle(new Vehicle("car1", "north", "south"));
        intersection.addVehicle(new Vehicle("car2", "north", "south"));
        intersection.addVehicle(new Vehicle("car3", "east", "west"));
        
        // North has 2/3 of vehicles (66%)
        int duration = intersection.calculateGreenDuration("north");
        assertTrue(duration > intersection.getMIN_GREEN_DURATION() && duration < intersection.getMAX_GREEN_DURATION());
        
        // Test with no vehicles
        Intersection emptyIntersection = new Intersection();
        assertEquals(intersection.getMIN_GREEN_DURATION(), emptyIntersection.calculateGreenDuration("north"));
    }

    @Test
    void testGetTotalVehicles() {
        assertEquals(0, intersection.getTotalVehicles());
        
        intersection.addVehicle(testVehicle);
        assertEquals(1, intersection.getTotalVehicles());
        
        intersection.addVehicle(new Vehicle("car2", "east", "west"));
        assertEquals(2, intersection.getTotalVehicles());
    }

    @Test
    void testGetTrafficStatus() {
        String status = intersection.getTrafficStatus();
        assertTrue(status.contains("north GREEN"));
        assertTrue(status.contains("east RED"));
        assertTrue(status.contains(String.valueOf(intersection.getCurrentGreenDuration())));
    }

    // @Test
    // void testProcessVehicles() {
    //     Road mockRoad = Mockito.mock(Road.class);
    //     when(mockRoad.getTrafficLight()).thenReturn(new TrafficLight());
    //     when(mockRoad.getVehiclesOnRoad()).thenReturn(List.of(testVehicle));
        
    //     intersection.processVehicles(mockRoad);
        
    //     // Shouldn't process because light is red by default in mock
    //     assertEquals(0, intersection.getLeftVehicles().size());
        
    //     // Set light to green and try again
    //     mockRoad.getTrafficLight().setLightStatus(LIGHT.GREEN);
    //     intersection.processVehicles(mockRoad);
        
    //     assertEquals(1, intersection.getLeftVehicles().size());
    // }
}