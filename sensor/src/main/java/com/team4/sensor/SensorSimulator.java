package com.team4.sensor;

public class SensorSimulator {

    private static SensorSimulator simulator = null;

    private SensorSimulator() {}

    /**
     * SensorSimulator is a singleton since there is only one simulator per robot.
     *
     * @return SensorSimulator
     */
    public static SensorSimulator getInstance() {
        if(simulator == null) {
            synchronized (SensorSimulator.class) {
                if(simulator == null) {
                    simulator = new SensorSimulator();
                }
            }
        }
        return simulator;
    }

    public static int [] getFloorDimension() {
        return new int[] { 10, 10};
    }
}