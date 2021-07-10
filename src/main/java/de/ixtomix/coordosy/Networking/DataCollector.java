package de.ixtomix.coordosy.Networking;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import de.ixtomix.coordosy.Configuration.CoordosyConfig;
import de.ixtomix.coordosy.Coordosy;
import de.ixtomix.coordosy.Data.CoordosyPlayer;
import de.ixtomix.coordosy.Listener.RtdbValueEventListener;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataCollector {

    ClientPlayerEntity clientPlayerEntity;
    List<Entity> entityList;

    public DataCollector(ClientPlayerEntity clientPlayerEntity) {
        this.clientPlayerEntity = clientPlayerEntity;
    }

    public void run() {

        CoordosyPlayer coordosyPlayer = new CoordosyPlayer(CoordosyConfig.MP_GROUP_ID.get(), String.valueOf(clientPlayerEntity.getUniqueID()));
        ClientPlayerEntity clientPlayerEntity = this.clientPlayerEntity;

        coordosyPlayer.x = clientPlayerEntity.getPosX();
        coordosyPlayer.y = clientPlayerEntity.getPosY();
        coordosyPlayer.z = clientPlayerEntity.getPosZ();
        coordosyPlayer.lookVector = clientPlayerEntity.getLookVec();

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

        FileInputStream serviceAccount =
                new FileInputStream("de/ixtomix/coordosy/Networking/Secrets/coordosy.json");

        FirebaseOptions.Builder firebaseOptionsBuilder = FirebaseOptions.builder();
        firebaseOptionsBuilder.setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(CoordosyConfig.API_ENDPOINT.get());

        FirebaseOptions firebaseOptions = firebaseOptionsBuilder.build();
        FirebaseApp.initializeApp(firebaseOptions);


        DatabaseReference refByGroupId = FirebaseDatabase.getInstance()
                .getReference(CoordosyConfig.MP_GROUP_ID.get());

        refByGroupId.addListenerForSingleValueEvent(new RtdbValueEventListener());

        DatabaseReference refUsersInGroup = refByGroupId.child("users");

        Map<String, CoordosyPlayer> users = new HashMap<>();
        users.put(coordosyPlayer.uuid, coordosyPlayer);

        refUsersInGroup.setValueAsync(users);
    }


}
