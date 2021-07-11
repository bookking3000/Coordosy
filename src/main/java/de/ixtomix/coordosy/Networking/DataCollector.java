package de.ixtomix.coordosy.Networking;

import de.ixtomix.coordosy.Configuration.CoordosyConfig;
import de.ixtomix.coordosy.Coordosy;
import de.ixtomix.coordosy.Data.CoordosyPlayer;
import de.ixtomix.coordosy.Data.CoordosyPlayerLookVector;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DataCollector {

    ClientPlayerEntity clientPlayerEntity;
    List<Entity> entityList;

    public DataCollector(ClientPlayerEntity clientPlayerEntity) {
        this.clientPlayerEntity = clientPlayerEntity;
    }

    public void run() {

        CoordosyPlayer coordosyPlayer = new CoordosyPlayer(
                CoordosyConfig.MP_GROUP_ID.get(),
                String.valueOf(clientPlayerEntity.getUniqueID())
        );

        ClientPlayerEntity clientPlayerEntity = this.clientPlayerEntity;

        coordosyPlayer.x = clientPlayerEntity.getPosX();
        coordosyPlayer.y = clientPlayerEntity.getPosY();
        coordosyPlayer.z = clientPlayerEntity.getPosZ();
        coordosyPlayer.lookVector = new CoordosyPlayerLookVector(clientPlayerEntity.getLookVec());

        if (clientPlayerEntity.isAlive() && clientPlayerEntity.isUser()) {
            if (CoordosyConfig.AGGRESSIVE_MODE.get()) {
                populateEntityList(clientPlayerEntity, coordosyPlayer);
            }

            try {
                sendCoordinatesToRealtimeDatabase(coordosyPlayer);
            } catch (IOException e) {
                Coordosy.LOGGER.error(e);
            }

        }
    }

    private void populateEntityList(ClientPlayerEntity clientPlayerEntity, CoordosyPlayer coordosyPlayer) {
        int bbExpansion = CoordosyConfig.BOUNDING_BOX_RANGE_EXPANSION.get();

        AxisAlignedBB alignedBB = clientPlayerEntity.getBoundingBox().grow(bbExpansion);

        entityList = clientPlayerEntity.world.getEntitiesWithinAABB(RemoteClientPlayerEntity.class, alignedBB);

        entityList.removeIf(
                e -> e.equals(clientPlayerEntity)
        );

        coordosyPlayer.entityListString = entityList.stream()
                .map(Entity::toString)
                .collect(Collectors.joining(", "));

    }

    private void sendCoordinatesToRealtimeDatabase(CoordosyPlayer coordosyPlayer) throws IOException {

        String groupId = CoordosyConfig.MP_GROUP_ID.get();
        String uuid = coordosyPlayer.uuid;

        try
        {
            new CURL().patch(CoordosyConfig.API_ENDPOINT.get() + groupId + "/users/" + uuid + "/position.json", coordosyPlayer.toJson());
            new CURL().patch(CoordosyConfig.API_ENDPOINT.get() + groupId + "/users/" + uuid + "/lookVector.json", coordosyPlayer.lookVector.toJson());
        }
        catch (Exception e)
        {
            Coordosy.LOGGER.error(e);
        }


    }

}
