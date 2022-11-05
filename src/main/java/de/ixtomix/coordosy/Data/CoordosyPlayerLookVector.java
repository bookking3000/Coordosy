package de.ixtomix.coordosy.Data;

import com.google.gson.GsonBuilder;
import com.mojang.math.Vector3d;
import net.minecraft.world.phys.Vec3;

public class CoordosyPlayerLookVector extends Vector3d {

    public CoordosyPlayerLookVector(Vec3 vec) {
        super(vec.x, vec.y, vec.z);
    }

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        return gson.create().toJson(this);
    }
}
