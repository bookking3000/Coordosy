package de.ixtomix.coordosy.Configuration;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class CoordosyConfig {

    public static final String CATEGORY_FILE = "coordosy";
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_MULTIPLAYER = "multiplayer";

    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.ConfigValue<String> RETHINKDB_HOST;
    public static ForgeConfigSpec.ConfigValue<String> RETHINKDB_USER;
    public static ForgeConfigSpec.BooleanValue AGGRESSIVE_MODE;
    public static ForgeConfigSpec.IntValue TICKS_BETWEEN_CHECK;

    /**
     * @deprecated
     */
    @Deprecated
    public static ForgeConfigSpec.DoubleValue BOUNDING_BOX_RANGE_EXPANSION;

    public static ForgeConfigSpec.ConfigValue<String> MP_GROUP_ID;

    static
    {
        CLIENT_BUILDER.comment("Coordosy-Settings-File").push(CATEGORY_FILE);

        CLIENT_BUILDER.comment("General").push(CATEGORY_GENERAL);
        setupBasicConfig();

        CLIENT_BUILDER.comment("Multiplayer").push(CATEGORY_MULTIPLAYER);
        setupMultiplayerConfig();
        CLIENT_BUILDER.pop();

        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupBasicConfig() {

        AGGRESSIVE_MODE = CLIENT_BUILDER.comment("true if Coordosy shall scan for other Players in Minecraft itself, false means only sending your own Coordinates to API-Endpoint. It currently is not properly implemented, so let it stay on false")
                .define("aggressiveMode", false);

        RETHINKDB_HOST = CLIENT_BUILDER.comment("RethinkDB-Host")
                .define("rtDbHost", "94.16.107.181");

        RETHINKDB_USER = CLIENT_BUILDER.comment("RethinkDB-User")
                .define("rtDbUser", "coordosy");

        TICKS_BETWEEN_CHECK = CLIENT_BUILDER.comment("How many ticks should Coordosy wait between Requests")
                .defineInRange("rqTicks", 20, 10, Integer.MAX_VALUE);

        CLIENT_BUILDER.pop();
    }

    private static void setupMultiplayerConfig() {

        MP_GROUP_ID = CLIENT_BUILDER.comment("Group-ID - The Developer of this Plugin, will tell this to you, to prevent misuse ;) (just contact me (Ixtomix#2313) on my Discord https://discord.gg/eYjv7Ys9fJ)")
                .define("mpGroupId", "coordosy-default");

        CLIENT_BUILDER.pop();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }
}
