package de.bookking3000.coordosy.Configuration;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

public class CoordosyConfig {

    public static final String CATEGORY_FILE = "coordosy";
    public static final String CATEGORY_GENERAL = "general";

    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue AGGRESSIVE_MODE;
    public static ForgeConfigSpec.ConfigValue<String> API_ENDPOINT;
    public static ForgeConfigSpec.IntValue TICKS_BETWEEN_CHECK;
    public static ForgeConfigSpec.IntValue BOUNDING_BOX_RANGE_EXPANSION;

    static {

        CLIENT_BUILDER.comment("Coordosy-Settings-File").push(CATEGORY_FILE);
        CLIENT_BUILDER.comment("General").push(CATEGORY_GENERAL);
        setupBasicConfig();
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupBasicConfig() {

        AGGRESSIVE_MODE = CLIENT_BUILDER.comment("True if Coordosy shall scan for other Players in Minecraft itself (High CPU Load!), False means only sending Data to API-Endpoint")
                .define("aggresiveMode", false);

        API_ENDPOINT = CLIENT_BUILDER.comment("API-Endpoint")
                .define("apiEndpoint", "http://coordosy-api.ixtomix.codes/");

        TICKS_BETWEEN_CHECK = CLIENT_BUILDER.comment("How many ticks should Coordosy wait between Requests")
                .defineInRange("rqTicks", 20, 10, Integer.MAX_VALUE);

        BOUNDING_BOX_RANGE_EXPANSION = CLIENT_BUILDER.comment("The radius Coordosy is Scanning for Players in Aggressive Mode")
                .defineInRange("boundingBoxExp", 20, 10, Integer.MAX_VALUE);

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

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }
}
