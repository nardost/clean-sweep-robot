package com.team4.robot;

import com.team4.commons.State;
import com.team4.sensor.SensorSimulator;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NoTilesLeftUnvisitedTest {

    @Test
    public void robot_visits_every_tile_on_the_floor() {
        RobotCleanSweep.getInstance().setState(State.STANDBY);
        RobotCleanSweep.getInstance().dryRun();
        assertTrue(SensorSimulator.getInstance().isFloorDone());
    }
}
