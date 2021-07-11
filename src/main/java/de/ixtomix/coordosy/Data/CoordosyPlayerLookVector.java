package de.ixtomix.coordosy.Data;

import com.google.gson.GsonBuilder;
import net.minecraft.util.math.vector.Vector3d;

public class CoordosyPlayerLookVector extends Vector3d {

    public CoordosyPlayerLookVector(Vector3d vec) {
        super(vec.x, vec.y, vec.z);
    }

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        return gson.create().toJson(this);
    }
}
