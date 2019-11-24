package com.team4.robot;

import com.team4.commons.ConfigManager;
import com.team4.sensor.SensorSimulator;
import org.junit.Before;
import org.junit.Test;

import static com.team4.commons.State.WORKING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class MaxDirtLevelTest {

    private DirtManager dirtManager;

    @Before
    public void init() {
        this.dirtManager = new DirtManager();
    }

    @Test
    public void dirt_manager_makes_sure_tank_is_emptied_and_robot_gets_back_to_WORKING_state_after_tank_fills_up() {
        RobotCleanSweep robot = RobotCleanSweep.getInstance();
        robot.setState(WORKING);
        int dirtCapacity = Integer.parseInt(ConfigManager.getConfiguration("dirtCapacity"));
        for(int i = 0; i < dirtCapacity; i++) {
            if(!SensorSimulator.getInstance().getLocationInfo(robot.getLocation()).isClean) {
                this.dirtManager.clean(3.0);
            }
        }
        assertThat(WORKING, is(equalTo(robot.getState())));
    }
}
