package com.ardagnsrn.uniopiyango.managers;

import com.ardagnsrn.uniopiyango.UnioPiyango;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GUIManager {

    private UnioPiyango plugin;

    private Inventory toptenGUI;
    private final int[] SLOTS = new int[]{4, 12, 14, 19, 20, 21, 22, 23, 24, 25};
    private Map<String, ItemStack> heads = new HashMap<>();

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

        if (heads.containsKey(player.getName())) {
            playerItem = heads.get(player.getName());
            playerItemMeta = (SkullMeta) playerItem.getItemMeta();
        } else {
            playerItemMeta.setOwner(player.getName());
        }

        playerItemMeta.setDisplayName(plugin.getMessage("gui.playerItem.name"));
        List<String> playerItemLore = plugin.getMessages("gui.playerItem.lore");
        playerItemLore.replaceAll(str -> str.replaceAll("%player%", player.getName())
                .replaceAll("%totalTickets%", String.valueOf(plugin.getTicketManager().getAllTickets(player.getName()).size()))
                .replaceAll("%quarterTickets%", String.valueOf(plugin.getTicketManager().getQuarterTickets(player.getName()).size()))
                .replaceAll("%halfTickets%", String.valueOf(plugin.getTicketManager().getHalfTickets(player.getName()).size()))
                .replaceAll("%fullTickets%", String.valueOf(plugin.getTicketManager().getFullTickets(player.getName()).size())));
        playerItemMeta.setLore(playerItemLore);
        playerItem.setItemMeta(playerItemMeta);
        heads.put(player.getName(), playerItem);

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

        ItemStack top10Item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta top10ItemMeta = top10Item.getItemMeta();
        top10ItemMeta.setDisplayName(plugin.getMessage("gui.top10Item.name"));
        List<String> top10ItemLore = plugin.getMessages("gui.top10Item.lore");
        top10ItemMeta.setLore(top10ItemLore);
        top10Item.setItemMeta(top10ItemMeta);

        ItemStack toggleItem = new ItemStack(Material.REDSTONE_COMPARATOR);
        ItemMeta toggleItemMeta = toggleItem.getItemMeta();
        toggleItemMeta.setDisplayName(plugin.getMessage("gui.toggleItem.name"));
        List<String> toggleItemLore = plugin.getMessages("gui.toggleItem.lore");
        toggleItemMeta.setLore(toggleItemLore);
        toggleItem.setItemMeta(toggleItemMeta);

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

        gui.setItem(8, top10Item);
        gui.setItem(12, quarterItem);
        gui.setItem(13, halfItem);
        gui.setItem(14, fullItem);
        gui.setItem(18, toggleItem);
        gui.setItem(22, playerItem);

        return gui;
    }

    public Inventory getTopTenGUI() {
        Map<String, String> tickets = new HashMap<>(plugin.getTicketManager().getTickets());
        Map<String, Long> players = new HashMap<>(); // Player, Amount of Tickets

        for (String ticketNo : tickets.keySet()) {
            String player = tickets.get(ticketNo);
            if (players.containsKey(player)) {
                players.put(player, players.get(player) + 1L);
            } else {
                players.put(player, 1L);
            }
        }

        players = players.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Create the top ten GUI if it does not exist
        if (toptenGUI == null) {
            toptenGUI = Bukkit.createInventory(null, 27, plugin.getMessage("toptenGUI.name"));
        }

        // Reset
        toptenGUI.clear();

        int i = 1;
        for (Map.Entry<String, Long> m : players.entrySet()) {
            String playerName = m.getKey();
            toptenGUI.setItem(SLOTS[i - 1], getPlayerHead(playerName, players.get(playerName), i));
            if (i++ == 10) break;
        }
        return toptenGUI;
    }

    private ItemStack getPlayerHead(String playerName, Long amount, int rank) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (playerName == null) return null;
        ItemMeta meta = skull.getItemMeta();

        if (heads.containsKey(playerName)) {
            skull = heads.get(playerName);
            meta = skull.getItemMeta();
        } else {
            SkullMeta skullMeta = (SkullMeta) meta;
            skullMeta.setOwner(playerName);
        }

        if (!Bukkit.getServer().getVersion().contains("1.7") && !Bukkit.getServer().getVersion().contains("1.8")) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }

        meta.setDisplayName(plugin.getMessage("toptenGUI.head").replaceAll("%playerName%", playerName).replaceAll("%rank%", String.valueOf(rank)));
        List<String> lore = new ArrayList<>();
        lore.add(plugin.getMessage("toptenGUI.ticketAmount"));
        lore.add("" + ChatColor.AQUA + amount);
        meta.setLore(lore);
        skull.setItemMeta(meta);

        heads.put(playerName, skull);
        return skull;
    }
}
