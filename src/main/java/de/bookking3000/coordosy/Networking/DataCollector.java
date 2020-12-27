package de.bookking3000.coordosy.Networking;

import com.google.gson.Gson;
import de.bookking3000.coordosy.Configuration.CoordosyConfig;
import de.bookking3000.coordosy.Coordosy;
import de.bookking3000.coordosy.Data.JsonData;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class DataCollector extends Thread {

    ClientPlayerEntity clientPlayerEntity;

    public DataCollector(ClientPlayerEntity clientPlayerEntity) {
        this.clientPlayerEntity = clientPlayerEntity;
    }

    @Override
    public void run() {

        JsonData data = new JsonData();
        ClientPlayerEntity clientPlayerEntity = this.clientPlayerEntity;

        data.x = clientPlayerEntity.getPosX();
        data.y = clientPlayerEntity.getPosY();
        data.z = clientPlayerEntity.getPosZ();
        data.uuid = String.valueOf(clientPlayerEntity.getUniqueID());
        data.lookVector = clientPlayerEntity.getLookVec();

        if (clientPlayerEntity.isAlive() && clientPlayerEntity.isUser()) {
            if (CoordosyConfig.AGGRESSIVE_MODE.get()) {
                List<Entity> entityList = clientPlayerEntity.world.getEntitiesWithinAABB(PlayerEntity.class, clientPlayerEntity.getBoundingBox().expand(CoordosyConfig.BOUNDING_BOX_RANGE_EXPANSION.get(), CoordosyConfig.BOUNDING_BOX_RANGE_EXPANSION.get(), CoordosyConfig.BOUNDING_BOX_RANGE_EXPANSION.get()));
                entityList.removeIf(e -> e.equals(clientPlayerEntity));
                data.entityList = entityList;
            }
            sendCoordinatesToHelperApplication(data);
        }

    }

    private void sendCoordinatesToHelperApplication(JsonData data) {

        try {
            String postUrl = CoordosyConfig.API_ENDPOINT.get();
            Gson gson = new Gson();

            if (CoordosyConfig.AGGRESSIVE_MODE.get()) {
                data.listString = data.entityList.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
            } else
                data.listString = Coordosy.MODID + " is configured to be non-aggressive in your Client-Config.";

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(postUrl);

            StringEntity postingString = new StringEntity(gson.toJson(data));
            gson.toJson(data);

            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            httpClient.execute(post);

        } catch (Exception e) {
            Coordosy.LOGGER.error(e.getMessage());
        }

    }
}
