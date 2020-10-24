package com.Buildathon.Handlers;

import com.Buildathon.Buildathon;
import com.Buildathon.BuildathonGlobals;
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


                        // If player types /buildathon help
                        if (argument.equalsIgnoreCase("help")) {
                            plugin.sendHelp(p);
                        }

                        // If player types /buildathon adduser
                        else if (argument.equalsIgnoreCase("adduser")) {
                            if (sender.hasPermission("buildathon.adduser")) {
                                if (args.length == 3) {
                                    plugin.ch.addUser(args[1], args[2]);
                                    plugin.sendInfo(p, "Added new user '" + args[1] + "' to team '" + args[2] + "'");
                                }
                                else {
                                    plugin.sendInfo(p, "Usage: /buildathon adduser [name] [team]");
                                }
                            }
                        }

                        // If player types /buildathon teams
                        else if (argument.equalsIgnoreCase("teams")) {
                            if (sender.hasPermission("buildathon.teams")) {
                                plugin.ch.getTeams(p);
                            }

                        }

                        // If player types /buildathon <anything not listed above>
                        else {
                            plugin.sendHelp(p);
                        }


                    }

                    else {
                        plugin.sendHelp(p);
                    }
                }
                else {
                    plugin.sendInfo(p, "You don't have permission to use /buildathon");
                }
            }

        }

        return true;
    }

}
