package com.duck.parkour;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.user.User;
import com.duck.utils.ChatUtils;
import org.bukkit.Material;
import panda.std.Option;
import panda.std.Result;

import java.util.ArrayList;

public class ParkourCategoryFactory {
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();



    public Result<ParkourCategory, String> createCategory(User user, String categoryName){
        String prefix = configurationFactory.getGeneralConfiguration()
                .standardParkourPrefix;

        Option<ParkourCategory> parkourCategoryOption = parkourManager.getParkourCategory(categoryName);

        if(!parkourCategoryOption.isEmpty()) {
            user.execute(player -> player.sendMessage(ChatUtils.component(prefix + " &7Parkour category already exist!")));

            return Result.error("Parkour category already exist.");
        }

        return Result.<ParkourCategory, String>ok(parkourCategoryOption.orElse(
                        ParkourCategory.builder()
                                .name(categoryName)
                                .description(new String[]{"Simple category..."})
                                .displayName("&7> &3New Category")
                                .guiIndex(0)
                                .guiMaterial(Material.STONE)
                                .permissionRequired(false)
                                .permissionName("")
                                .requiredLevel(1)
                                .xpReward(0)
                                .ids(new ArrayList<>())
                                .parkourMaterial(Material.STONE)
                                .build()
        ).get()).peek(
                parkourCategory -> {
                    parkourManager.addCategory(parkourCategory);

                    user.execute(player ->
                            player.sendMessage(ChatUtils.component(prefix + " &7Parkour category has been created! name= &6" + categoryName)));
                }
        );
    }

    public void removeParkourCategory(String name,  User user){
        String prefix = configurationFactory.getGeneralConfiguration()
                .standardParkourPrefix;


        Option<ParkourCategory> parkourCategories = parkourManager.getParkourCategory(name);

        if(parkourCategories.isEmpty())
            user.execute(player -> player.sendMessage(ChatUtils.component(prefix + " &7parkour is not exist.")));

        parkourCategories.peek(parkourCategory -> {
            parkourManager.removeCategory(parkourCategory);

            user.execute(player -> player.sendMessage(ChatUtils.component(prefix + " &7Parkour category has been removed! Name= &6" + parkourCategories)));
        });
    }
}
