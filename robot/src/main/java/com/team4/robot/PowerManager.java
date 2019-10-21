package com.team4.robot;

interface PowerManager {
    void recharge();
    void updateBatteryLevel(int units);
    int getBatteryLevel();
}