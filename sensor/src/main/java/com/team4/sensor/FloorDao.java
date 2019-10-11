package com.team4.sensor;

import com.team4.commons.Direction;
import com.team4.commons.FloorType;
import com.team4.commons.Location;

public class FloorDao {
    public FloorType floorType;
    public Direction [] openPassages; // a maximum of 4 open passages.
    public Location [] chargingStations; // a maximum of 12 nearby charging stations.
}
