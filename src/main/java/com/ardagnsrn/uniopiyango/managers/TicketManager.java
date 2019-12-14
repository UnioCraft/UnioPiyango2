package com.ardagnsrn.uniopiyango.managers;

import com.ardagnsrn.uniopiyango.UnioPiyango;
import com.ardagnsrn.uniopiyango.managers.ConfigManager.Config;
import com.ardagnsrn.uniopiyango.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.*;

public class TicketManager {

    private UnioPiyango plugin;

    /*
     * Quarter Tickets between 100000-399999
     * Half Tickets between 400000-699999
     * Full Tickets between 700000-999999
     */
    private Map<String, String> tickets = new HashMap<>(); // Ticket No, Player
    private List<String> winnerTickets = new ArrayList<>();

    public TicketManager(UnioPiyango plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        tickets = new HashMap<>();

        FileConfiguration data = plugin.getConfigManager().getConfig(Config.DATA);
        for (String ticketNo : data.getKeys(false)) {
            tickets.put(ticketNo, data.getString(ticketNo));
        }

        Bukkit.getScheduler().runTaskTimer(plugin, this::saveTickets, 6000L, 6000L);
    }

    public void saveTickets() {
        FileConfiguration data = plugin.getConfigManager().getConfig(Config.DATA);
        for (String str : data.getKeys(false)) {
            data.set(str, null);
        }

        for (String ticketNo : tickets.keySet()) {
            data.set(ticketNo, tickets.get(ticketNo));
        }

        plugin.getConfigManager().saveConfig(Config.DATA);
    }

    public String getTicketOwner(String ticketNo) {
        return tickets.get(ticketNo);
    }

    public List<String> getQuarterTickets(String playerName) {
        List<String> quarterTickets = new ArrayList<>();

        for (String ticketNo : tickets.keySet()) {
            if (tickets.get(ticketNo).equals(playerName) && Integer.parseInt(ticketNo) <= 399999) {
                quarterTickets.add(ticketNo);
            }
        }

        return quarterTickets;
    }

    public List<String> getHalfTickets(String playerName) {
        List<String> halfTickets = new ArrayList<>();

        for (String ticketNo : tickets.keySet()) {
            if (tickets.get(ticketNo).equals(playerName) && Integer.parseInt(ticketNo) >= 400000 && Integer.parseInt(ticketNo) <= 699999) {
                halfTickets.add(ticketNo);
            }
        }

        return halfTickets;
    }

    public List<String> getFullTickets(String playerName) {
        List<String> fullTickets = new ArrayList<>();

        for (String ticketNo : tickets.keySet()) {
            if (tickets.get(ticketNo).equals(playerName) && Integer.parseInt(ticketNo) >= 700000) {
                fullTickets.add(ticketNo);
            }
        }

        return fullTickets;
    }

    public List<String> getAllTickets(String playerName) {
        List<String> allTickets = new ArrayList<>();

        for (String ticketNo : tickets.keySet()) {
            if (tickets.get(ticketNo).equals(playerName)) {
                allTickets.add(ticketNo);
            }
        }

        return allTickets;
    }

    public List<String> getAllTickets() {
        return new ArrayList<>(tickets.keySet());
    }

    public List<String> getAllQuarterTickets() {
        List<String> allQuarterTickets = new ArrayList<>();

        for (String ticketNo : tickets.keySet()) {
            if (Integer.parseInt(ticketNo) <= 399999) {
                allQuarterTickets.add(ticketNo);
            }
        }

        return allQuarterTickets;
    }

    public List<String> getAllHalfTickets() {
        List<String> allHalfTickets = new ArrayList<>();

        for (String ticketNo : tickets.keySet()) {
            if (Integer.parseInt(ticketNo) >= 400000 && Integer.parseInt(ticketNo) <= 699999) {
                allHalfTickets.add(ticketNo);
            }
        }

        return allHalfTickets;
    }

    public List<String> getAllFullTickets() {
        List<String> allFullTickets = new ArrayList<>();

        for (String ticketNo : tickets.keySet()) {
            if (Integer.parseInt(ticketNo) >= 700000) {
                allFullTickets.add(ticketNo);
            }
        }

        return allFullTickets;
    }

    public long getTotalMoneySpent() {
        return (getAllFullTickets().size() * 100000000L) + (getAllHalfTickets().size() * 50000000L) + (getAllQuarterTickets().size() * 25000000L);
    }

    public void sendTicketInformation(Player player) {
        player.sendMessage(plugin.getMessage("ticketInformation.theTicketsYouHaveBought"));
        player.sendMessage(plugin.getMessage("ticketInformation.tickets")
                .replaceAll("%quarterTickets%",
                        getQuarterTickets(player.getName()).toString()
                                .replace("[", "")
                                .replace("]", ""))
                .replaceAll("%halfTickets%",
                        getHalfTickets(player.getName()).toString()
                                .replace("[", "")
                                .replace("]", ""))
                .replaceAll("%fullTickets%",
                        getFullTickets(player.getName()).toString()
                                .replace("[", "")
                                .replace("]", "")));
    }

    public String getTicketInformation(String player) {
        return plugin.getMessage("ticketInformation.tickets")
                .replaceAll("%quarterTickets%",
                        getQuarterTickets(player).toString()
                                .replace("[", "")
                                .replace("]", ""))
                .replaceAll("%halfTickets%",
                        getHalfTickets(player).toString()
                                .replace("[", "")
                                .replace("]", ""))
                .replaceAll("%fullTickets%",
                        getFullTickets(player).toString()
                                .replace("[", "")
                                .replace("]", ""));
    }

    public void buyTicket(Player player, TicketType ticketType) {
        if (!plugin.getEventStatus()) {
            player.sendMessage(plugin.getMessage("buyTickets.eventIsOver"));
            return;
        }

        int moneyNeeded = 0;
        if (ticketType.equals(TicketType.QUARTER)) {
            moneyNeeded = 25000000;
        } else if (ticketType.equals(TicketType.HALF)) {
            moneyNeeded = 50000000;
        } else if (ticketType.equals(TicketType.FULL)) {
            moneyNeeded = 100000000;
        }

        if (!plugin.getEconomy().has(player, moneyNeeded)) {
            player.sendMessage(plugin.getMessage("buyTickets.notEnoughMoney"));
            return;
        }

        int ticketNo;
        Random r = new Random();

        int low = 0;
        int high = 0;

        if (ticketType.equals(TicketType.QUARTER)) {
            low = 100000;
            high = 399999;
        } else if (ticketType.equals(TicketType.HALF)) {
            low = 400000;
            high = 699999;
        } else if (ticketType.equals(TicketType.FULL)) {
            low = 700000;
            high = 999999;
        }

        ticketNo = r.nextInt(high - low) + low;

        while (true) {
            if (tickets.containsKey(String.valueOf(ticketNo))) {
                ticketNo = r.nextInt(high - low) + low;
            } else {
                break;
            }
        }

        if (plugin.getEconomy().withdrawPlayer(player, moneyNeeded).transactionSuccess()) {
            tickets.put(String.valueOf(ticketNo), player.getName());
            Utils.spawnFireworks(player.getLocation(), 1);
            if (plugin.getBroadcastBuying()) {
                Bukkit.broadcastMessage(plugin.getMessage("buyTickets.alert").replaceAll("%player%", player.getName()));
            }
            player.sendMessage(plugin.getMessage("buyTickets.success").replaceAll("%moneyNeeded%", NumberFormat.getInstance().format(moneyNeeded)).replaceAll("%ticketNo%", String.valueOf(ticketNo)));
        }
    }

    public void drawWinners() {
        plugin.getConfigManager().getConfig(Config.DATA).set("100winners", getWinners(3));
        plugin.getConfigManager().getConfig(Config.DATA).set("50winners", getWinners(5));
        plugin.getConfigManager().getConfig(Config.DATA).set("20winners", getWinners(10));
        plugin.getConfigManager().getConfig(Config.DATA).set("10winners", getWinners(20));
        plugin.getConfigManager().getConfig(Config.DATA).set("1winners", getWinners(100));

        plugin.setEventStatus(false);
        plugin.getConfig().set("eventStatus", false);
        plugin.saveConfig();
        plugin.getConfigManager().saveConfig(Config.DATA);

        //TODO Automatically put the data to website and broadcast in game.
    }

    public List<String> getWinners(int amount) {
        List<String> allTickets = new ArrayList<>(getAllTickets());
        allTickets.removeAll(winnerTickets);
        Collections.shuffle(allTickets);
        List<String> winners = allTickets.subList(0, amount);
        winnerTickets.addAll(winners);
        return winners;
    }

    public enum TicketType {
        QUARTER, HALF, FULL
    }


}
