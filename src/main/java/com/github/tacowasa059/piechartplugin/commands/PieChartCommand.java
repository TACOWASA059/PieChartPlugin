package com.github.tacowasa059.piechartplugin.commands;

import com.github.tacowasa059.piechartplugin.PieChartPlugin;
import com.github.tacowasa059.piechartplugin.utils.GiveMap;
import com.github.tacowasa059.piechartplugin.utils.MakePieChart;
import com.github.tacowasa059.piechartplugin.utils.SkinRetriever;
import net.md_5.bungee.api.ChatMessageType;
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

public class PieChartCommand implements CommandExecutor {
    private final PieChartPlugin plugin;
    public PieChartCommand(PieChartPlugin plugin){
        this.plugin=plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p=(Player) sender;
            if(!p.isOp()){
                p.sendMessage(ChatColor.RED+"このコマンドを実行する権限がありません。");
                return true;
            }
            if(args.length==2&& args[0].equalsIgnoreCase("get")){
                p.sendMessage(ChatColor.LIGHT_PURPLE+"-------------------");
                p.sendMessage(ChatColor.GREEN+"スキンを取得しています。");
                String mcid=args[1];
                SkinRetriever skinRetriever=new SkinRetriever();
                //mcidからスキンを取得
                BufferedImage img =skinRetriever.getSkin(mcid);
                if(img==null){
                    p.sendMessage(ChatColor.RED+"スキンの取得に失敗しました。");
                    p.sendMessage(ChatColor.RED+"MCIDが正しいことを確認してください。");
                    p.sendMessage(ChatColor.AQUA+"/PieChart get <MCID>");
                    p.sendMessage(ChatColor.LIGHT_PURPLE+"-------------------");
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
                BufferedImage bufferedImage_original =makePieChart.getPieChart(list,"chart-original.png");
                List<Map.Entry<Integer, Integer>> list2=makePieChart.modify_image(list);
                BufferedImage bufferedImage=makePieChart.getPieChart(list2,"chart-cluster.png");
                List<Map.Entry<Integer,Integer>> list3=makePieChart.sort_color_count(list);
                BufferedImage bufferedImage2=makePieChart.getPieChart(list3,"chart-sorted.png");

                p.sendMessage(ChatColor.AQUA+"円グラフの生成が完了しました。");
                p.sendMessage(ChatColor.LIGHT_PURPLE+"-------------------");
                GiveMap.output(bufferedImage_original,p,mcid+"-original");//地図を付与する
                GiveMap.output(bufferedImage,p,mcid+"-clustered");//地図を付与する
                GiveMap.output(bufferedImage2,p,mcid+"-sorted");//地図の付与
                return true;
            }
            else if(args.length==2&&args[0].equalsIgnoreCase("setThreshold")){
                double n;
                try{
                    n=Double.parseDouble(args[1]);
                }catch (NumberFormatException e){
                    p.sendMessage(ChatColor.RED+"引数は数値で入力してください。");
                    p.sendMessage(ChatColor.AQUA+"/PieChart setThreshold <value>");
                    return true;
                }

                plugin.getConfig().set("ThresholdDistance",n);
                p.sendMessage(ChatColor.GREEN +"ThresholdDistanceが"+n+"に変更されました");
                return true;
            }
            else if(args.length==1&&args[0].equalsIgnoreCase("showConfig")){
                p.sendMessage(ChatColor.LIGHT_PURPLE+"-------------------");
                p.sendMessage(ChatColor.AQUA+"ThresholdDistance(sortedで使う閾値)"+ChatColor.GREEN+" : "+ChatColor.GREEN+plugin.getConfig().getDouble("ThresholdDistance"));
                p.sendMessage(ChatColor.AQUA+"SaveImageAsPNG(pngファイルをサーバーに保存するか)"+ChatColor.GREEN+" : "+plugin.getConfig().getBoolean("SaveImageAsPNG"));
                p.sendMessage(ChatColor.LIGHT_PURPLE+"-------------------");
                return true;
            }
            else if(args.length==1&&args[0].equalsIgnoreCase("saveConfig")){
                plugin.saveConfig();
                p.sendMessage(ChatColor.GREEN+"コンフィグを保存しました。");
            }
            else if(args.length==1&&args[0].equalsIgnoreCase("reloadConfig")){
                plugin.reloadConfig();
                p.sendMessage(ChatColor.GREEN+"コンフィグをリロードしました。");
            }
            else{
                p.sendMessage(ChatColor.RED+"コマンドが間違っています。");
            }

        }
        return true;
    }
}
