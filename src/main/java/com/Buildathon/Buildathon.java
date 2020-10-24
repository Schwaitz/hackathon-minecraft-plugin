package com.Buildathon;

import com.Buildathon.Handlers.BuildathonCommandHandler;
import com.Buildathon.Listeners.BuildathonEventListener;
import com.Buildathon.Utilities.BuildathonUtilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Buildathon extends JavaPlugin {


    public FileConfiguration config;


    public BuildathonUtilities bUtilities;


    @Override
    public void onEnable() {

        getLogger().info("Buildathon has been started");

        if (!getDataFolder().exists()) {
            getLogger().info("Buildathon folder does not exist. Creating it now");
            getDataFolder().mkdirs();
            this.saveDefaultConfig();
        }

        bUtilities = new BuildathonUtilities(this);

        setupFiles();

        this.getCommand("buildathon").setExecutor(new BuildathonCommandHandler(this));
        getServer().getPluginManager().registerEvents(new BuildathonEventListener(this), this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Buildathon has been disabled");
    }


    private boolean setupFiles() {
        File f = new File(getDataFolder().getPath(), "config.yml");

        if (!f.exists()) {
            getLogger().info("config.yml does not exist. Creating it now");
            saveDefaultConfig();
        }
        else {
            getLogger().info("config.yml exists. Loading existing configuration");
            getConfig().options().copyDefaults(true);
            saveConfig();
            config = YamlConfiguration.loadConfiguration(f);
        }


        f = new File(getDataFolder().getPath(), "Buildathon.log");

        if (!f.exists()) {
            getLogger().info("Buildathon.log does not exist. Creating it now");
            try { f.createNewFile(); } catch (IOException e) {}
        }
        else { getLogger().info("Buildathon.log exists"); }

        return true;
    }


    public void logToFile(String message) {
        try {
            File f = new File(getDataFolder(), "Buildathon.log");

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            Date now = new Date();

            FileWriter fw = new FileWriter(f, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(sdf.format(now) + " - " + message);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

