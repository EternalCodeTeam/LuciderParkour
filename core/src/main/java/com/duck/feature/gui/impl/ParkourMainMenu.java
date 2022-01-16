package com.duck.feature.gui.impl;

import com.duck.LuciderParkour;
import com.duck.feature.gui.GUIManager;
import com.duck.feature.gui.NonPagedMenu;
import com.duck.parkour.ParkourCategory;
import com.duck.parkour.ParkourManager;
import com.duck.user.User;
import com.duck.utils.ChatUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import org.bukkit.enchantments.Enchantment;

public class ParkourMainMenu implements NonPagedMenu<LuciderParkour> {

    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private Gui gui;
    private ParkourCategoryMenu parkourCategoryMenu;
    private final GUIManager guiManager = LuciderParkour.getInstance().getGuiManager();

    private final String title;

    public ParkourMainMenu(String title){
        this.title = title;
    }



    @Override
    public void setup(User user) {

        this.gui = Gui.gui()
                .title(ChatUtils.component(title))
                .rows(6)
                .create();


        parkourManager.getAllCategories()
                        .forEach(parkourCategory -> {
                            gui.setItem(parkourCategory.getGuiIndex(),
                                    ItemBuilder.from(parkourCategory.getGuiMaterial())
                                            .name(ChatUtils.component(parkourCategory.getDisplayName().replace("_", " ")))
                                            .lore(
                                                    ChatUtils.component("&7XP: &3" + parkourCategory.getXpReward()),
                                                    ChatUtils.component("&7Required Level: &3" + parkourCategory.getRequiredLevel())
                                            )
                                            .enchant(Enchantment.THORNS)
                                            .asGuiItem());
                        });

        gui.setDefaultClickAction(event -> {
            if(event.getWhoClicked().getOpenInventory().getTopInventory().equals(gui.getInventory())){
                ParkourCategory parkourCategory = parkourManager.getBySlot(event.getSlot());

                this.parkourCategoryMenu = new ParkourCategoryMenu(ChatUtils.color(
                        parkourCategory.getDisplayName()
                ), parkourCategory);

                parkourCategoryMenu.openInventory(user);
            }
        });

    }

    @Override
    public void openInventory(User user) {
        setup(user);

        user.execute(player -> gui.open(player));
        user.execute(player -> player.sendMessage("Opened gui"));
    }

    public Gui getGui() {
        return gui;
    }
}
