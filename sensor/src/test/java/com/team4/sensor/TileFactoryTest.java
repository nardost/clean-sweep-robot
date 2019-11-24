package com.team4.sensor;

import com.team4.commons.LocationFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(Parameterized.class)
public class TileFactoryTest {

    private Tile first;
    private Tile second;

    public TileFactoryTest(Tile first, Tile second) {
        this.first = first;
        this.second = second;
    }

    @Parameters(name = "{index}")
    public static Collection<Object[]> data() {
        return Stream.of(new Object[][] {
                { TileFactory.createTile(LocationFactory.createLocation(0, 0)), TileFactory.createTile(LocationFactory.createLocation(0, 0)) },
                { TileFactory.createTile(LocationFactory.createLocation(10, 0)), TileFactory.createTile(LocationFactory.createLocation(10, 0)) },
                { TileFactory.createTile(LocationFactory.createLocation(0, 10)), TileFactory.createTile(LocationFactory.createLocation(0, 10)) },
                { TileFactory.createTile(LocationFactory.createLocation(12, 21)), TileFactory.createTile(LocationFactory.createLocation(12, 21)) },
        }).collect(Collectors.toList());
    }

    @Test
    public void tile_factory_creates_only_one_tile_object_per_unique_location() {
        assertThat(first, is(equalTo(second)));
    }
}
