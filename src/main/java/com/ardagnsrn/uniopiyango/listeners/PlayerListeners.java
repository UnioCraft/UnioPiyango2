package com.ardagnsrn.uniopiyango.listeners;

import com.ardagnsrn.uniopiyango.UnioPiyango;
import com.ardagnsrn.uniopiyango.managers.TicketManager.TicketType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerListeners implements Listener {

    private UnioPiyango plugin;

    public PlayerListeners(UnioPiyango plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getTitle().equals(plugin.getMessage("gui.name"))) {
            event.setCancelled(true);
            if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }
            if (event.getSlot() == 0) {
                return;
            }
            if (event.getSlot() == 4) {
                return;
            }
            if (event.getSlot() == 12) {
                plugin.getTicketManager().buyTicket(player, TicketType.QUARTER);
            }
            if (event.getSlot() == 13) {
                plugin.getTicketManager().buyTicket(player, TicketType.HALF);
            }
            if (event.getSlot() == 14) {
                plugin.getTicketManager().buyTicket(player, TicketType.FULL);
            }
            if (event.getSlot() == 22) {
                player.closeInventory();
                plugin.getTicketManager().sendTicketInformation(player);
            }
        }
    }
}
