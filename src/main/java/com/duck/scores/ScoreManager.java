package com.duck.scores;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.parkour.Parkour;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.ChatUtils;
import com.duck.utils.TimeUtils;
import panda.std.stream.PandaStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ScoreManager {

    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();

    public void removeScore(UUID uuid, Parkour parkour){
        if(contains(uuid, parkour)){
            parkour.getParkourScores().remove(getByUUID(uuid, parkour));
        }
    }

    public void addScore(UUID uuid, long time, Parkour parkour){

        String prefix = configurationFactory.getGeneralConfiguration().standardParkourPrefix;
        User user = userManager.getUser(uuid).get();


       if(!contains(uuid, parkour)){
           parkour.getParkourScores().add(new ParkourScore(uuid, user.toPlayer().getName(), time));

           user.execute(player -> player.sendMessage(ChatUtils.component(prefix +
                   " &7Congratulations, you've completed this map with new score. Time was &b" +
                   TimeUtils.letterTimeFormat(time))));
       }
       else if(isBetterThanLast(getByUUID(uuid, parkour).getTime(), time)) {
           int scoreIndex = parkour.getParkourScores().indexOf(getByUUID(uuid, parkour));

           parkour.getParkourScores().set(scoreIndex, new ParkourScore(uuid, user.toPlayer().getName(), time));
           user.execute(player -> player.sendMessage(ChatUtils.component(prefix +
                   " &7Congratulations, you've made better time. It is &b" +
                   TimeUtils.letterTimeFormat(time))));
       }else {
            user.execute(player -> player.sendMessage(ChatUtils.component(prefix + " &7Unfortunately, your time is not better.")));
       }
    }

    public boolean contains(UUID uuid, Parkour parkour){
        return parkour.getParkourScores()
                .stream().anyMatch(parkourScore -> parkourScore.getUuid().equals(uuid));
    }

    public boolean isBetterThanLast(long lastTime, long newTime){
        return lastTime > newTime;
    }

    public ParkourScore getByUUID(UUID uuid, Parkour parkour){
        return PandaStream.of(parkour.getParkourScores()).find(parkourScore -> parkourScore.getUuid().equals(uuid)).get();
    }

}
