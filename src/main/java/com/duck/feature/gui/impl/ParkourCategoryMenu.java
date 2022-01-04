package com.duck.feature.gui.impl;

import com.duck.LuciderParkour;
import com.duck.feature.gui.NonPagedMenu;
import com.duck.parkour.Parkour;
import com.duck.parkour.ParkourCategory;
import com.duck.parkour.ParkourManager;
import com.duck.scores.ParkourScore;
import com.duck.user.User;
import com.duck.utils.ChatUtils;
import com.duck.utils.ScoreUtils;
import com.duck.utils.TimeUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.ScrollType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.ScrollingGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.stream.PandaStream;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParkourCategoryMenu implements NonPagedMenu<LuciderParkour> {
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private ScrollingGui gui;

    private final String title;
    private final ParkourCategory parkourCategory;

    public ParkourCategoryMenu(String title, ParkourCategory category){
        this.title = title;
        this.parkourCategory = category;
    }

    @Override
    public void setup(User user) {
        this.gui = Gui.scrolling()
                .title(ChatUtils.component(title))
                .rows(6)
                .pageSize(45)
                .scrollType(ScrollType.VERTICAL)
                .create();


        List<Parkour> parkours =
                PandaStream.of(parkourCategory.getIds()).mapOpt(parkourManager::getArena)
                                .collect(Collectors.toList());

        parkours
                .forEach(parkour -> {
                    if(parkours.size() > parkours.indexOf(parkour)){

                        List<Component> totalDescription = List.of(
                                ChatUtils.component("&7Required level: &3" + parkour.getLevelRequired()),
                                ChatUtils.component("&7Xp reward: &3" + parkour.getXpReward()));

                        gui.addItem(
                                ItemBuilder
                                        .from(parkourCategory.getParkourMaterial())
                                        .enchant(Enchantment.THORNS)
                                        .name(ChatUtils.component("&7> &3" + parkour.getName()))
                                        .lore(totalDescription)
                                        .asGuiItem()
                        );
                    }
                });

        gui.setDefaultClickAction(event -> {
            Player player = (Player) event.getWhoClicked();

            if(event.getCurrentItem() != null && event.getCurrentItem().getType() == parkourCategory.getParkourMaterial()){
                parkourManager.join(parkours.get(event.getSlot()), player);
                event.setCancelled(true);
            }
        });




        gui.getFiller().fillBetweenPoints(6, 1, 6, 9,
                ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem(event -> event.setCancelled(true)));

// Previous item
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).name(ChatUtils.component("&8> &7Previous")).asGuiItem(event -> { gui.previous(); event.setCancelled(true);}));
// Next item
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).name(ChatUtils.component("&8> &7Next")).asGuiItem(event -> { gui.next(); event.setCancelled(true);}));

    }

    private List<GuiItem> getGuiItems(User user) {
        List<Parkour> parkourByCategory =
                PandaStream.of(parkourCategory.getIds())
                        .filter(integer -> parkourManager.getArena(integer) != null)
                        .map(integer -> parkourManager.getArena(integer).get())
                        .toList();


        return PandaStream.of(parkourByCategory)
                .map(parkour1 -> ItemBuilder
                        .from(Material.STONE)
                        .name(ChatUtils.component("&8> &3" + parkour1.getName().replaceAll("_", " ")))
                        .lore(
                                ChatUtils.component("&3XP: &7" + parkour1.getXpReward()),
                                ChatUtils.component("&3RequiredLevel: &7" + parkour1.getLevelRequired()),
                                ChatUtils.component("&3First score username: &7" + ScoreUtils.getFirstPlayerScore(parkour1).getName()),
                                ChatUtils.component("&3First score time: &7" + TimeUtils.letterTimeFormat(ScoreUtils.getFirstPlayerTime(parkour1)))
                        )
                        .asGuiItem(action -> {
                            user.execute(player -> parkourManager.join(parkour1, player));
                            action.setCancelled(true);
                        })
                ).toList();
    }

    @Override
    public void openInventory(User user) {
        setup(user);

        user.execute(player -> gui.open(player));
        user.execute(player -> player.sendMessage("Opened gui"));
    }

    public ScrollingGui getGui() {
        return gui;
    }

    public ParkourManager getParkourManager() {
        return parkourManager;
    }

    public String getTitle() {
        return title;
    }

    public ParkourCategory getParkourCategory() {
        return parkourCategory;
    }
}
