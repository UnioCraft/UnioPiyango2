package com.ardagnsrn.uniopiyango.managers;

import com.ardagnsrn.uniopiyango.UnioPiyango;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.NumberFormat;
import java.util.List;

public class GUIManager {

    private UnioPiyango plugin;

    public GUIManager(UnioPiyango plugin) {
        this.plugin = plugin;
    }

    public Inventory getGUI(Player player) {
        Inventory gui = Bukkit.getServer().createInventory(player, 27, plugin.getMessage("gui.name"));

        ItemStack emptyItem = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta emptyItemMeta = emptyItem.getItemMeta();
        emptyItemMeta.setDisplayName(" ");
        emptyItem.setItemMeta(emptyItemMeta);

        ItemStack adminItem = new ItemStack(Material.BARRIER);
        ItemMeta adminItemMeta = adminItem.getItemMeta();
        adminItemMeta.setDisplayName(plugin.getMessage("gui.adminItem.name"));
        List<String> adminItemLore = plugin.getMessages("gui.adminItem.lore");
        adminItemLore.replaceAll(str -> str.replaceAll("%totalTickets%", String.valueOf(plugin.getTicketManager().getAllTickets().size()))
                .replaceAll("%quarterTickets%", String.valueOf(plugin.getTicketManager().getAllQuarterTickets().size()))
                .replaceAll("%halfTickets%", String.valueOf(plugin.getTicketManager().getAllHalfTickets().size()))
                .replaceAll("%fullTickets%", String.valueOf(plugin.getTicketManager().getAllFullTickets().size()))
                .replaceAll("%totalMoney%", NumberFormat.getInstance().format(plugin.getTicketManager().getTotalMoneySpent())));
        adminItemMeta.setLore(adminItemLore);
        adminItem.setItemMeta(adminItemMeta);

        ItemStack playerItem = new ItemStack(Material.SKULL_ITEM);
        SkullMeta playerItemMeta = (SkullMeta) playerItem.getItemMeta();
        playerItemMeta.setOwner(player.getName());
        playerItemMeta.setDisplayName(plugin.getMessage("gui.playerItem.name"));
        List<String> playerItemLore = plugin.getMessages("gui.playerItem.lore");
        playerItemLore.replaceAll(str -> str.replaceAll("%player%", player.getName())
                .replaceAll("%totalTickets%", String.valueOf(plugin.getTicketManager().getAllTickets(player.getName()).size()))
                .replaceAll("%quarterTickets%", String.valueOf(plugin.getTicketManager().getQuarterTickets(player.getName()).size()))
                .replaceAll("%halfTickets%", String.valueOf(plugin.getTicketManager().getHalfTickets(player.getName()).size()))
                .replaceAll("%fullTickets%", String.valueOf(plugin.getTicketManager().getFullTickets(player.getName()).size())));
        playerItemMeta.setLore(playerItemLore);
        playerItem.setItemMeta(playerItemMeta);

        ItemStack eventInfo = new ItemStack(Material.WATCH);
        ItemMeta eventInfoMeta = eventInfo.getItemMeta();
        eventInfoMeta.setDisplayName(plugin.getMessage("gui.eventInfo.name"));
        List<String> eventInfoLore = plugin.getMessages("gui.eventInfo.lore");
        eventInfoMeta.setLore(eventInfoLore);
        eventInfo.setItemMeta(eventInfoMeta);

        ItemStack winnerInfo = new ItemStack(Material.BOOK);
        ItemMeta winnerInfoMeta = winnerInfo.getItemMeta();
        winnerInfoMeta.setDisplayName(plugin.getMessage("gui.winnerInfo.name"));
        List<String> winnerInfoLore = plugin.getMessages("gui.winnerInfo.lore");
        winnerInfoMeta.setLore(winnerInfoLore);
        winnerInfo.setItemMeta(winnerInfoMeta);

        ItemStack quarterItem = new ItemStack(Material.PAPER);
        ItemMeta quarterItemMeta = quarterItem.getItemMeta();
        quarterItemMeta.setDisplayName(plugin.getMessage("gui.quarterItem.name"));
        List<String> quarterItemLore = plugin.getMessages("gui.quarterItem.lore");
        quarterItemMeta.setLore(quarterItemLore);
        quarterItem.setItemMeta(quarterItemMeta);

        ItemStack halfItem = new ItemStack(Material.PAPER);
        ItemMeta halfItemMeta = halfItem.getItemMeta();
        halfItemMeta.setDisplayName(plugin.getMessage("gui.halfItem.name"));
        List<String> halfItemLore = plugin.getMessages("gui.halfItem.lore");
        halfItemMeta.setLore(halfItemLore);
        halfItem.setItemMeta(halfItemMeta);

        ItemStack fullItem = new ItemStack(Material.PAPER);
        ItemMeta fullItemMeta = fullItem.getItemMeta();
        fullItemMeta.setDisplayName(plugin.getMessage("gui.fullItem.name"));
        List<String> fullItemLore = plugin.getMessages("gui.fullItem.lore");
        fullItemMeta.setLore(fullItemLore);
        fullItem.setItemMeta(fullItemMeta);

        for (int i = 0; i < 27; i++) {
            gui.setItem(i, emptyItem);
        }

        if (player.hasPermission("uniopiyango.admin")) {
            gui.setItem(0, adminItem);
        }

        if (plugin.getEventStatus()) {
            gui.setItem(4, eventInfo);
        } else {
            gui.setItem(4, winnerInfo);
        }

        gui.setItem(12, quarterItem);
        gui.setItem(13, halfItem);
        gui.setItem(14, fullItem);
        gui.setItem(22, playerItem);

        return gui;
    }
}
