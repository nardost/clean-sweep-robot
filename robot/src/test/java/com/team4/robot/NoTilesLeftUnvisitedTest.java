package com.team4.robot;

import com.team4.commons.State;
import com.team4.sensor.SensorSimulator;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class NoTilesLeftUnvisitedTest {

    @Test
    public void robot_visits_every_tile_on_the_floor() {
        RobotCleanSweep.getInstance().setState(State.STANDBY);
        RobotCleanSweep.getInstance().dryRun();
        assertThat(true, is(equalTo(SensorSimulator.getInstance().getDonePercentage() == 100.0)));
    }
}
