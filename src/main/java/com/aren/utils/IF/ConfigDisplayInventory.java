package com.aren.utils.IF;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class ConfigDisplayInventory {

    public ConfigDisplayInventory() {}

    public ChestGui create() {
        ChestGui gui = new ChestGui(3, "자원전쟁 콘피그");

        gui.setOnGlobalClick(event -> {
            event.setCancelled(true);
        });

        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        gui.addPane(background);

        OutlinePane navPane = new OutlinePane(3, 1, 3, 1);

        ItemStack skillSelect = new ItemStack(Material.DIAMOND_ORE);
        ItemMeta skillMeta = skillSelect.getItemMeta();
        skillMeta.setDisplayName(ChatColor.BLUE + "스킬 설정");
        skillSelect.setItemMeta(skillMeta);

        ItemStack gameSelect = new ItemStack(Material.CLOCK);
        ItemMeta gameMeta = gameSelect.getItemMeta();
        gameMeta.setDisplayName(ChatColor.BLUE + "게임 설정");
        gameSelect.setItemMeta(gameMeta);

        navPane.addItem(new GuiItem(skillSelect, event -> {
            createGameConfigInventory().show(event.getWhoClicked());
        }));
        navPane.addItem(new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        navPane.addItem(new GuiItem(gameSelect));

        gui.addPane(navPane);

        return gui;
    }

    private ChestGui createGameConfigInventory() {
        ChestGui gui = new ChestGui(3, "자원전쟁 콘피그");

        gui.setOnGlobalClick(event -> {
            event.setCancelled(true);
        });

        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        gui.addPane(background);

        OutlinePane navPane = new OutlinePane(3, 1, 3, 1);

        navPane.addItem(new GuiItem(createItem(Material.CLOCK, "게임 시간 설정"), event -> {
            NumberInputInventory inv = new NumberInputInventory("GameConfig", "game.time");
            inv.create().show(event.getWhoClicked());
        }));
        navPane.addItem(new GuiItem(createItem(Material.STONE_PICKAXE, "기본템 지급"), event -> {
            ItemGetInventory inv = new ItemGetInventory("GameConfig", "game.default_item");
            inv.create().show(event.getWhoClicked());
        }));
        navPane.addItem(new GuiItem(createItem(Material.BARRIER, "무적 시간 설정"), event -> {
            NumberInputInventory inv = new NumberInputInventory("GameConfig", "game.invincible_duration");
            inv.create().show(event.getWhoClicked());
        }));

        gui.addPane(navPane);

        return gui;
    }

    public ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }

    public void show(ChestGui gui, Player player) {
        gui.show(player);
    }
}
