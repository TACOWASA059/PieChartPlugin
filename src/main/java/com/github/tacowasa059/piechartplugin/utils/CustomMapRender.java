package com.github.tacowasa059.piechartplugin.utils;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import java.awt.Color;

import java.awt.image.BufferedImage;

public class CustomMapRender extends MapRenderer {
    private BufferedImage img;
    public CustomMapRender(BufferedImage img){
        this.img=img;
    }
    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        //x_axis -left to right (0-127)
        //y_axis -top to bottom (0-127)
        mapCanvas.drawImage(0,0, MapPalette.resizeImage(img));
    }
    //byte変換したときに透明度の補正を入れるmethod
    //is not used
    private byte calc_rgb(int rgb){
        Color color = new Color(rgb, true);
        byte c_default=MapPalette.matchColor(color.getRed(), color.getGreen(), color.getBlue());
        Color cPalette = new Color(c_default);

        if (color.getAlpha() == 0){
            return c_default;
        }
        else{
            int cRed = (color.getRed() * color.getAlpha() + cPalette.getRed() * (255 - color.getAlpha())) / 255;
            int cGreen = (color.getGreen() * color.getAlpha() + cPalette.getGreen() * (255 - color.getAlpha())) / 255;
            int cBlue = (color.getBlue() * color.getAlpha() + cPalette.getBlue() * (255 - color.getAlpha())) / 255;

            byte c_modified = MapPalette.matchColor(cRed, cGreen, cBlue);
            return c_modified;
        }
    }
}
