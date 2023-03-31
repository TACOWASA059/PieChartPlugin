package com.github.tacowasa059.piechartplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PieChartTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list=new ArrayList<>();
        if(args.length==1){
            list.add("get");
            list.add("setThreshold");
            list.add("showConfig");
            list.add("saveConfig");
            list.add("reloadConfig");
        }
        else if(args.length==2&&args[0].equalsIgnoreCase("get")){
            for(Player player: Bukkit.getOnlinePlayers()){
                String name=player.getName();
                list.add(name);
            }
        }
        return list;
    }
}
