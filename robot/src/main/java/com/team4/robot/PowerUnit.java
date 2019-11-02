package com.team4.robot;

import com.team4.commons.ConfigManager;
import com.team4.commons.LogManager;
import com.team4.commons.RobotException;
import com.team4.commons.Utilities;

import static com.team4.commons.State.LOW_BATTERY;

class PowerUnit implements PowerManager {

    private double batteryLevel;

    PowerUnit() {
        int maxBatteryLevel = Integer.parseInt(ConfigManager.getConfiguration("maxBatteryLevel"));
        setBatteryLevel(maxBatteryLevel);
    }

    @Override
    public void recharge() {
        int timeToCharge = Integer.parseInt(ConfigManager.getConfiguration("timeToCharge"));
        Utilities.doLoopedTimeDelay("...CHARGING...", timeToCharge, RobotCleanSweep.getInstance().getZeroTime());
        int maxBatteryLevel = Integer.parseInt(ConfigManager.getConfiguration("maxBatteryLevel"));
        setBatteryLevel(maxBatteryLevel);
    }

    @Override
    public double getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public void updateBatteryLevel(double units) {
        if(units < 0 || units > 3) {
            throw new RobotException("Invalid power usage level.");
        }
        setBatteryLevel(getBatteryLevel() - units);

        if(getBatteryLevel() <= 0) {
            RobotCleanSweep.getInstance().setState(LOW_BATTERY);
            LogManager.print("...GOING BACK TO CHARGING STATION...", RobotCleanSweep.getInstance().getZeroTime());
        }
    }

    private void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
}
