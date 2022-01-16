package com.duck.commands;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.parkour.ParkourFactory;
import com.duck.parkour.ParkourManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import panda.std.Option;

import java.util.function.Consumer;

public class UserCommand {

    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();

    private final String userPermissionString = configurationFactory.getGeneralConfiguration().userPermission;
    private final String adminPermissionString = configurationFactory.getGeneralConfiguration().adminPermission;

    @Command(name = "users")
    public void onUserCommandResponse(Context<Player> playerContext)
    {

        String prefix = configurationFactory.getGeneralConfiguration().standardParkourPrefix;
        String validCommandUse = configurationFactory.getGeneralConfiguration().validCommandArgumentUse;
        String unknownCommandArgument = configurationFactory.getGeneralConfiguration().unknownCommandMessage;


        String firstArgument = playerContext.getArg(0);

        Consumer<Player> playerConsumer = switch(firstArgument){


            case "setusername" -> player -> {

                Option<Player> playerOption = Option.of(Bukkit.getPlayer(playerContext.getArg(1)));

                Option<User> userOption = userManager.getUser(playerOption.get().getUniqueId());


                if(userOption.isDefined()){
                    userOption.peek(u -> {
                        u.setUsername(playerContext.getArg(2));

                        u.execute(player1 -> player1.sendMessage(ChatUtils.component(prefix +
                                " &7Changed user name into &6" + playerContext.getArg(2))));
                    });
                }
            };

            case "setlevel" -> player -> {
                Option<Player> playerOption = Option.of(Bukkit.getPlayer(playerContext.getArg(1)));

                Option<User> userOption = userManager.getUser(playerOption.get().getUniqueId());


                if(userOption.isDefined()){
                    userOption.peek(u -> {
                        u.setLevel(playerContext.getArg(2, int.class));

                        player.sendMessage(ChatUtils.component(prefix +
                                " &7Changed user level into &6" + playerContext.getArg(2, int.class)));
                    });
                }
            };

            case "setxp" -> player -> {

                Option<Player> playerOption = Option.of(Bukkit.getPlayer(playerContext.getArg(1)));

                Option<User> userOption = userManager.getUser(playerOption.get().getUniqueId());


                if(userOption.isDefined()){
                    userOption.peek(u -> {
                        u.setXp(playerContext.getArg(2, double.class));

                        player.sendMessage(ChatUtils.component(prefix +
                                " &7Changed user xp into &6" + playerContext.getArg(2, double.class)));
                    });
                }
            };

            case "setcoins" -> player -> {

                Option<Player> playerOption = Option.of(Bukkit.getPlayer(playerContext.getArg(1)));

                Option<User> userOption = userManager.getUser(playerOption.get().getUniqueId());


                if(userOption.isDefined()){
                    userOption.peek(u -> {
                        u.setCoins(playerContext.getArg(2, double.class));

                        player.sendMessage(ChatUtils.component(prefix +
                                " &7Changed user coins into &6" + playerContext.getArg(2, double.class)));
                    });
                }
            };

            case "setscoreslimit" -> player -> {

                Option<Player> playerOption = Option.of(Bukkit.getPlayer(playerContext.getArg(1)));

                Option<User> userOption = userManager.getUser(playerOption.get().getUniqueId());


                if(userOption.isDefined()){
                    userOption.peek(u -> {
                        u.setScoresLimit(playerContext.getArg(2, int.class));

                        player.sendMessage(ChatUtils.component(prefix +
                                " &7Changed user scores limit on arena scoreboard into &6" + playerContext.getArg(2, int.class)));
                    });
                }
            };

            default -> throw new IllegalStateException("Unexpected value: " + firstArgument);
        };

        if(playerContext.testPermission(adminPermissionString, false))
            playerConsumer.accept(playerContext.getSender());
    }
}
