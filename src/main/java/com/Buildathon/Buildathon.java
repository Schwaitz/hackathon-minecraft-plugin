package com.Buildathon;

import com.Buildathon.Handlers.BuildathonCommandHandler;
import com.Buildathon.Handlers.ConnectionHandler;
import com.Buildathon.Listeners.BuildathonEventListener;
import com.Buildathon.Utilities.BuildathonUtilities;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Buildathon extends JavaPlugin {


    public FileConfiguration config;

    Map<String, String> helpStrings;

    public BuildathonUtilities bUtilities;
    public ConnectionHandler ch;


    @Override
    public void onEnable() {

        getLogger().info(BuildathonGlobals.pluginStartMessageString);

        if (!getDataFolder().exists()) {
            getLogger().info("Buildathon folder does not exist. Creating it now");
            getDataFolder().mkdirs();
            this.saveDefaultConfig();
        }


        setupFiles();
        setupHelpStrings();

        bUtilities = new BuildathonUtilities(this);
        ch = new ConnectionHandler(this);


        this.getCommand("buildathon").setExecutor(new BuildathonCommandHandler(this));
        getServer().getPluginManager().registerEvents(new BuildathonEventListener(this), this);

    }

    @Override
    public void onDisable() {
        getLogger().info(BuildathonGlobals.pluginEndMessageString);
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


    void setupHelpStrings() {
        helpStrings = new HashMap<String, String>();

        helpStrings.put("buildathon.adduser", ChatColor.BLUE + "/buildathon adduser [name] [team] - " + ChatColor.GRAY + "Add a new user to the MySQL Database");
        helpStrings.put("buildathon.teams", ChatColor.BLUE + "/buildathon teams - " + ChatColor.GRAY + "Get a list of teams and their members");
    }


    public void sendInfo(Player p, String message) {
        String sendString = BuildathonGlobals.infoString + message;
        p.sendMessage(sendString);
    }

    public void sendHelp(Player p) {
        String sendString = "\n";
        sendString += ChatColor.GRAY + "--------[" + ChatColor.BLUE + "Buildathon Help" + ChatColor.GRAY + "]--------\n";
        sendString += ChatColor.BLUE + "/buildathon help - " + ChatColor.GRAY + "Display this help page\n";

        for (String k : helpStrings.keySet()) {
            if (p.hasPermission(k)) { sendString += helpStrings.get(k) + "\n"; }
        }

        p.sendMessage(sendString);

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

