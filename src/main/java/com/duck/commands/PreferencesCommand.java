package com.duck.commands;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.feature.timer.TimerType;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import com.fasterxml.jackson.databind.type.PlaceholderForType;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class PreferencesCommand {

    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();
    private final String userPermissionString = configurationFactory.getGeneralConfiguration().userPermission;
    private final String adminPermissionString = configurationFactory.getGeneralConfiguration().adminPermission;

    @Command(name = "preferences")
    public void onPreferencesCommandResponse(Context<Player> playerContext){
        String prefix = configurationFactory.getGeneralConfiguration().standardParkourPrefix;
        String validCommandUse = configurationFactory.getGeneralConfiguration().validCommandArgumentUse;
        String unknownCommandArgument = configurationFactory.getGeneralConfiguration().unknownCommandMessage;


        Consumer<Player> playerConsumer = switch(playerContext.getArg(0)){


            case "scoreslimit" -> player -> {
                User user = userManager.getUser(player.getUniqueId()).get();

                int limit = playerContext.getArg(1, int.class);

                if(limit > 0 && limit <= 10){
                    user.setScoresLimit(limit);
                    user.execute(player1 -> player1.sendMessage(ChatUtils.component(prefix + " &7scores limit has been changed to &b" + limit)));
                }else user.execute(player1 -> player1.sendMessage(ChatUtils.component(validCommandUse
                        .replaceAll("%use-arg%", "&f/preferences scoreslimit <1-10>"))));
            };

            case "setscoresvisible" -> player -> {
                User user = userManager.getUser(player.getUniqueId()).get();

                boolean isVisible = user.isHighScoresScoreboardVisibility();

                user.setHighScoresScoreboardVisibility(!isVisible);

                user.execute(player1 -> player1.sendMessage(ChatUtils.component(prefix +
                        " &7 visibility of scoreboard with best parkour scores has been change to &b" + isVisible)));
            };

            case "settimertype" -> player -> {
                User user = userManager.getUser(player.getUniqueId()).get();

                String chosenTimerType = playerContext.getArg(1).toUpperCase();

                TimerType timerType = Enum.valueOf(TimerType.class, chosenTimerType);

                user.setTimerType(timerType);
            };


            default -> player -> {
                player.sendMessage(ChatUtils.component(unknownCommandArgument));
            };
        };

        if(playerContext.testPermission(userPermissionString, false))
            playerConsumer.accept(playerContext.getSender());
    }
}
