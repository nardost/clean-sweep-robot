package com.team4.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class LocationFactoryTest {

    private Location first;
    private Location second;

    public LocationFactoryTest(Location first, Location second) {
        this.first = first;
        this.second = second;
    }

    @Parameters(name = "{index}")
    public static Collection<Object[]> data() throws RobotException {
        return (ArrayList<Object[]>) Stream.of(new Object[][] {
                { LocationFactory.createLocation(0, 0), LocationFactory.createLocation(0, 0) },
                { LocationFactory.createLocation(1, 9), LocationFactory.createLocation(1, 9) }
        }).collect(Collectors.toList());
    }

    @Test
    public void location_factory_does_creates_only_one_location_object_per_unique_coordinates() {
        assertEquals(first, second);
    }
}
