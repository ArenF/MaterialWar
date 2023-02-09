package com.aren.utils.IF;

import com.aren.utils.ConfigManager;
import com.aren.utils.data.ConfigFile;
import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.Label;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class NumberInputInventory {

    String filename;
    String key;

    public NumberInputInventory(String filename, String key) {
        this.filename = filename;
        this.key = key;
    }

    public ChestGui setName(ChestGui gui, String name) {
        gui.setTitle(name);
        return gui;
    }

    public ChestGui create() {
        ChestGui gui = new ChestGui(6, "숫자 입력 : ");

//        입력 종료 코드
//        String title = gui.getTitle();
//
//        if (Objects.equals(title.split(" : ")[1], "")) {
//            return;
//        }
//        int count = Integer.parseInt(title.split(" : ")[1]);
//        ConfigManager configManager = ConfigManager.getInstance();
//
//        ConfigFile configFile = configManager.getConfig(filename);
//        configFile.set(key, count);
//        event.getPlayer().sendMessage("입력을 마치었습니다.");

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(createItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.BLACK + "[]")));
        background.setRepeat(true);

        gui.addPane(background);

        Label input = new Label(3, 1, 3, 3, Font.LIGHT_GRAY);
        Label input2 = new Label(4, 4, 1, 1, Font.LIGHT_GRAY);

        input.setText("123456789", (character, item) -> new GuiItem(item, event -> {
            //player clicked on character
            setName(gui, gui.getTitle() + character).update();
        }));

        input2.setText("0", (character, item) -> new GuiItem(item, event -> {
            setName(gui, gui.getTitle() + character).update();
        }));

        gui.addPane(input);
        gui.addPane(input2);

        OutlinePane cancelPane = new OutlinePane(3, 4, 1, 1, Pane.Priority.HIGH);
        OutlinePane checkPane = new OutlinePane(5, 4, 1, 1, Pane.Priority.HIGH);

        cancelPane.addItem(new GuiItem(createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "취소"), event -> {
            event.getWhoClicked().getOpenInventory().close();
        }));

        gui.addPane(cancelPane);

        checkPane.addItem(new GuiItem(createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "확인"), event -> {
            String title = gui.getTitle();
//
            if (Objects.equals(title.split(" : ")[1], "")) {
                return;
            }
            int count = Integer.parseInt(title.split(" : ")[1]);
            ConfigManager configManager = ConfigManager.getInstance();

            ConfigFile configFile = configManager.getConfig(filename);
            configFile.set(key, count);
            event.getWhoClicked().sendMessage("입력을 마치었습니다.");
            event.getWhoClicked().getOpenInventory().close();
        }));

        gui.addPane(checkPane);

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
