package com.Buildathon.Handlers;

import com.Buildathon.Buildathon;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class BuildathonCommandHandler implements CommandExecutor {

    public Buildathon plugin;


    World world;

    public BuildathonCommandHandler(Buildathon plugin) {
        this.plugin = plugin;
        world = Bukkit.getWorld("world");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("buildathon")) {

            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (p.hasPermission("buildathon.use")) {

                    if (args.length >= 1) {
                        String argument = args[0];
                        p.sendMessage("You used the /buildathon command with args[0] as " + argument);
                    }

                    else {
                        p.sendMessage("You just typed /buildathon");
                    }
                }
                else {
                    p.sendMessage("You don't have permission to use /buildathon");
                }
            }

        }

        return true;
    }

}
