package de.ixtomix.coordosy.Networking;

import de.ixtomix.coordosy.Coordosy;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CURL {
    public void patch(String Url, String Data) throws Exception {
        URL obj = new URL(Url);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPatch httpPatch = new HttpPatch(obj.toURI());
        httpPatch.setHeader("Content-type", "application/json");

        StringEntity entity = new StringEntity(Data, StandardCharsets.UTF_8);
        httpPatch.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPatch);
        int statusCode = response.getStatusLine().getStatusCode();

        Coordosy.LOGGER.debug("Response (" + statusCode + "): " + response);

        try {
            httpClient.close();
        }
        catch (IOException e) {
            Coordosy.LOGGER.error(e.getMessage());
        }

    }

    public void delete(String Url) throws Exception {
        URL obj = new URL(Url);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(obj.toURI());
        HttpResponse response = httpClient.execute(httpDelete);

        int statusCode = response.getStatusLine().getStatusCode();
        Coordosy.LOGGER.debug("Response (" + statusCode + "): " + response);

        try {
            httpClient.close();
        }
        catch (IOException e) {
            Coordosy.LOGGER.error(e.getMessage());
        }

    }

}
