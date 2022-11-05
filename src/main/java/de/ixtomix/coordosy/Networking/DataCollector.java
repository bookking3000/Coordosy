package de.ixtomix.coordosy.Networking;

import de.ixtomix.coordosy.Configuration.CoordosyConfig;
import de.ixtomix.coordosy.Coordosy;
import de.ixtomix.coordosy.Data.CoordosyPlayer;
import de.ixtomix.coordosy.Data.CoordosyPlayerLookVector;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class DataCollector {

    Player player;

    List<? extends Player> entityList;

    public DataCollector(Player player) {
        this.player = player;
    }

    public void run() {

        CoordosyPlayer coordosyPlayer = new CoordosyPlayer(
                CoordosyConfig.MP_GROUP_ID.get(),
                player.getStringUUID()
        );

        coordosyPlayer.x = this.player.getX();
        coordosyPlayer.y = this.player.getY();
        coordosyPlayer.z = this.player.getZ();
        coordosyPlayer.lookVector = new CoordosyPlayerLookVector(this.player.getLookAngle());
        coordosyPlayer.worldName = getWorldName();


        if (this.player.isAlive()) {
            if (CoordosyConfig.AGGRESSIVE_MODE.get()) {
                populateEntityList(this.player);
            }

            try {
                sendCoordinatesToRealtimeDatabase(coordosyPlayer);
            } catch (IOException e) {
                Coordosy.LOGGER.error(e);
            }

        }
    }

    public void clear() {

        CoordosyPlayer coordosyPlayer = new CoordosyPlayer(
                CoordosyConfig.MP_GROUP_ID.get(),
                player.getStringUUID()
        );

        coordosyPlayer.worldName = getWorldName();

        try {
            clearCoordinatesFromRealtimeDatabase(coordosyPlayer);
        } catch (IOException e) {
            Coordosy.LOGGER.error(e);
        }
    }

    @NotNull
    private String getWorldName() {
        String worldName;
        if (Minecraft.getInstance().getCurrentServer() != null) {
            worldName = String.valueOf(Minecraft.getInstance().getCurrentServer().ip.hashCode());
        } else {
            Coordosy.LOGGER.debug("You are tracked in Single-Player mode, which is usually only used in development environments.");
            worldName = player.getStringUUID() + "-sp";
        }
        return worldName;
    }

    private void populateEntityList(Player clientPlayer) {

        entityList = clientPlayer.getLevel().players();
        entityList.removeIf(
                e -> e.equals(clientPlayer)
        );

        //Coordosy.LOGGER.debug("eL: " + Arrays.toString(entityList.toArray()));
    }


    private void sendCoordinatesToRealtimeDatabase(CoordosyPlayer coordosyPlayer) throws IOException {

        String groupId = CoordosyConfig.MP_GROUP_ID.get();
        String world = coordosyPlayer.worldName;
        String uuid = coordosyPlayer.uuid;

        try {
            new CURL().patch(CoordosyConfig.API_ENDPOINT.get() + "/" + groupId + world + "/users/" + uuid + "/position.json", coordosyPlayer.toJson());
            new CURL().patch(CoordosyConfig.API_ENDPOINT.get() + "/" + groupId + world + "/users/" + uuid + "/lookVector.json", coordosyPlayer.lookVector.toJson());
        } catch (Exception e) {
            Coordosy.LOGGER.error(e);
        }

    }

    private void clearCoordinatesFromRealtimeDatabase(CoordosyPlayer coordosyPlayer) throws IOException {

        String groupId = CoordosyConfig.MP_GROUP_ID.get();
        String world = coordosyPlayer.worldName;
        String uuid = coordosyPlayer.uuid;

        try {
            new CURL().delete(CoordosyConfig.API_ENDPOINT.get() + "/" + groupId + "/users/" + world + "/" + uuid + "/position.json");
            new CURL().delete(CoordosyConfig.API_ENDPOINT.get() + "/" + groupId + "/users/" + world + "/" + uuid + "/lookVector.json");
        } catch (Exception e) {
            Coordosy.LOGGER.error(e);
        }

    }

}
