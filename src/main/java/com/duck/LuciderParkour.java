package com.duck;

import com.duck.commands.ParkourCommand;
import com.duck.commands.PreferencesCommand;
import com.duck.commands.UserCommand;
import com.duck.configuration.ConfigurationFactory;
import com.duck.data.flat.FlatDataManager;
import com.duck.data.flat.FlatDataTransfer;
import com.duck.feature.gui.GUIManager;
import com.duck.feature.scoreboard.ScoreboardManager;
import com.duck.feature.scoreboard.arena.ArenaScoreboardManager;
import com.duck.feature.scoreboard.lobby.LobbyScoreboardManager;
import com.duck.feature.timer.ParticlesTimer;
import com.duck.listeners.*;
import com.duck.parkour.ParkourCategoryFactory;
import com.duck.parkour.ParkourFactory;
import com.duck.parkour.ParkourManager;
import com.duck.scores.ScoreManager;
import com.duck.user.UserFactory;
import com.duck.user.UserManager;
import lombok.SneakyThrows;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import panda.std.stream.PandaStream;

@Plugin(name = "LuciderParkour", version = "1.0.0b")
@ApiVersion(ApiVersion.Target.v1_17)
public class LuciderParkour extends JavaPlugin {

    private static LuciderParkour instance;

    private ParkourManager parkourManager;
    private ParkourFactory parkourFactory;
    private ParkourCategoryFactory parkourCategoryFactory;

    private ScoreManager scoreManager;

    private FlatDataManager flatDataManager;
    private FlatDataTransfer flatDataTransfer;

    private ConfigurationFactory configurationFactory;

    private UserManager userManager;
    private UserFactory userFactory;

    private ArenaScoreboardManager arenaScoreboardManager;
    private LobbyScoreboardManager lobbyScoreboardManager;
    private ScoreboardManager scoreboardManager;

    private GUIManager guiManager;

    private BukkitFrame bukkitFrame;

    @Override
    public void onDisable() {
        flatDataTransfer.transferUsersIntoConfiguration();
        flatDataTransfer.transferArenasIntoConfiguration();
        flatDataTransfer.transferCategoriesIntoConfiguration();
    }

    @Override
    public void onLoad() {
    }


    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        this.flatDataManager = new FlatDataManager();

        this.configurationFactory = new ConfigurationFactory();

        this.scoreboardManager = new ScoreboardManager();
        this.arenaScoreboardManager = new ArenaScoreboardManager();

        this.userManager = new UserManager();
        this.userFactory = new UserFactory(userManager);

        this.parkourManager = new ParkourManager();
        this.parkourFactory = new ParkourFactory();
        this.parkourCategoryFactory = new ParkourCategoryFactory();

        this.lobbyScoreboardManager = new LobbyScoreboardManager();

        configurationFactory.initConfigs(this);

        this.flatDataTransfer = new FlatDataTransfer();

        flatDataTransfer.transferUsersIntoMemoryCache();
        flatDataTransfer.transferArenasIntoMemoryCache();

        this.scoreManager = new ScoreManager();


        buildListeners();
        flatDataTransfer.transferCategoriesIntoMemoryCache();
        this.guiManager = new GUIManager();

        this.bukkitFrame = new BukkitFrame(this);
        buildCommands();

        new ParticlesTimer(20L, this);
    }

    public static LuciderParkour getInstance() {
        return instance;
    }

    public ParkourManager getParkourManager() {
        return parkourManager;
    }

    public ParkourFactory getParkourFactory() {
        return parkourFactory;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public FlatDataTransfer getFlatDataTransfer() {
        return flatDataTransfer;
    }

    public UserFactory getUserFactory() {
        return userFactory;
    }

    public LobbyScoreboardManager getLobbyScoreboardManager() {
        return lobbyScoreboardManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public BukkitFrame getBukkitFrame() {
        return bukkitFrame;
    }

    public FlatDataManager getFlatDataManager() {
        return flatDataManager;
    }

    public ConfigurationFactory getConfigurationFactory() {
        return configurationFactory;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public ParkourCategoryFactory getParkourCategoryFactory() {
        return parkourCategoryFactory;
    }

    private void buildCommands(){
        PandaStream.of(
                new ParkourCommand(), new UserCommand(), new PreferencesCommand())
                .forEach(command -> bukkitFrame.registerCommands(command));
    }

    public void buildListeners(){
        PandaStream.of(new PlayerJoinListener(),
                        new PlayerMoveListener(),
                        new PlayerFallDamageListener(),
                        new PlayerFoodLevelChangeListener(),
                        new ChatListener(),
                        new DateChangeListener())
                .forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public ArenaScoreboardManager getArenaScoreboardManager() {
        return arenaScoreboardManager;
    }
}
