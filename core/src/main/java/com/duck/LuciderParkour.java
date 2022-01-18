package com.duck;

import com.duck.commands.CategoriesCommand;
import com.duck.commands.ParkourCommand;
import com.duck.commands.PreferencesCommand;
import com.duck.commands.UserCommand;
import com.duck.configuration.ConfigurationFactory;
import com.duck.data.flat.FlatDataManager;
import com.duck.data.flat.FlatDataTransfer;
import com.duck.data.sql.SQLManager;
import com.duck.feature.gui.GUIManager;
import com.duck.feature.timer.ParticlesTimer;
import com.duck.listeners.*;
import com.duck.listeners.inventory.PlayerInventoryClickListener;
import com.duck.listeners.inventory.PlayerInventoryDropItemsListener;
import com.duck.listeners.inventory.PlayerInventoryInteractListener;
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
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.scoreboard.ScoreboardManager;
import panda.std.stream.PandaStream;

@Plugin(name = "LuciderParkour", version = "1.0.0b")
@ApiVersion(ApiVersion.Target.v1_17)
@Dependency("LuckPerms")
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


    private GUIManager guiManager;

    private BukkitFrame bukkitFrame;

    private ScoreboardManager scoreboardManager;

    private SQLManager sqlManager;

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


        this.userManager = new UserManager();
        this.userFactory = new UserFactory(userManager);

        this.parkourManager = new ParkourManager();
        this.parkourFactory = new ParkourFactory();
        this.parkourCategoryFactory = new ParkourCategoryFactory();
        this.sqlManager = new SQLManager();


        configurationFactory.initConfigs(this);

        this.flatDataTransfer = new FlatDataTransfer();

        flatDataTransfer.transferUsersIntoMemoryCache();
        flatDataTransfer.transferArenasIntoMemoryCache();

        this.scoreManager = new ScoreManager();



        flatDataTransfer.transferCategoriesIntoMemoryCache();
        this.guiManager = new GUIManager();


        this.bukkitFrame = new BukkitFrame(this);


        buildCommands();
        buildListeners();

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

    public SQLManager getSqlManager() {
        return sqlManager;
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

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }


    private void buildCommands(){
        PandaStream.of(
                new ParkourCommand(), new UserCommand(), new PreferencesCommand(), new CategoriesCommand())
                .forEach(command -> bukkitFrame.registerCommands(command));
    }

    public void buildListeners(){
        PandaStream.of(new PlayerJoinListener(),
                new PlayerMoveListener(),
                new PlayerFallDamageListener(),
                new PlayerFoodLevelChangeListener(),
                new ChatListener(),
                new DateChangeListener(),
                new PlayerInventoryInteractListener(),
                new PlayerInventoryClickListener(),
                new PlayerInventoryDropItemsListener(),
                new PlayerDamageListener(),
                new PlayerFarmLandListener())
                .forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

}
