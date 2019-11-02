package com.team4.robot;

interface PowerManager {
    void recharge();
    void updateBatteryLevel(double units);
    double getBatteryLevel();
}