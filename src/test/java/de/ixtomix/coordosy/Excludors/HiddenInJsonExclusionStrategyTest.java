package de.ixtomix.coordosy.Excludors;

import com.google.gson.Gson;
import de.ixtomix.coordosy.Data.CoordosyPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HiddenInJsonExclusionStrategyTest {

    private CoordosyPlayer coordosyPlayer;

    @BeforeEach
    void setUp() {
        coordosyPlayer = new CoordosyPlayer("GroupId", "ffffffff-ffff-ffff-ffff-ffffffffffff");

        coordosyPlayer.x = 42;
        coordosyPlayer.y = 43;
        coordosyPlayer.z = 44;
    }

    @Test
    void test() {

        Gson gson = new Gson();
        CoordosyPlayer coordosyPlayerFromJson = gson.fromJson(coordosyPlayer.toJson(), CoordosyPlayer.class);

        assertEquals(coordosyPlayerFromJson.x,coordosyPlayer.x);
        assertEquals(coordosyPlayerFromJson.y,coordosyPlayer.y);
        assertEquals(coordosyPlayerFromJson.z,coordosyPlayer.z);

        assertNull(coordosyPlayerFromJson.uuid);
        assertNull(coordosyPlayerFromJson.MpGroupId);

    }

}