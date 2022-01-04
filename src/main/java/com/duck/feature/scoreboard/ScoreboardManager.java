package com.duck.feature.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ScoreboardManager {

    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public void createScoreboard(FastBoard fastBoard, UUID uuid){
        if(!boards.containsKey(uuid))
            boards.put(uuid, fastBoard);
    }
}
