package com.example.customannouncements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CustomAnnouncements extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("CustomAnnouncements включен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomAnnouncements выключен!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("announcemenu")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("customannouncements.menu")) {
                    openMenu(player);
                } else {
                    player.sendMessage(ChatColor.RED + "У вас нет прав!");
                }
            }
            return true;
        }
        return false;
    }

    private void openMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.AQUA + "Меню объявлений");

        inv.setItem(0, createItem(Material.PAPER, ChatColor.GREEN + "Титул: Приветствие"));
        inv.setItem(1, createItem(Material.BOOK, ChatColor.YELLOW + "ActionBar: Совет"));
        inv.setItem(2, createItem(Material.REDSTONE, ChatColor.RED + "Сообщение: Внимание!"));

        player.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("Меню объявлений")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;

            Player p = (Player) e.getWhoClicked();
            String itemName = e.getCurrentItem().getItemMeta().getDisplayName();

            if (itemName.contains("Титул")) {
                p.sendTitle(ChatColor.GOLD + "Добро пожаловать!", ChatColor.AQUA + "Удачи в игре!", 10, 70, 20);
            } else if (itemName.contains("ActionBar")) {
                p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                        new net.md_5.bungee.api.chat.TextComponent(ChatColor.YELLOW + "Используйте /sethome чтобы сохранить точку!"));
            } else if (itemName.contains("Сообщение")) {
                Bukkit.broadcastMessage(ChatColor.RED + "[ОБЪЯВЛЕНИЕ] " + ChatColor.WHITE + "Сервер скоро будет перезагружен!");
            }
            p.closeInventory();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        p.sendTitle(ChatColor.DARK_RED + "Вы умерли!", ChatColor.GRAY + "Будьте осторожнее!", 10, 60, 10);
    }
}
