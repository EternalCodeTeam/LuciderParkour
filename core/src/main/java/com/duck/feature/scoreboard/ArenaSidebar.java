package com.duck.feature.scoreboard;

import com.duck.parkour.Parkour;
import com.duck.scores.ParkourScore;
import com.duck.user.User;
import com.duck.utils.ChatUtils;
import com.duck.utils.TimeUtils;
import org.apache.commons.lang.Validate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ArenaSidebar{

    private ScoreboardBuilder scoreboardBuilder;

    public void createBoard(Parkour parkour, User user){
        Validate.notNull(parkour);

        this.scoreboardBuilder = new ScoreboardBuilder("&3" +
                parkour.getName().replaceAll("_", " " )
                + " &7(&5" + parkour.getId() + "&7)",
                "parkour-board");


        int place = 1;

        List<ParkourScore> parkourScoreList = parkour.getParkourScores()
                .stream()
                .limit(user.getScoresLimit())
                .sorted(Comparator.comparing(ParkourScore::getTime))
                .collect(Collectors.toList());

        for(ParkourScore parkourScore : parkourScoreList){
            scoreboardBuilder.add(ChatUtils.color("&7" + place + ". &3" + parkourScore.getUserName() + ": &d"
            + TimeUtils.letterTimeFormat(parkourScore.getTime())));
            place++;
        }

        scoreboardBuilder.build();
        user.execute(player -> scoreboardBuilder.send(player));
    }

    public void clear(){
        scoreboardBuilder.reset();
    }

    public ScoreboardBuilder getScoreboardBuilder() {
        return scoreboardBuilder;
    }
}
