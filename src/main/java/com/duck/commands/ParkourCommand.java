package com.duck.commands;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.configuration.parsers.ParsedLocation;
import com.duck.feature.gui.GUIManager;
import com.duck.feature.scoreboard.lobby.LobbyScoreboardManager;
import com.duck.feature.timer.ArenaTimer;
import com.duck.parkour.*;
import com.duck.scores.ScoreManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import com.duck.utils.LocationUtils;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import net.dzikoysk.cdn.source.Source;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import panda.std.Option;

import java.util.List;
import java.util.function.Consumer;

public class ParkourCommand {

    private final ParkourFactory parkourFactory = LuciderParkour.getInstance().getParkourFactory();
    private final ScoreManager scoreManager = LuciderParkour.getInstance().getScoreManager();
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();
    private final LobbyScoreboardManager lobbyScoreboardManager = LuciderParkour.getInstance().getLobbyScoreboardManager();
    private final GUIManager guiManager = LuciderParkour.getInstance().getGuiManager();
    private final ParkourCategoryFactory parkourCategoryFactory = LuciderParkour.getInstance().getParkourCategoryFactory();

    @Command(name = "parkour", aliases = {"pk"})
    public void onParkourCommandResponse(Context<Player> playerContext){

        Option<User> user = userManager.getUser(playerContext.getSender().getUniqueId());

        String messagePrefix = configurationFactory.getGeneralConfiguration().standardParkourPrefix;
        String validCommandUse = configurationFactory.getGeneralConfiguration().validCommandArgumentUse;
        String unknownCommandArgument = configurationFactory.getGeneralConfiguration().unknownCommandMessage;

        Consumer<Player> playerConsumer = switch(playerContext.getArg(0)){
            case "createarena" -> player -> {
                int arenaId = playerContext.getArg(1, int.class);

                parkourFactory.createArena(user.get(), arenaId);
            };

            case "removearena" -> player -> {
                int arenaId = playerContext.getArg(1, int.class);


                parkourFactory.removeArena(arenaId, user.get());
            };

            case "openGui" -> player -> {
                guiManager.getParkourMainMenu().openInventory(user.get());
            };

            case "random" -> player -> {
                Parkour randomizedParkour = parkourManager.randomizeParkour();

                parkourManager.join(randomizedParkour, player);
            };

            case "join" -> player -> {
                int arenaId = playerContext.getArg(1, int.class);

                parkourManager.getArena(arenaId)
                        .peek(parkour1 -> parkourManager.join(parkour1, player));
            };

            case "leave" -> player -> {
                Location location = LocationUtils.asFullLocation(configurationFactory.getGeneralConfiguration().lobbyLocation, ", ");

                Option<ArenaTimer> arenaTimer = userManager.getTimer(player.getUniqueId());

                int activeParkour = user.get().getActiveId();
                if(activeParkour > 0){
                    if(arenaTimer.isDefined()){
                        if(!arenaTimer.get().isCancelled())
                            arenaTimer.get().cancel();

                        userManager.removeTimer(user.get());
                    }

                    player.teleport(location);
                    user.get().setActiveId(0);
                }
            };

            case "setlobby" -> player -> {
                configurationFactory.getGeneralConfiguration().lobbyLocation = LocationUtils.fromFullLocation(player.getLocation(), ", ");

                configurationFactory.getCdn().render(configurationFactory.getGeneralConfiguration(), Source.of(LuciderParkour.getInstance().getFlatDataManager().getGeneralConfigurationFile()));


                player.sendMessage(ChatUtils.component(messagePrefix + " &7Changed lobby location."));
            };

            case "add" -> player -> {
                Consumer<Player> addConsumer = switch(playerContext.getArg(1)){
                    case "category" -> player1 -> {
                        String category = playerContext.getArg(2, String.class);

                        parkourCategoryFactory.createCategory(user.get(), category);
                    };

                    case "score" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);
                        String playerName = playerContext.getArg(3, String.class);
                        long time = playerContext.getArg(4, long.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();
                        Player player2 = Option.of(Bukkit.getPlayer(playerName)).get();

                        scoreManager.addScore(player2.getUniqueId(), time, parkour);
                    };

                    case "startblock" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        List<ParsedLocation> actualLocations = parkour.getStartLocations();

                        ParsedLocation parsedLocation = LocationUtils.parseNormalLocation(player1.getLocation());

                        if(!actualLocations.contains(parsedLocation)){
                            actualLocations.add(parsedLocation);

                            player1.sendMessage(ChatUtils.component(messagePrefix + " &7added new start block."));
                        }else player1.sendMessage(ChatUtils.component(messagePrefix + " &7Can't add start block on this location..."));
                    };

                    case "endblock" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        List<ParsedLocation> actualLocations = parkour.getEndLocations();

                        ParsedLocation parsedLocation = LocationUtils.parseNormalLocation(player1.getLocation());

                        if(!actualLocations.contains(parsedLocation)){
                            actualLocations.add(parsedLocation);
                            player1.sendMessage(ChatUtils.component(messagePrefix + " &7added new end block."));
                        }else player1.sendMessage(ChatUtils.component(messagePrefix + " &7Can't add end block on this location..."));
                    };

                    case "failblock" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        List<Material> actualFailBlocks = parkour.getFailBlocks();

                        Material material = Material.matchMaterial(playerContext.getArg(3, String.class));

                        if(!actualFailBlocks.contains(material)){
                            actualFailBlocks.add(material);
                            player1.sendMessage(ChatUtils.component(messagePrefix + " &7added new fail block."));
                        }else player1.sendMessage(ChatUtils.component(messagePrefix + " &7Can't add fail block..."));

                    };

                    default -> player1 -> player1.sendMessage(ChatUtils.component(unknownCommandArgument));
                };

                addConsumer.accept(player);
            };

            case "remove" -> player -> {
                Consumer<Player> removeConsumer = switch(playerContext.getArg(1)){

                    case "category" -> player1 -> {
                        String category = playerContext.getArg(2, String.class);

                        parkourCategoryFactory.removeParkourCategory(category, user.get());
                    };

                    case "score" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);
                        String playerName = playerContext.getArg(3, String.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();
                        Player player2 = Option.of(Bukkit.getPlayer(playerName)).get();
                        scoreManager.removeScore(user.get().getUuid(), parkour);
                    };


                    case "startblock" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        List<ParsedLocation> actualLocations = parkour.getStartLocations();

                        ParsedLocation parsedLocation = LocationUtils.parseNormalLocation(player1.getLocation());

                        if(actualLocations.contains(parsedLocation)){
                            actualLocations.remove(parsedLocation);
                            player1.sendMessage(ChatUtils.component(messagePrefix + " &7removed start block."));
                        }else player1.sendMessage(ChatUtils.component(messagePrefix + " &7Can't remove this start block on this location..."));
                    };

                    case "endblock" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        List<ParsedLocation> actualLocations = parkour.getEndLocations();

                        ParsedLocation parsedLocation = LocationUtils.parseNormalLocation(player1.getLocation());

                        if(actualLocations.contains(parsedLocation)){
                            actualLocations.remove(parsedLocation);
                            player1.sendMessage(ChatUtils.component(messagePrefix + " &7removed end block."));
                        }else player1.sendMessage(ChatUtils.component(messagePrefix + " &7Can't remove end block on this location..."));
                    };

                    case "failblock" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        List<Material> actualFailBlocks = parkour.getFailBlocks();

                        Material material = Material.matchMaterial(playerContext.getArg(3, String.class));

                        if(actualFailBlocks.contains(material)){
                            actualFailBlocks.remove(material);
                            player1.sendMessage(ChatUtils.component(messagePrefix + " &7removed fail block."));
                        }else player1.sendMessage(ChatUtils.component(messagePrefix + " &7Can't remove fail block..."));
                    };

                    default -> player1 -> player1.sendMessage(ChatUtils.component(unknownCommandArgument));
                };

                removeConsumer.accept(player);
            };

            case "set" -> player -> {
                Consumer<Player> setConsumer = switch(playerContext.getArg(1)){
                    case "name" -> player1 -> {
                        String name = playerContext.getArg(3, String.class);


                        int arenaId = playerContext.getArg(2, int.class);
                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        String previousName = parkour.getName();

                        parkour.setName(name);
                        player1.sendMessage(ChatUtils.component(messagePrefix + " &7changed name from &6" + previousName + " &7into &b" + parkour.getName()));
                    };

                    case "category" -> player1 -> {
                        String category = playerContext.getArg(3, String.class);
                        int arenaId = playerContext.getArg(2, int.class);
                        ParkourCategory parkourCategory = parkourManager.getParkourCategory(category).get();

                        if(!parkourCategory.getIds().contains(arenaId)){
                            parkourCategory.getIds().add(arenaId);
                            player1.sendMessage(ChatUtils.component(messagePrefix + " &7changed category to &6" + parkourCategory.getName()));
                        }

                    };

                    case "spawn" -> player1 -> {
                        Location spawnLocation = player1.getLocation();

                        int arenaId = playerContext.getArg(2, int.class);
                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        Block underPlayerBlock = spawnLocation.getBlock().getRelative(BlockFace.DOWN);

                        if(underPlayerBlock.getType() != Material.AIR
                                && underPlayerBlock.getType() != Material.WATER
                                && underPlayerBlock.getType() != Material.LAVA){

                            String locationString = LocationUtils.fromFullLocation(player1.getLocation(), ", ");

                            parkour.setSpawnLocation(locationString);
                            player1.sendMessage(ChatUtils.component(messagePrefix + " &7changed spawn location."));
                        }
                    };

                    case "xp" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);
                        double xp = playerContext.getArg(3, double.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        double previousXpReward = parkour.getXpReward();

                        parkour.setXpReward(xp);
                        player1.sendMessage(ChatUtils.component(messagePrefix + " &7changed xp from parkour from &6" + previousXpReward + " &7to &b" + parkour.getXpReward()));
                    };

                    case "requiredLevel" -> player1 -> {
                        int arenaId = playerContext.getArg(2, int.class);
                        int requiredLevel = playerContext.getArg(3, int.class);

                        Parkour parkour = parkourManager.getArena(arenaId).get();

                        int previousRequiredLevel = parkour.getLevelRequired();

                        parkour.setLevelRequired(requiredLevel);
                        player1.sendMessage(ChatUtils.component(messagePrefix + " &7changed required level from &6" + previousRequiredLevel + " &7into &6" + parkour.getLevelRequired()));
                    };

                    default -> player1 -> player1.sendMessage(ChatUtils.component(unknownCommandArgument));
                };

                setConsumer.accept(player);
            };

            default -> player1 -> player1.sendMessage(ChatUtils.component(unknownCommandArgument));
        };

        playerConsumer.accept(playerContext.getSender());
    }
}
