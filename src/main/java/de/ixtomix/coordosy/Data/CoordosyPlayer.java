package de.ixtomix.coordosy.Data;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.ixtomix.coordosy.Annotations.HiddenInJson;
import de.ixtomix.coordosy.Excludors.HiddenInJsonExclusionStrategy;
import net.minecraft.util.math.vector.Vector3d;

public class CoordosyPlayer {

    @HiddenInJson
    public String uuid;

    @HiddenInJson
    public String MpGroupId;

    @HiddenInJson
    public CoordosyPlayerLookVector lookVector;

    @Expose
    public double x;

    @Expose
    public double y;

    @Expose
    public double z;

    @Expose
    public String entityListString;

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
        return "" + uuid + "{" +
                "lookVector=" + lookVector +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.excludeFieldsWithoutExposeAnnotation();
        return gson.setExclusionStrategies(new HiddenInJsonExclusionStrategy()).create().toJson(this);
    }
}

