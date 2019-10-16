package com.team4.sensor;

import com.team4.commons.Direction;
import static com.team4.commons.Direction.*;
import static org.junit.Assert.assertArrayEquals;

import com.team4.commons.Location;
import com.team4.commons.LocationFactory;
import org.junit.Before;
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
                { LocationFactory.createLocation(1, 0), new Direction[] {SOUTH, EAST, WEST} },
                { LocationFactory.createLocation(0, 1), new Direction[] {NORTH, SOUTH, EAST} }
        }).collect(Collectors.toList());
    }
    @Test
    public void getLocationInfo_returns_correct_direction_of_open_passages_for_a_given_location() {
        FloorDao dao = SensorSimulator.getInstance().getLocationInfo(location);
        assertArrayEquals(passages, dao.openPassages);
    }
}
