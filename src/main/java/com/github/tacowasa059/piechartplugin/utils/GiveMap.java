package com.github.tacowasa059.piechartplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
//BufferedImage をMapにレンダリングしアイテムとして与える
public class GiveMap {
    public static void output(BufferedImage img, Player player,String name){
        ItemStack mapitem=new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMeta=(MapMeta)mapitem.getItemMeta();
        MapView mapview= Bukkit.createMap(player.getWorld());

        //canvasなどの設定
        CustomMapRender mapRender=new CustomMapRender(img);
        mapview.getRenderers().clear();
        mapview.addRenderer(mapRender);

        mapMeta.setMapView(mapview);
        mapMeta.setDisplayName(name);
        mapitem.setItemMeta(mapMeta);
        player.getInventory().addItem(mapitem);
    }
}
