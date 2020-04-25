package de.bookking3000.coordosy;

import de.bookking3000.coordosy.Data.Coordinates;
import de.bookking3000.coordosy.Networking.CoordosyNetworkThread;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod("coordosy")
public class Coordosy {

    public static final String MODID = "coordosy";
    public static final String VERSION = "1.0";
    public static final Logger LOGGER = LogManager.getLogger();

    public final Minecraft minecraft = Minecraft.getInstance();
    private Coordinates coordinates;
    private int ticks = 0;


    public Coordosy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("coordosy loaded");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (ticks!=100){
            ticks++;
            return;
        }
        ticks = 0;
        if (minecraft.player != event.player){
            return;
        }
        if (minecraft.player != null && minecraft.player.isAlive()) {
            double x = minecraft.player.getPosX();
            double y = minecraft.player.getPosY();
            double z = minecraft.player.getPosZ();
            coordinates = new Coordinates(x, y, z);
        }
        try {
            sendCoordinatesToHelperApplication(coordinates,minecraft.player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCoordinatesToHelperApplication(Coordinates coordinates, PlayerEntity playerEntity) throws IOException {
        CoordosyNetworkThread cnt = new CoordosyNetworkThread(playerEntity);
        cnt.sendDataToLocalAppServer(coordinates);
    }
}
