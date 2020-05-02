package com.ardagnsrn.uniopiyango.commands;

import com.ardagnsrn.uniopiyango.UnioPiyango;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPiyango implements CommandExecutor {

    private UnioPiyango plugin;

    public CmdPiyango(UnioPiyango plugin) {
        this.plugin = plugin;
        plugin.getCommand("piyango").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (sender instanceof Player) {
                if (args[0].equalsIgnoreCase("top")) {
                    Player player = (Player) sender;
                    player.openInventory(plugin.getGuiManager().getTopTenGUI());
                    return true;
                }
                if (args[0].equalsIgnoreCase("mesaj")) {
                    if (plugin.getTicketManager().getBroadcastClosed().contains(sender.getName())) {
                        plugin.getTicketManager().getBroadcastClosed().remove(sender.getName());
                        sender.sendMessage(plugin.getMessage("command.mesajOpened"));
                    } else {
                        plugin.getTicketManager().getBroadcastClosed().add(sender.getName());
                        sender.sendMessage(plugin.getMessage("command.mesajClosed"));
                    }
                    return true;
                }
            }
            if (sender.hasPermission("uniopiyango.admin")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reload();
                    sender.sendMessage(plugin.getMessage("command.reload"));
                    return true;
                }

                if (args[0].equalsIgnoreCase("baslat")) {
                    if (plugin.getEventStatus()) {
                        sender.sendMessage(plugin.getMessage("command.baslat"));
                        //plugin.getTicketManager().drawWinners();
                        plugin.getTicketManager().fix();
                        return true;
                    }
                }

                if (args[0].equalsIgnoreCase("togglebroadcast")) {
                    if (plugin.getBroadcastBuying()) {
                        Bukkit.broadcastMessage(plugin.getMessage("command.broadcastClosed"));
                    } else {
                        Bukkit.broadcastMessage(plugin.getMessage("command.broadcastOpened"));
                    }
                    plugin.setBroadcastBuying(!plugin.getBroadcastBuying());
                    return true;
                }

                if (args[0].equalsIgnoreCase("sorgu")) {
                    if (args.length != 3) {
                        sender.sendMessage(plugin.getMessage("command.sorgu.usage"));
                    } else {
                        if (args[1].equalsIgnoreCase("bilet")) {
                            String ticketNo = args[2];
                            String owner = plugin.getTicketManager().getTicketOwner(ticketNo);

                            if (owner != null) {
                                sender.sendMessage(plugin.getMessage("command.sorgu.ticketOwner").replaceAll("%ticketNo%", ticketNo).replaceAll("%owner%", owner));
                            } else {
                                sender.sendMessage(plugin.getMessage("command.sorgu.ticketOwner").replaceAll("%ticketNo%", ticketNo));
                            }

                        }

                        if (args[1].equalsIgnoreCase("oyuncu")) {
                            String player = args[2];
                            if (plugin.getTicketManager().getAllTickets(player).size() <= 0) {
                                sender.sendMessage(plugin.getMessage("command.sorgu.playerNotFound"));
                            } else {
                                sender.sendMessage(plugin.getMessage("command.sorgu.playerTickets").replaceAll("%player%", player));
                                sender.sendMessage(plugin.getTicketManager().getTicketInformation(player));
                            }
                        }
                    }
                    return true;
                }
            }
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.openInventory(plugin.getGuiManager().getGUI(player));
        }
        return true;
    }
}