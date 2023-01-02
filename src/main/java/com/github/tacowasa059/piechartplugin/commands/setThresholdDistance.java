package com.github.tacowasa059.piechartplugin.commands;

import com.github.tacowasa059.piechartplugin.PieChartPlugin;
import com.github.tacowasa059.piechartplugin.utils.MakePieChart;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class setThresholdDistance implements CommandExecutor {
    private final PieChartPlugin plugin;

    public setThresholdDistance(PieChartPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p =(Player) sender;
            if(p.hasPermission("getPieChart")){
                if(args.length==0){
                    p.sendMessage("引数が不足しています。");
                    p.sendMessage(ChatColor.RED+"/setThresholdDistance <value>");
                }
                else{
                    double n=Double.parseDouble(args[0]);
                    plugin.getConfig().set("ThresholdDistance",n);
                    p.sendMessage(ChatColor.GREEN +"ThresholdDistanceが"+n+"に変更されました");
                }
            }
            else{
                p.sendMessage(ChatColor.RED+"このコマンドを実行する権限がありません。");
            }
        }
        else{
            System.out.println("このコマンドはプレイヤーから実行してください。");
        }
        return true;
    }
}
