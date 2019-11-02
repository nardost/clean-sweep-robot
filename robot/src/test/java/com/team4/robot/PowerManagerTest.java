package com.team4.robot;

import com.team4.commons.ConfigManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PowerManagerTest {
    @Test
    public void power_manager_charges_robot_to_100_percent() {
        int maxBatteryLevel = Integer.parseInt(ConfigManager.getConfiguration("maxBatteryLevel"));
        RobotCleanSweep.getInstance().getPowerManager().recharge();
        assertEquals(maxBatteryLevel, RobotCleanSweep.getInstance().getPowerManager().getBatteryLevel(), .5);
    }
}
