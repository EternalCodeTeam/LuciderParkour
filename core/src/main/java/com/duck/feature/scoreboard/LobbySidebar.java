package com.duck.feature.scoreboard;

import com.duck.LuciderParkour;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LobbySidebar {

    private ScoreboardBuilder scoreboardBuilder;
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();

    public void createBoard(User user){
        this.scoreboardBuilder = new ScoreboardBuilder("&3Lobby", "lobby-board");

        refreshUserStatistics(user);
        refreshDate();

        scoreboardBuilder.build();
        user.execute(player -> scoreboardBuilder.send(player));
    }

    private void refreshDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        scoreboardBuilder.add(dateFormat.format(cal.getTime()), -2);
    }

    private void refreshUserStatistics(User user){
        scoreboardBuilder.add(ChatUtils.color("&7> &3Level: &7" + user.getLevel()), 3);
        scoreboardBuilder.add(" ", 2);
        scoreboardBuilder.add(ChatUtils.color("&7> &3Experience: &7" + user.getXp() +  "&6/ &7" + userManager.getXpLeft(user)), 1);
        scoreboardBuilder.add("  ", 0);
        scoreboardBuilder.add(ChatUtils.color("&7> &3Username: &7" + user.getUsername()), -1);

    }

    public void clear(){
        scoreboardBuilder.reset();
    }

    public ScoreboardBuilder getScoreboardBuilder() {
        return scoreboardBuilder;
    }
}
