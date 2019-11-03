package com.team4.robot;

import com.team4.commons.ConfigManager;
import com.team4.commons.RobotException;
import org.junit.Before;
import org.junit.Test;

import static com.team4.commons.State.WORKING;
import static org.junit.Assert.assertEquals;

public class MaxDirtLevelTest {

    private DirtManager dirtManager;

    @Before
    public void init() {
        this.dirtManager = new DirtManager();
    }

    @Test(expected = RobotException.class)
    public void dirt_manager_throws_exception_on_dirt_level_exceeding_dirtCapacity() {
        int dirtCapacity = Integer.parseInt(ConfigManager.getConfiguration("dirtCapacity"));
        for(int i = 0; i <= dirtCapacity / 2; i++) {
            this.dirtManager.clean(3.0);
        }
    }

    @Test
    public void dirt_manager_makes_sure_tank_is_emptied_and_robot_gets_back_to_WORKING_state_after_tank_fills_up() {
        RobotCleanSweep robot = RobotCleanSweep.getInstance();
        int dirtCapacity = Integer.parseInt(ConfigManager.getConfiguration("dirtCapacity"));
        for(int i = 0; i <= dirtCapacity / 2; i++) {
            this.dirtManager.clean(2.0);
        }
        assertEquals(WORKING, robot.getState());
    }
}
