package com.duck.commands;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.parkour.ParkourCategory;
import com.duck.parkour.ParkourCategoryFactory;
import com.duck.parkour.ParkourManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import panda.std.Option;

import java.util.List;
import java.util.function.Consumer;

public class CategoriesCommand {

    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ParkourCategoryFactory parkourCategoryFactory = LuciderParkour.getInstance().getParkourCategoryFactory();

    private final String userPermissionString = configurationFactory.getGeneralConfiguration().userPermission;
    private final String adminPermissionString = configurationFactory.getGeneralConfiguration().adminPermission;

    @Command(name = "categories")
    public void onCategoriesCommandResponse(Context<Player> playerContext){

        String prefix = configurationFactory.getGeneralConfiguration().standardParkourPrefix;
        String validCommandUse = configurationFactory.getGeneralConfiguration().validCommandArgumentUse;
        String unknownCommandArgument = configurationFactory.getGeneralConfiguration().unknownCommandMessage;

        User user = userManager.getUser(playerContext.getSender().getUniqueId()).get();


        Consumer<Player> firstArgumentConsumer = switch(playerContext.getArg(0)){

            case "create" -> player -> {
                String categoryName = playerContext.getArg(1);

                Option<Integer> guiIndexOption = Option.of(playerContext.getArg(2, Integer.class));

                if(!categoryName.isEmpty() && !parkourManager.getParkourCategory(categoryName).isDefined()){
                    parkourCategoryFactory.createCategory(user, categoryName);

                    ParkourCategory category = parkourManager.getParkourCategory(categoryName).get();

                    if(guiIndexOption.isDefined())
                        category.setGuiIndex(guiIndexOption.get());
                }
            };

            case "remove" -> player -> {
                String categoryName = playerContext.getArg(1);

                parkourCategoryFactory.removeParkourCategory(categoryName, user);
            };


            case "addparkour" -> player ->  {
                String categoryName = playerContext.getArg(1);
                int id = playerContext.getArg(2, int.class);

                if(!categoryName.isEmpty()){
                    ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                    if(!parkourCategory.getIds().contains(id) && parkourManager.getArena(id).isDefined()){
                        parkourCategory.getIds().add(id);
                    }
                }
            };

            case "removeparkour" -> player ->  {
                String categoryName = playerContext.getArg(1);
                int id = playerContext.getArg(2, int.class);

                if(!categoryName.isEmpty()){
                    ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                    parkourCategory.getIds().remove(id);
                }
            };

            case "set" -> player -> {

                Consumer<Player> secondArgumentConsumer = switch (playerContext.getArg(1)){
                    case "slot" -> player1 -> {
                        String categoryName = playerContext.getArg(2);
                        int slot = playerContext.getArg(3, int.class);

                        if(!categoryName.isEmpty()){
                            ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                            parkourCategory.setGuiIndex(slot);
                        }
                    };
                    case "categoryitem" -> player1 -> {
                        String categoryName = playerContext.getArg(2);
                        Material handMaterial = player1.getInventory().getItemInMainHand().getType();

                        if(!categoryName.isEmpty()){
                            ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                            parkourCategory.setGuiMaterial(handMaterial);
                        }
                    };

                    case "parkouritem" -> player1 -> {
                        String categoryName = playerContext.getArg(2);
                        Material handMaterial = player1.getInventory().getItemInMainHand().getType();

                        if(!categoryName.isEmpty()){
                            ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                            parkourCategory.setParkourMaterial(handMaterial);
                        }
                    };

                    case "title" -> player1 -> {
                        String categoryName = playerContext.getArg(2);
                        String title = playerContext.getArg(3);

                        if(!categoryName.isEmpty() && !title.isEmpty()){
                            ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                            parkourCategory.setDisplayName(title);
                        }
                    };

                    case "description" -> player1 -> {
                        String categoryName = playerContext.getArg(2);
                        String someDescriptionString = playerContext.getArg(3);

                        if(!categoryName.isEmpty()){
                            ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                            String[] finalizedDescription = someDescriptionString.split(",");
                            parkourCategory.setDescription(finalizedDescription);
                        }
                    };

                    case "setreward" -> player1 -> {
                        String categoryName = playerContext.getArg(2);
                        Option<Double> xpReward = Option.of(playerContext.getArg(3, Double.class));

                        if(!categoryName.isEmpty()){
                            ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                            if(xpReward.isDefined())
                                parkourCategory.setXpReward(xpReward.get());
                        }
                    };

                    case "requiredlvl" -> player1 -> {
                        String categoryName = playerContext.getArg(2);
                        Option<Integer> requiredLevel = Option.of(playerContext.getArg(3, Integer.class));

                        if(!categoryName.isEmpty()){
                            ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                            if(requiredLevel.isDefined())
                                parkourCategory.setRequiredLevel(requiredLevel.get());
                        }
                    };

                    case "permissionrequired" -> player1 -> {
                        String categoryName = playerContext.getArg(2);
                        Option<Boolean> requiredPermission = Option.of(playerContext.getArg(3, Boolean.class));

                        if(!categoryName.isEmpty()){
                            ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                            if(requiredPermission.isDefined())
                                parkourCategory.setPermissionRequired(requiredPermission.get());
                        }
                    };

                    case "permissionname" -> player1 -> {
                        String categoryName = playerContext.getArg(2);
                        String permissionName = playerContext.getArg(3);

                        if(!categoryName.isEmpty() && !permissionName.isEmpty()){
                            ParkourCategory parkourCategory = parkourManager.getParkourCategory(categoryName).get();

                            parkourCategory.setPermissionName(permissionName);
                        }
                    };






                    default -> player1 -> player1.sendMessage(ChatUtils.component(unknownCommandArgument));
                };

                secondArgumentConsumer.accept(player);

            };


            default -> player -> player.sendMessage(ChatUtils.component(unknownCommandArgument));
        };


        if(playerContext.testPermission(adminPermissionString, false))
            firstArgumentConsumer.accept(playerContext.getSender());
    }
}
