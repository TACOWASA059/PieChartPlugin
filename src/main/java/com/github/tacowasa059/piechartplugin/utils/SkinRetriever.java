package com.github.tacowasa059.piechartplugin.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.json.JSONObject;

//Todolist
//例外処理の追加
public class SkinRetriever {
    public BufferedImage getSkin(String mcid) {
        try{
            // Send a request to the Minecraft skin server
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + getUUID(mcid));
            InputStreamReader reader = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(reader);
            StringBuilder response = new StringBuilder();
            String  line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // Parse the response to get the skin data
            JSONObject json = new JSONObject(response.toString());
            String skinData = json.getJSONArray("properties").getJSONObject(0).getString("value");
            skinData = skinData.replaceFirst("data:image/png;base64,", "");
            // Decode the skin data and save it as a BufferedImage
            JSONObject json_meta=new JSONObject(new String(Base64.getDecoder().decode(skinData)));
            String skin_url = json_meta.getJSONObject("textures").getJSONObject("SKIN").getString("url");
            BufferedImage skin = ImageIO.read(new URL(skin_url));
            return skin;
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException("IO Exception", ioEx);
        }
    }
    //mcidからuuidを取得するmethod
    private String getUUID(String mcid) {
        try{
            // Send a request to the Minecraft skin server
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + mcid);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(reader);
            StringBuilder response = new StringBuilder();
            String  line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            //Jsonobject
            JSONObject json = new JSONObject(response.toString());
            String uuid = json.getString("id");
            System.out.println("uuid:"+uuid);
            return uuid;
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException("IO Exception", ioEx);
        }
    }
}