package com.team4.sensor;

import com.team4.commons.Direction;
import static com.team4.commons.Direction.*;
import static org.junit.Assert.assertArrayEquals;

import com.team4.commons.Location;
import com.team4.commons.LocationFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class GetLocationInfoTest {

    private Location location;
    private Direction[] passages;

    public GetLocationInfoTest(Location location, Direction[] passages) {
        this.location = location;
        this.passages = passages;
    }

    @Parameters(name = "{index}")
    public static Collection<Object[]> data() {
        return (ArrayList<Object[]>) Stream.of(new Object[][] {
                { LocationFactory.createLocation(0, 0), new Direction[] {SOUTH, EAST, null, null} },
                { LocationFactory.createLocation(1, 0), new Direction[] {SOUTH, EAST, WEST, null} },
                { LocationFactory.createLocation(0, 1), new Direction[] {NORTH, SOUTH, EAST, null} }
        }).collect(Collectors.toList());
    }
    @Test
    public void get_location_info_test_returns_correct_dao() {
        FloorDao dao = SensorSimulator.getInstance().getLocationInfo(location);
        assertArrayEquals(passages, dao.openPassages);
    }
}
