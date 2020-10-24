package com.Buildathon.Listeners;

import com.Buildathon.Buildathon;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class BuildathonEventListener implements Listener {

    Buildathon plugin;
    World world;

    public BuildathonEventListener(Buildathon plugin) {
        this.plugin = plugin;
        world = Bukkit.getWorld("world");
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent e) {
        e.getPlayer().sendMessage("You placed a " + e.getBlock().getType().name() + " block");

    }

}
