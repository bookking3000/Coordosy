package de.bookking3000.coordosy.Data;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class JsonData {
    public Vector3d lookVector;
    public String uuid;
    public double x;
    public double y;
    public double z;

    public String listString;
    transient public List<Entity> entityList;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        return (this.x == ((JsonData) obj).x) &&
                (this.y == ((JsonData) obj).y) &&
                (this.z == ((JsonData) obj).z) &&
                (this.lookVector.equals(((JsonData) obj).lookVector)) &&
                (this.uuid.equals(((JsonData) obj).uuid));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

