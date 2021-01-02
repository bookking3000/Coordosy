package de.bookking3000.coordosy.Configuration;

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

    public static ForgeConfigSpec.ConfigValue<String> API_ENDPOINT;
    public static ForgeConfigSpec.BooleanValue AGGRESSIVE_MODE;
    public static ForgeConfigSpec.IntValue TICKS_BETWEEN_CHECK;
    public static ForgeConfigSpec.IntValue BOUNDING_BOX_RANGE_EXPANSION;

    public static ForgeConfigSpec.ConfigValue<String> MP_GROUP_ID;
    public static ForgeConfigSpec.ConfigValue<String> MP_BLACKLIST;

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

    //ToDO RemoteConfig?
    private static void setupBasicConfig() {

        AGGRESSIVE_MODE = CLIENT_BUILDER.comment("True if Coordosy shall scan for other Players in Minecraft itself, false means only sending Data to API-Endpoint")
                .define("aggressiveMode", false);

        API_ENDPOINT = CLIENT_BUILDER.comment("API-Endpoint (You'll probably won't change that)")
                .define("apiEndpoint", "http://coordosy-api.ixtomix.codes/");

        TICKS_BETWEEN_CHECK = CLIENT_BUILDER.comment("How many ticks should Coordosy wait between Requests")
                .defineInRange("rqTicks", 20, 10, Integer.MAX_VALUE);

        BOUNDING_BOX_RANGE_EXPANSION = CLIENT_BUILDER.comment("The radius Coordosy is Scanning for Players in Aggressive Mode")
                .defineInRange("boundingBoxExp", 50, 25, Integer.MAX_VALUE);

        CLIENT_BUILDER.pop();
    }

    private static void setupMultiplayerConfig() {

        MP_GROUP_ID = CLIENT_BUILDER.comment("Group-ID -- The Developer of this Plugin, will tell this to you, to prevent misuse ;)")
                .define("mpGroupId", "tb007");

        //MP_BLACKLIST = CLIENT_BUILDER.comment("Servers you want this Mod to be inactive on (WIP)")
        //      .define("serverBlacklist", "");

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
