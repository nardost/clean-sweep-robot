package com.team4.robot;

import com.team4.commons.ConfigManager;
import com.team4.commons.LogManager;
import com.team4.commons.Utilities;

class PowerUnit implements PowerManager {

    private int batteryLevel;

    PowerUnit() {
        int maxBatteryLevel = Integer.parseInt(ConfigManager.getConfiguration("maxBatteryLevel"));
        setBatteryLevel(maxBatteryLevel);
    }

    @Override
    public void recharge() {
        long timeToCharge = Long.parseLong(ConfigManager.getConfiguration("timeToCharge"));
        System.out.println();
        LogManager.print("...CHARGING...", RobotCleanSweep.getInstance().getZeroTime());
        System.out.println();
        Utilities.doTimeDelay(timeToCharge);
        int maxBatteryLevel = Integer.parseInt(ConfigManager.getConfiguration("maxBatteryLevel"));
        setBatteryLevel(maxBatteryLevel);
    }

    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public void updateBatteryLevel(int units) {
        setBatteryLevel(getBatteryLevel() - units);
    }

    private void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
}
