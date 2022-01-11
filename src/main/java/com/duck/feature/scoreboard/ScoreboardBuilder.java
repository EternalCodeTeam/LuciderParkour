package com.duck.feature.scoreboard;

import com.duck.utils.ChatUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Maciek on 2016-08-14.
 */
public class ScoreboardBuilder {

    private Scoreboard scoreboard;
    private String title;
    private Map<String, Integer> scores;
    private List<Team> teams;
    private String name;

    public ScoreboardBuilder(String title, String name) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.title = title;
        this.scores = Maps.newLinkedHashMap();
        this.teams = Lists.newArrayList();
        this.name = name;
    }

    public void blankLine() {
        this.add(" ");
    }

    public void add(final String text) {
        this.add(text, null);
    }

    public void add(String text, final Integer score) {
        Preconditions.checkArgument(text.length() < 48, "text cannot be over 48 characters in length");
        text = this.fixDuplicates(text);
        this.scores.put(text, score);
    }

    private String fixDuplicates(String text) {
        while (this.scores.containsKey(text)) {
            text = String.valueOf(text) + "§r";
        }
        if (text.length() > 48) {
            text = text.substring(0, 47);
        }
        return text;
    }

    private Map.Entry<Team, String> createTeam(final String text) {
        String result = "";
        if (text.length() <= 16) {
            return new AbstractMap.SimpleEntry<Team, String>(null, text);
        }
        final Team team = this.scoreboard.registerNewTeam("text-" + this.scoreboard.getTeams().size());
        final Iterator<String> iterator = Splitter.fixedLength(16).split(text).iterator();
        team.prefix(ChatUtils.component(iterator.next()));
        result = iterator.next();
        if (text.length() > 32) {
            team.suffix(ChatUtils.component(iterator.next()));
        }
        this.teams.add(team);
        return new AbstractMap.SimpleEntry<>(team, result);
    }

    public void build() {
        final Objective obj = this.scoreboard.registerNewObjective(name, "dummy", ChatUtils.component((this.title.length() > 32) ? this.title.substring(0, 31) : this.title));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        int index = this.scores.size();
        for (final Map.Entry<String, Integer> text : this.scores.entrySet()) {
            final int score = (text.getValue() != null) ? text.getValue() : index;
            obj.getScore(text.getKey()).setScore(score);
            --index;
        }
    }

    public void reset() {
        this.title = null;
        this.scores.clear();
        for (final Team t : this.teams) {
            t.unregister();
        }
        this.teams.clear();
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public static void destroy(final Player... players) {
        for (final Player p : players) {
            final ScoreboardBuilder pk = new ScoreboardBuilder("a", "a");
            pk.build();
            pk.send(p);
        }
    }

    public void send(final Player... players) {
        for (final Player p : players) {
            p.setScoreboard(this.scoreboard);
        }
    }
}

