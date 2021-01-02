package de.bookking3000.coordosy.Data;

import java.util.UUID;

public class PlayerPosition {

    double x;
    double y;
    double z;

    UUID playerUUID;

    public PlayerPosition(double x, double y, double z, UUID playerUUID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.playerUUID = playerUUID;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public String toString() {
        return "{\"x\":"
                + getX()
                + "," +
                "\"y\":" + "\"" + getY() + "\""
                + "," +
                "\"z\":" + "\"" + getZ() + "\""
                + "," +
                "\"uuid\":" + "\"" + getPlayerUUID() + "\""
                + "}";
    }
}
