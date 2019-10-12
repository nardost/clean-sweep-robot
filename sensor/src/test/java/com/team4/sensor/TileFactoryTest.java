package com.team4.sensor;

import com.team4.commons.Location;
import com.team4.commons.LocationFactory;
import com.team4.commons.RobotException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TileFactoryTest {

    private Tile first;
    private Tile second;

    public TileFactoryTest(Tile first, Tile second) {
        this.first = first;
        this.second = second;
    }

    @Parameters(name = "{index}")
    public static Collection<Object[]> data() throws RobotException {
        Location [] locations = new Location[] {
                LocationFactory.createLocation(0, 0),
                LocationFactory.createLocation(10, 0),
                LocationFactory.createLocation(0, 10),
                LocationFactory.createLocation(12, 21)
        };
        return (ArrayList<Object[]>) Stream.of(new Object[][] {
                { TileFactory.createTile(locations[0]), TileFactory.createTile(locations[0]) },
                { TileFactory.createTile(locations[1]), TileFactory.createTile(locations[1]) },
                { TileFactory.createTile(locations[2]), TileFactory.createTile(locations[2]) },
                { TileFactory.createTile(locations[3]), TileFactory.createTile(locations[3]) },
        }).collect(Collectors.toList());
    }

    @Test
    public void tile_factory_creates_only_one_tile_object_per_unique_location() {
        assertEquals(first, second);
    }
}
