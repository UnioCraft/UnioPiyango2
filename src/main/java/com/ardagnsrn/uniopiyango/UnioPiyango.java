package com.ardagnsrn.uniopiyango;

import com.ardagnsrn.uniopiyango.commands.CmdPiyango;
import com.ardagnsrn.uniopiyango.listeners.PlayerListeners;
import com.ardagnsrn.uniopiyango.managers.ConfigManager;
import com.ardagnsrn.uniopiyango.managers.ConfigManager.Config;
import com.ardagnsrn.uniopiyango.managers.GUIManager;
import com.ardagnsrn.uniopiyango.managers.TicketManager;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class UnioPiyango extends JavaPlugin implements Listener {

    public static String hataPrefix = ChatColor.AQUA + "" + ChatColor.BOLD + "UNIOCRAFT " + ChatColor.DARK_GREEN + "->" + ChatColor.RED + " ";
    public static String dikkatPrefix = ChatColor.AQUA + "" + ChatColor.BOLD + "UNIOCRAFT " + ChatColor.DARK_GREEN + "->" + ChatColor.GOLD + " ";
    public static String bilgiPrefix = ChatColor.AQUA + "" + ChatColor.BOLD + "UNIOCRAFT " + ChatColor.DARK_GREEN + "->" + ChatColor.GREEN + " ";
    public static String consolePrefix = "[UnioPiyango] ";

    @Getter
    private ConfigManager configManager;
    @Getter
    private TicketManager ticketManager;
    @Getter
    private GUIManager guiManager;
    @Getter
    private Economy economy;
    @Getter
    @Setter
    private Boolean eventStatus; // True: Ongoing, False: Over

    public void onEnable() {
        setupVaultEconomy();

        //Managers
        configManager = new ConfigManager(this);
        ticketManager = new TicketManager(this);
        guiManager = new GUIManager(this);

        //Listeners
        new PlayerListeners(this);

        //Commands
        new CmdPiyango(this);

        eventStatus = getConfig().getBoolean("eventStatus");
    }

    public void onDisable() {
        ticketManager.saveTickets();
    }

    public void reload() {
        reloadConfig();
        for (Config config : Config.values()) {
            configManager.saveConfig(config);
            configManager.reloadConfig(config);
        }
        eventStatus = getConfig().getBoolean("eventStatus");
        guiManager = new GUIManager(this);

    }

    public String getMessage(String configSection) {
        FileConfiguration config = configManager.getConfig(Config.LANG);
        if (config.getString(configSection) == null) return null;

        return ChatColor.translateAlternateColorCodes('&', config.getString(configSection).replaceAll("%hataprefix%", hataPrefix).replaceAll("%bilgiprefix%", bilgiPrefix).replaceAll("%dikkatprefix%", dikkatPrefix).replaceAll("%prefix%", bilgiPrefix));
    }

    public List<String> getMessages(String configSection) {
        FileConfiguration config = configManager.getConfig(Config.LANG);
        if (config.getString(configSection) == null) return null;

        List<String> newList = new ArrayList<>();
        for (String msg : config.getStringList(configSection)) {
            newList.add(ChatColor.translateAlternateColorCodes('&', msg.replaceAll("%hataprefix%", hataPrefix).replaceAll("%bilgiprefix%", bilgiPrefix).replaceAll("%dikkatprefix%", dikkatPrefix).replaceAll("%prefix%", bilgiPrefix)));
        }
        return newList;
    }

    private void setupVaultEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }
}
