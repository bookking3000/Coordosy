package de.ixtomix.coordosy.Data;

import net.minecraft.util.math.vector.Vector3d;

public class CoordosyPlayer {
    public Vector3d lookVector;
    public String uuid;
    public double x;
    public double y;
    public double z;

    public String entityListString;
    public String MpGroupId;

    public CoordosyPlayer(String MpGroupId, String uuid) {
        this.MpGroupId = MpGroupId;
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        return (this.x == ((CoordosyPlayer) obj).x) &&
                (this.y == ((CoordosyPlayer) obj).y) &&
                (this.z == ((CoordosyPlayer) obj).z) &&
                (this.lookVector.equals(((CoordosyPlayer) obj).lookVector)) &&
                (this.uuid.equals(((CoordosyPlayer) obj).uuid));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "CoordosyPlayer{" +
                "lookVector=" + lookVector +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

