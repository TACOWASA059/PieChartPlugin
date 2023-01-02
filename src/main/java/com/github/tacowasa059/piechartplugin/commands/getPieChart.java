package com.github.tacowasa059.piechartplugin.commands;

import com.github.tacowasa059.piechartplugin.PieChartPlugin;
import com.github.tacowasa059.piechartplugin.utils.GiveMap;
import com.github.tacowasa059.piechartplugin.utils.MakePieChart;
import com.github.tacowasa059.piechartplugin.utils.SkinRetriever;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class getPieChart implements CommandExecutor {
    private final PieChartPlugin plugin;
    public getPieChart(PieChartPlugin plugin){
        this.plugin=plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p=(Player) sender;
            if(p.hasPermission("getPieChart")){
                if(args.length==0){//引数がないとき
                    p.sendMessage(ChatColor.RED+"MCIDを指定してください。");
                    p.sendMessage(ChatColor.RED+"/getskin <MCID>");
                }
                else{
                    p.sendMessage(ChatColor.GREEN+"スキンを取得しています。");
                    String mcid=args[0];
                    SkinRetriever skinRetriever=new SkinRetriever();
                    //mcidからスキンを取得
                    BufferedImage img =skinRetriever.getSkin(mcid);
                    if(img==null){
                        p.sendMessage(ChatColor.RED+"スキンの取得に失敗しました。");
                        return false;
                    }
                    p.sendMessage(ChatColor.GREEN+"スキンの読み込みが完了しました。");
                    if(plugin.getConfig().getBoolean("SaveImageAsPNG")){
                        try {
                            ImageIO.write(img, "png", new File("skin.png"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    List<Map.Entry<Integer, Integer>> list= MakePieChart.CountColor(img);
                    //円グラフの作成
                    MakePieChart makePieChart=new MakePieChart(plugin);
                    BufferedImage bufferedImage_original =makePieChart.getPieChart(list,"chart1.png");
                    List<Map.Entry<Integer, Integer>> list2=makePieChart.modify_image(list);
                    BufferedImage bufferedImage=makePieChart.getPieChart(list2,"chart2.png");

                    p.sendMessage(ChatColor.AQUA+"円グラフの生成が完了しました。");
                    GiveMap.output(bufferedImage_original,p);//地図を付与する
                    GiveMap.output(bufferedImage,p);//地図を付与する
                }
            }
            else{
                p.sendMessage(ChatColor.RED+"このコマンドを実行する権限がありません。");
            }
        }
        return true;
    }
}
