package com.team4.sensor;

import com.team4.commons.Location;
import com.team4.commons.LocationFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class GetLocationInfoNeighborsWithinRadiusTest {
    private Location location;
    private Location[] neighbors;

    public GetLocationInfoNeighborsWithinRadiusTest(Location location, Location[] neighbors) {
        this.location = location;
        this.neighbors = neighbors;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return (ArrayList<Object[]>) Stream.of(new Object[][] {
                {
                    LocationFactory.createLocation(0, 0),
                    new Location [] {
                            LocationFactory.createLocation(1, 0),
                            LocationFactory.createLocation(2, 0),
                            LocationFactory.createLocation(0, 1),
                            LocationFactory.createLocation(1, 1),
                            LocationFactory.createLocation(0, 2),
                    }
                },
                {
                        LocationFactory.createLocation(2, 2),
                        new Location [] {
                                LocationFactory.createLocation(2, 0),
                                LocationFactory.createLocation(1, 1),
                                LocationFactory.createLocation(2, 1),
                                LocationFactory.createLocation(3, 1),
                                LocationFactory.createLocation(0, 2),
                                LocationFactory.createLocation(1, 2),
                                LocationFactory.createLocation(3, 2),
                                LocationFactory.createLocation(4, 2),
                                LocationFactory.createLocation(1, 3),
                                LocationFactory.createLocation(2, 3),
                                LocationFactory.createLocation(3, 3),
                                LocationFactory.createLocation(2, 4),
                        }
                }

        }).collect(Collectors.toList());
    }

    @Test @Ignore
    public void getLocationInfo_returns_correct_neighbors_within_a_given_radius() {
        FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(location);
        assertArrayEquals(neighbors, floorDao.chargingStations);
    }
}
