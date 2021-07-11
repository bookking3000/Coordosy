package de.ixtomix.coordosy.Excludors;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import de.ixtomix.coordosy.Annotations.HiddenInJson;

public class HiddenInJsonExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(HiddenInJson.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return aClass.getAnnotation(HiddenInJson.class) != null;
    }

}