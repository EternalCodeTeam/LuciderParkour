package com.duck.feature.scoreboard.arena;

import com.duck.LuciderParkour;
import com.duck.feature.scoreboard.ScoreboardManager;
import com.duck.parkour.Parkour;
import com.duck.scores.ParkourScore;
import com.duck.user.User;
import com.duck.utils.ChatUtils;
import com.duck.utils.TimeUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import panda.std.Option;

import java.util.*;

public class ArenaScoreboardManager {

    private final ScoreboardManager scoreboardManager = LuciderParkour.getInstance().getScoreboardManager();

    public void sendArenaScoreboard(Parkour parkour, User user) {

        UUID uuid = user.getUuid();

        Option<FastBoard> fastBoards = Option.of(scoreboardManager.getBoards().get(uuid));



        String scoreboardTitle = "       &b" + parkour.getName() + "&7(&6" + parkour.getId() + "&7)       ";
        Player player = Option.of(Bukkit.getPlayer(user.getUuid())).get();


        if(fastBoards.isDefined()){
            FastBoard board= fastBoards.get();

            getUpdatedBoards(board, parkour, user, scoreboardTitle);
        }else {
            FastBoard newBoard = new FastBoard(player);

            getUpdatedBoards(newBoard, parkour, user, scoreboardTitle);
            scoreboardManager.getBoards().put(uuid, newBoard);
        }
    }

    public void getUpdatedBoards(FastBoard newBoard, Parkour parkour, User user, String title){

        Player player = Option.of(Bukkit.getPlayer(user.getUuid())).get();




        newBoard.updateTitle(ChatUtils.color(title.replaceAll("_", " ")));


        List<ParkourScore> scoreList = parkour.getParkourScores()
                .stream()
                .sorted(Comparator.comparingLong(ParkourScore::getTime))
                .limit(user.getScoresLimit())
                .toList();

        newBoard.getLines().clear();

        if (user.getScoresLimit() > 0 && user.getScoresLimit() <= 10) {

            int i = 0;

            if(scoreList.size() > 0){
                for(ParkourScore parkourScore : scoreList){

                    if (i > user.getScoresLimit() - 1) {
                        break;
                    }

                    Option<Player> scoredPlayer = Option.of(Bukkit.getPlayer(parkourScore.getUuid()));

                    if(scoredPlayer.isDefined()) {
                        int currentPlace = i + 1;
                        newBoard.updateLine(i, ChatUtils.color("&8" + currentPlace + ". &7" + scoredPlayer.get().getName() + ": &3" + TimeUtils.letterTimeFormat(parkourScore.getTime())));
                        i++;
                    }
                }
            }else {
                newBoard.updateLine(0, ChatUtils.color("&3No scores yet..."));
            }
        }

    }

}
