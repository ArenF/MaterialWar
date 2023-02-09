package com.aren.utils.IF;

import com.aren.utils.ConfigManager;
import com.aren.utils.data.ConfigFile;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Mask;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemGetInventory {

    private String filename;
    private String key;

    public ItemGetInventory(String filename, String key) {
        this.filename = filename;
        this.key = key;
    }

    public ChestGui create() {
        ChestGui gui = new ChestGui(4, "기본템을 넣고 초록버튼을 눌러주십시오.");

        gui.setOnTopClick(event -> {
            if (event.getCurrentItem() != null)
                event.setCancelled(true);
        });

        OutlinePane background = new OutlinePane(0, 3, 9, 1, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(createItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.BLACK + "[]")));
        background.setRepeat(true);

        gui.addPane(background);

        Pattern pattern = new Pattern(
                "001000200"
        );

        PatternPane pane = new PatternPane(0, 3, 9, 1, Pane.Priority.HIGH, pattern);

        pane.bindItem('0', new GuiItem(createItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.BLACK + "[]")));

        pane.bindItem('2', new GuiItem(createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "확인"), clickEvent -> {
            clickEvent.setCancelled(true);
            List<ItemStack> itemStacks = new ArrayList<ItemStack>();
            for (int i = 0; i < 27; i++) {
                if (gui.getInventory().getItem(i) == null)
                    continue;
                itemStacks.add(gui.getInventory().getItem(i));
            }

            ConfigFile gameConfig = ConfigManager.getInstance().getConfig(filename);
            gameConfig.set(key, itemStacks);
            clickEvent.getWhoClicked().sendMessage(ChatColor.GREEN + "입력을 마치었습니다.");
            clickEvent.getWhoClicked().closeInventory();

        }));
        pane.bindItem('1', new GuiItem(createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "취소"), event -> {
            event.setCancelled(true);

            for (int i = 0; i < 27; i++) {
                if (gui.getInventory().getItem(i) == null)
                    continue;
                event.getWhoClicked().getInventory().addItem(gui.getInventory().getItem(i));
            }

            event.getWhoClicked().sendMessage(ChatColor.RED + "입력을 취소하였습니다.");
            event.getWhoClicked().closeInventory();

        }));


        gui.addPane(pane);

        return gui;
    }

    public ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }
}
