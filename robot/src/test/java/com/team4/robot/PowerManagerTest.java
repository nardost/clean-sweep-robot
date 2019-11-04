package com.team4.robot;

import com.team4.commons.ConfigManager;
import com.team4.commons.RobotException;
import com.team4.commons.State;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PowerManagerTest {
    @Before
    public void init() {
        RobotCleanSweep.getInstance().setState(State.WORKING);
        RobotCleanSweep.getInstance().getPowerManager().recharge();
    }
    @Test
    public void power_manager_charges_robot_to_100_percent() {
        int maxBatteryLevel = Integer.parseInt(ConfigManager.getConfiguration("maxBatteryLevel"));
        RobotCleanSweep.getInstance().getPowerManager().recharge();
        assertEquals(maxBatteryLevel, RobotCleanSweep.getInstance().getPowerManager().getBatteryLevel(), .5);
    }
    @Test(expected = RobotException.class)
    public void power_manager_throws_exception_on_invalid_power_usage_level() {
        RobotCleanSweep.getInstance().getPowerManager().updateBatteryLevel(4);
    }
    @Test
    public void robot_goes_to_LOW_BATTERY_state_when_battery_level_is_below_a_certain_amount() {
        while(RobotCleanSweep.getInstance().getPowerManager().getBatteryLevel() > 1) {
            RobotCleanSweep.getInstance().getPowerManager().updateBatteryLevel(1);
        }
        RobotCleanSweep.getInstance().getPowerManager().updateBatteryLevel(1);
        assertEquals(State.LOW_BATTERY, RobotCleanSweep.getInstance().getState());
    }
    @After
    public void resetRobt() {
        RobotCleanSweep.getInstance().setState(State.WORKING);
        RobotCleanSweep.getInstance().getPowerManager().recharge();
    }
}
