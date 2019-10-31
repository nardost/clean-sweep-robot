package com.team4.sensor;

import com.team4.commons.Direction;
import static com.team4.commons.Direction.*;
import static org.junit.Assert.assertArrayEquals;

import com.team4.commons.Location;
import com.team4.commons.LocationFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class GetLocationInfoCorrectDirectionsOfPassageTest {

    private Location location;
    private Direction[] passages;

    public GetLocationInfoCorrectDirectionsOfPassageTest(Location location, Direction[] passages) {
        this.location = location;
        this.passages = passages;
    }

    @Before
    public void init() {
    }

    @Parameters(name = "{index}")
    public static Collection<Object[]> data() {
        return (ArrayList<Object[]>) Stream.of(new Object[][] {
                { LocationFactory.createLocation(0, 0), new Direction[] {SOUTH, EAST} },
                { LocationFactory.createLocation(0, 9), new Direction[] {NORTH, EAST} },
                { LocationFactory.createLocation(9, 0), new Direction[] {SOUTH, WEST} },
                { LocationFactory.createLocation(9, 9), new Direction[] {NORTH, WEST} },
                { LocationFactory.createLocation(5, 0), new Direction[] {SOUTH, EAST, WEST} },
                { LocationFactory.createLocation(5, 9), new Direction[] {NORTH, EAST, WEST} },
                { LocationFactory.createLocation(0, 5), new Direction[] {NORTH, SOUTH, EAST} },
                { LocationFactory.createLocation(9, 5), new Direction[] {NORTH, SOUTH, WEST} }
                //{ LocationFactory.createLocation(0, 1), new Direction[] {NORTH, SOUTH, EAST} },
                //{ LocationFactory.createLocation(0, 5), new Direction[] {SOUTH, EAST} },
                //{ LocationFactory.createLocation(0, 4), new Direction[] {EAST} },
                //{ LocationFactory.createLocation(1, 0), new Direction[] {SOUTH, EAST, WEST} },
                //{ LocationFactory.createLocation(2, 0), new Direction[] {SOUTH, EAST, WEST} },
                //{ LocationFactory.createLocation(1, 1), new Direction[] {NORTH, SOUTH, EAST, WEST} },
                //{ LocationFactory.createLocation(5, 3), new Direction[] {NORTH, SOUTH, WEST} },
                //{ LocationFactory.createLocation(6, 2), new Direction[] {NORTH, SOUTH, EAST, WEST} },
                //{ LocationFactory.createLocation(4, 7), new Direction[] {NORTH, SOUTH, EAST} },
                //{ LocationFactory.createLocation(8, 8), new Direction[] {NORTH, SOUTH, EAST, WEST} },
                //{ LocationFactory.createLocation(1, 5), new Direction[] {NORTH, SOUTH, EAST, WEST} },
                //{ LocationFactory.createLocation(7, 6), new Direction[] {NORTH, SOUTH, EAST, WEST} },
                //{ LocationFactory.createLocation(3, 3), new Direction[] {EAST, WEST} },
                //{ LocationFactory.createLocation(2, 7), new Direction[] {NORTH, SOUTH, EAST, WEST} }
        }).collect(Collectors.toList());
    }
    @Test
    @Ignore
    public void getLocationInfo_returns_correct_direction_of_open_passages_for_a_given_location() {
        FloorDao dao = SensorSimulator.getInstance().getLocationInfo(location);
        assertArrayEquals(passages, dao.openPassages);
    }
}
