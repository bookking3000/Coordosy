package de.ixtomix.coordosy.Networking;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import de.ixtomix.coordosy.Configuration.CoordosyConfig;
import de.ixtomix.coordosy.Coordosy;
import de.ixtomix.coordosy.Data.CoordosyPlayer;
import de.ixtomix.coordosy.Data.CoordosyPlayerLookVector;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

//Todo Naming
public class DataCollector {

    Player player;

    List<? extends Player> entityList;

    public static final RethinkDB r = RethinkDB.r;

    public DataCollector(Player player) {
        this.player = player;
    }

    Connection conn = r.connection().user(CoordosyConfig.RETHINKDB_USER.get()).hostname(CoordosyConfig.RETHINKDB_HOST.get()).port(28015).connect();

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

    }

    private void sendCoordinatesToRealtimeDatabase(CoordosyPlayer coordosyPlayer) throws IOException {

        String groupId = CoordosyConfig.MP_GROUP_ID.get();

        try {
            r.db("coordosy-" + groupId).table("users").insert(r.json(coordosyPlayer.toJson())).optArg("conflict", "update").run(conn);
        } catch (Exception e) {
            Coordosy.LOGGER.error(e);
        }

    }

    private void clearCoordinatesFromRealtimeDatabase(CoordosyPlayer coordosyPlayer) throws IOException {

        String groupId = CoordosyConfig.MP_GROUP_ID.get();

        try {
            r.db("coordosy-" + groupId).table("users").get(coordosyPlayer.uuid).delete().run(conn);
        } catch (Exception e) {
            Coordosy.LOGGER.error(e);
        }

    }

}
