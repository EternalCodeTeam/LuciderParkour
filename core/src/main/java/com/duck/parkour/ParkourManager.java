package com.duck.parkour;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.feature.scoreboard.ArenaSidebar;
import com.duck.feature.scoreboard.LobbySidebar;
import com.duck.feature.timer.ArenaTimer;
import com.duck.feature.timer.LobbyTimer;
import com.duck.user.User;
import com.duck.user.UserManager;
import com.duck.utils.LocationUtils;
import com.duck.utils.RandomUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.apache.commons.lang.Validate;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.stream.PandaStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ParkourManager {

    private final ConcurrentHashMap<Integer, Parkour> parkourMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ParkourCategory> categoryConcurrentHashMap = new ConcurrentHashMap<>();

    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();

    public void addArena(Parkour parkour){
        Validate.notNull(parkour, "Parkour can't be null!");

        if(!parkourMap.containsKey(parkour.getId()))
            parkourMap.put(parkour.getId(), parkour);
    }

    public void removeArena(Parkour parkour){
        Validate.notNull(parkour, "Parkour can't be null!");

        parkourMap.remove(parkour.getId());
    }



    public void addCategory(ParkourCategory parkourCategory){
        Validate.notNull(parkourCategory);

        if(!categoryConcurrentHashMap.containsKey(parkourCategory.getName()))
            categoryConcurrentHashMap.put(parkourCategory.getName(), parkourCategory);
    }

    public void removeCategory(ParkourCategory parkourCategory){
        categoryConcurrentHashMap.remove(parkourCategory.getName());
    }

    public Option<ParkourCategory> getParkourCategory(String name){ return Option.of(categoryConcurrentHashMap.get(name));}


    public Option<Parkour> getArena(int id){
        return Option.of(parkourMap.get(id));
    }

    public void join(Parkour parkour, Player player){
        Validate.notNull(parkour);
        Validate.notNull(player);

        Option<User> user = LuciderParkour.getInstance().getUserManager().getUser(player.getUniqueId());

        if(parkour.getId() > 0 && parkour.getSpawnLocation() != null && user.get().getLevel() >= parkour.getLevelRequired()){
            Sound sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MUSIC, 1f, 1f);

            player.teleport(LocationUtils.asFullLocation(parkour.getSpawnLocation(), ", "));
            player.playSound(sound);
            user.get().setActiveId(parkour.getId());
            player.setGameMode(GameMode.ADVENTURE);

            Option<ArenaTimer> arenaTimer = userManager.getTimer(user.get().getUuid());

            if(arenaTimer.isDefined()){
                arenaTimer.get().cancel();
            }


            player.setExp(0f);
            player.setLevel(0);
            sendParkourScoreboard(player);
        }


    }


    public ConcurrentHashMap<Integer, Parkour> getParkourMap() {
        return parkourMap;
    }

    public ConcurrentHashMap<String, ParkourCategory> getCategoryConcurrentHashMap() {
        return categoryConcurrentHashMap;
    }


    public List<ParkourCategory> getAllCategories(){
        return new ArrayList<>(getCategoryConcurrentHashMap().values());
    }

    public Parkour randomizeParkour(){
        return RandomUtils.getRandomElementFromList(new ArrayList<>(parkourMap
                .values()));
    }

    public ParkourCategory getBySlot(int slot){
        return PandaStream
                .of(getCategoryConcurrentHashMap().values())
                .find(parkourCategory -> parkourCategory.getGuiIndex() == slot)
                .get();
    }

    public void sendParkourScoreboard(Player player){
        ArenaSidebar arenaSidebar = new ArenaSidebar();

        Option<User> user = userManager.getUser(player.getUniqueId());

        if(user.isDefined()){
            User u = user.get();
            Parkour parkour = getArena(u.getActiveId()).get();

            arenaSidebar.createBoard(parkour, u);
        }
    }

    public void sendLobbyScoreboard(Player player){
        LobbySidebar lobbySidebar = new LobbySidebar();

        Option<User> user = userManager.getUser(player.getUniqueId());

        if(user.isDefined()){
            User u = user.get();

            lobbySidebar.createBoard(u);
        }
    }

    public void lobby(ConfigurationFactory configurationFactory, User user, Player player) {
        Location location = LocationUtils.asFullLocation(configurationFactory.getGeneralConfiguration().lobbyLocation, ", ");

        Option<ArenaTimer> arenaTimer = userManager.getTimer(player.getUniqueId());

        int activeParkour = user.getActiveId();
        if (activeParkour > 0) {
            if (arenaTimer.isDefined()) {
                if (!arenaTimer.get().isCancelled())
                    arenaTimer.get().cancel();

                userManager.removeTimer(user);
            }

            player.teleport(location);
            user.setActiveId(0);
            new LobbyTimer(1, LuciderParkour.getInstance(), user);
        }
    }
}
