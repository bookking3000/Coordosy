package de.bookking3000.coordosy.Networking;

import de.bookking3000.coordosy.Coordosy;
import de.bookking3000.coordosy.Data.Coordinates;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Level;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class CoordosyNetworkThread {

    private final UUID uuid;

    public CoordosyNetworkThread(PlayerEntity playerEntity) {
        this.uuid = playerEntity.getUniqueID();
    }

    public void sendDataToLocalAppServer(Coordinates coordinates) {
        Socket server = null;
        try {
            server = new Socket("localhost", 10050);
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);

            out.println("UUID:" + this.uuid);
            out.println("Coordinates:" + coordinates);
        } catch (Exception e) {
            Coordosy.LOGGER.error(e);
        }
    }

}
