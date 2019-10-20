package com.team4.sensor;

import com.team4.commons.Location;

public interface Sensor {
    FloorDao getLocationInfo(Location location);
    void setTileDone(Location location);
}
