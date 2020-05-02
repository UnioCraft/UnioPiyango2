package com.ardagnsrn.uniopiyango.listeners;

import com.ardagnsrn.uniopiyango.UnioPiyango;
import com.ardagnsrn.uniopiyango.managers.TicketManager.TicketType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerListeners implements Listener {

    private UnioPiyango plugin;
    private Map<String, Integer> ticketAmounts = new HashMap<>();

    public PlayerListeners(UnioPiyango plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if (!event.getInventory().getTitle().equals(plugin.getMessage("gui.name"))) {
            return;
        }

        ticketAmounts.put(player.getName(), plugin.getTicketManager().getAllTickets(player.getName()).size());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!event.getInventory().getTitle().equals(plugin.getMessage("gui.name"))) {
            return;
        }

        if (ticketAmounts.containsKey(player.getName())) {
            int oldAmount = ticketAmounts.get(player.getName());
            int currentAmount = plugin.getTicketManager().getAllTickets(player.getName()).size();
            int finalAmount = currentAmount - oldAmount;

            if (finalAmount != 0 && plugin.getBroadcastBuying()) {
                for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                    if (!plugin.getTicketManager().getBroadcastClosed().contains(otherPlayer.getName())) {
                        otherPlayer.sendMessage(plugin.getMessage("buyTickets.alert").replaceAll("%player%", player.getName()).replaceAll("%amount%", String.valueOf(finalAmount)));
                    }
                }
            }

            ticketAmounts.remove(player.getName());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getTitle().equals(plugin.getMessage("toptenGUI.name"))) {
            event.setCancelled(true);
            return;
        }

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
            if (event.getSlot() == 8) {
                player.openInventory(plugin.getGuiManager().getTopTenGUI());
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
            if (event.getSlot() == 18) {
                player.closeInventory();
                player.performCommand("piyango mesaj");
            }
            if (event.getSlot() == 22) {
                player.closeInventory();
                plugin.getTicketManager().sendTicketInformation(player);
            }
        }
    }
}
