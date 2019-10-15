package com.team4.sensor;

public class SensorSimulator {

    private static Floor floor;

    private static SensorSimulator simulator = null;
    private SensorSimulator() {
        this.floor = Floor.getInstance();
    }

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

    static Floor getFloor() {
        return floor;
    }

    public static int getNumberOfTiles() {
        return getFloor().getTiles().size();
    }

    public static int [] getFloorDimension() {
        return new int[] { getFloor().WIDTH, getFloor().LENGTH };
    }
}