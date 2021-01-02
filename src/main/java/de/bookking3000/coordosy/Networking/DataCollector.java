package de.bookking3000.coordosy.Networking;

import com.google.gson.Gson;
import de.bookking3000.coordosy.Configuration.CoordosyConfig;
import de.bookking3000.coordosy.Coordosy;
import de.bookking3000.coordosy.Data.JsonData;
import de.bookking3000.coordosy.Data.PlayerPosition;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;

public class DataCollector {

    ClientPlayerEntity clientPlayerEntity;
    List<Entity> entityList;

    public DataCollector(ClientPlayerEntity clientPlayerEntity) {
        this.clientPlayerEntity = clientPlayerEntity;
    }

    public void run() {

        JsonData data = new JsonData(CoordosyConfig.MP_GROUP_ID.get());
        ClientPlayerEntity clientPlayerEntity = this.clientPlayerEntity;

        data.x = clientPlayerEntity.getPosX();
        data.y = clientPlayerEntity.getPosY();
        data.z = clientPlayerEntity.getPosZ();
        data.uuid = String.valueOf(clientPlayerEntity.getUniqueID());
        data.lookVector = clientPlayerEntity.getLookVec();

        if (clientPlayerEntity.isAlive() && clientPlayerEntity.isUser())
        {
            if (CoordosyConfig.AGGRESSIVE_MODE.get())
            {

                int bbExpansion = CoordosyConfig.BOUNDING_BOX_RANGE_EXPANSION.get();

                AxisAlignedBB alignedBB = clientPlayerEntity.getBoundingBox().grow(bbExpansion);

                entityList = clientPlayerEntity.world.getEntitiesWithinAABB(RemoteClientPlayerEntity.class, alignedBB);
                entityList.removeIf(e -> e.equals(clientPlayerEntity));

            }
            sendCoordinatesToHelperApplication(data, entityList);
        }
    }

    private void sendCoordinatesToHelperApplication(JsonData data, List<Entity> entityList) {

        try
        {
            String postUrl = CoordosyConfig.API_ENDPOINT.get();
            Gson gson = new Gson();

            //TODO Permission?
            if (CoordosyConfig.AGGRESSIVE_MODE.get())
            {
                data.entityListString = makeEntityListString(entityList);
            }

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(postUrl);

            StringEntity postingString = new StringEntity(gson.toJson(data));

            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            Coordosy.LOGGER.debug("ecx::" + httpClient.execute(post));

        } catch (Exception e)
        {
            Coordosy.LOGGER.error(e.getMessage());
        }

    }

    protected String makeEntityListString(List<Entity> list) {

        if (list == null)
        {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("[");
        for (int i = 0; i < list.size(); i++)
        {
            Entity e = list.get(i);
            stringBuilder.append(new PlayerPosition(e.getPosX(), e.getPosY(), e.getPosZ(), e.getUniqueID()).toString());
            if (i != list.size() - 1)
                stringBuilder.append(",");
        }
        stringBuilder.append("]");

        return stringBuilder.toString();

    }

}
