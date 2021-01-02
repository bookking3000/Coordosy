package de.bookking3000.coordosy;

import de.bookking3000.coordosy.Configuration.CoordosyConfig;
import de.bookking3000.coordosy.Networking.DataCollector;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(Coordosy.MODID)
public class Coordosy {

    public static final String MODID = "coordosy";
    public static final String VERSION = "1.0";
    public static final Logger LOGGER = LogManager.getLogger();

    public final Minecraft minecraft = Minecraft.getInstance();
    private int ticks = 0;

    public Coordosy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CoordosyConfig.CLIENT_CONFIG);
        CoordosyConfig.loadConfig(CoordosyConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("coordosy-client.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info(MODID + " " + VERSION + " " + " loaded");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (ticks != (CoordosyConfig.TICKS_BETWEEN_CHECK.get()))
        {
            ticks++;
            return;
        }
        ticks = 0;

        if (minecraft.player == null)
        {
            return;
        }
        if (minecraft.isSingleplayer())
        {
            return;
        }
        if (minecraft.player != event.player)
        {
            return;
        }

        try
        {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                DataCollector dataCollector = new DataCollector(minecraft.player);
                dataCollector.run();
            });
        } catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
    }
}
