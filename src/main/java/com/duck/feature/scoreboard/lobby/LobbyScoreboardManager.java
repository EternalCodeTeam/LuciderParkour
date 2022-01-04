package com.duck.feature.scoreboard.lobby;

import com.duck.LuciderParkour;
import com.duck.feature.scoreboard.ScoreboardManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import com.duck.utils.RandomUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import panda.std.Option;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class LobbyScoreboardManager {

    private final ScoreboardManager scoreboardManager = LuciderParkour.getInstance().getScoreboardManager();
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();


    public void sendLobbyScoreboard(User user){

        UUID uuid = user.getUuid();

        Option<FastBoard> fastBoards = Option.of(scoreboardManager.getBoards().get(uuid));
        Player player = Option.of(Bukkit.getPlayer(user.getUuid())).get();

        if(fastBoards.isDefined()){
            FastBoard board= fastBoards.get();

            getUpdatedBoards(board, user);
        }else {
            FastBoard newBoard = new FastBoard(player);

            getUpdatedBoards(newBoard, user);
            scoreboardManager.getBoards().put(uuid, newBoard);
        }

    }


    public void getUpdatedBoards(FastBoard fastBoard, User user){

        Date today = new Date();
        DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
                DateFormat.DEFAULT, new Locale("pl"));
        String datetime = dtf.format(today);
        String randomFriendName = user.getFriends().size() > 0 ? RandomUtils.getRandomElementFromList(user.getFriends()).getUsername() : "Nobody";


        fastBoard.updateTitle(ChatUtils.color("       &f&lPARKOUR       "));
        fastBoard.updateLine(0, " ");
        fastBoard.updateLine(1, ChatUtils.color("&7Online Players: &b" + (long) Bukkit.getOnlinePlayers().size()));
        fastBoard.updateLine(2, "  ");
        fastBoard.updateLine(3, ChatUtils.color("&7Available friends: &3" + (long) user.getFriends().size()));
        fastBoard.updateLine(4, ChatUtils.color("&6" + randomFriendName));
        fastBoard.updateLine(5, "    ");
        fastBoard.updateLine(6, ChatUtils.color("&7Username: &3" + user.getUsername()));
        fastBoard.updateLine(7, ChatUtils.color("&7Level: &3" + user.getLevel()));
        fastBoard.updateLine(8, ChatUtils.color("&7Experience: &3" + user.getXp()));
        fastBoard.updateLine(9, ChatUtils.color("&7Coins: &3" + user.getCoins()));
        fastBoard.updateLine(10, ChatUtils.color("&7Needed xp to level up: &3" + userManager.getXpLeft(user)));
        fastBoard.updateLine(11, "        ");
        fastBoard.updateLine(12, ChatUtils.color("&f" + datetime));
    }
}
