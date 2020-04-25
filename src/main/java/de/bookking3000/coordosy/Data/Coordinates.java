package de.bookking3000.coordosy.Data;

public class Coordinates {

    double xCoord;
    double yCoord;
    double zCoord;

    public Coordinates(double xCoord, double yCoord, double zCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }

    public double getXCoord() {
        return xCoord;
    }

    public double getYCoord() {
        return yCoord;
    }

    public double getZCoord() {
        return zCoord;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + xCoord +
                ",y=" + yCoord +
                ",z=" + zCoord +
                '}';
    }
}
