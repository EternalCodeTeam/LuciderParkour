package com.duck.data.flat;

import com.duck.LuciderParkour;
import com.duck.configuration.ConfigurationFactory;
import com.duck.parkour.Parkour;
import com.duck.parkour.ParkourCategory;
import com.duck.parkour.ParkourManager;
import com.duck.user.User;
import com.duck.user.UserManager;
import lombok.SneakyThrows;

import java.util.Arrays;

public class FlatDataTransfer {

    private final ParkourManager parkourManager = LuciderParkour.getInstance().getParkourManager();
    private final UserManager userManager = LuciderParkour.getInstance().getUserManager();
    private final ConfigurationFactory configurationFactory = LuciderParkour.getInstance().getConfigurationFactory();
    private final FlatDataManager flatDataManager = LuciderParkour.getInstance().getFlatDataManager();


    @SneakyThrows
    public void transferCategoriesIntoMemoryCache() {
        ParkourCategory[] categories = configurationFactory
                .readObjectFromJackson(flatDataManager.getCategoriesConfigurationFile(), ParkourCategory[].class);

        if(categories != null){
            Arrays.stream(categories)
                    .filter(category -> !parkourManager.getCategoryConcurrentHashMap().containsKey(category.getName()))
                    .forEach(category -> parkourManager.getCategoryConcurrentHashMap().put(category.getName(), category));

            System.out.println("Transferred " + parkourManager.getCategoryConcurrentHashMap().size() + " categories into the cache memory!");
        }else System.out.println("Problem with config value! Probably null, so 0 categories goes to the memory.");
    }

    @SneakyThrows
    public void transferCategoriesIntoConfiguration(){
        ParkourCategory[] parkourCategories = parkourManager
                .getCategoryConcurrentHashMap()
                .values().toArray(new ParkourCategory[0]);

        configurationFactory.writeObjectToJackson(flatDataManager.getCategoriesConfigurationFile(), parkourCategories);

        System.out.println("Transferred " + parkourCategories.length + " categories into the configuration!");
    }



    @SneakyThrows
    public void transferUsersIntoMemoryCache() {
        User[] users = configurationFactory
                .readObjectFromJackson(flatDataManager.getUserConfigurationFile(), User[].class);

        if(users != null){
            Arrays.stream(users)
                    .filter(user -> !userManager.getUserConcurrentHashMap().containsKey(user.getUuid()))
                    .forEach(user -> userManager.getUserConcurrentHashMap().put(user.getUuid(), user));

            System.out.println("Transferred " + userManager.getUserConcurrentHashMap().size() + " users into the cache memory!");
        }else System.out.println("Problem with config value! Probably null, so 0 users goes to the memory.");
    }

    @SneakyThrows
    public void transferArenasIntoMemoryCache(){
        Parkour[] arenas = configurationFactory
                .readObjectFromJackson(flatDataManager.getParkourConfigurationFile(), Parkour[].class);

        if(arenas != null) {
            Arrays.stream(arenas)
                    .filter(parkour -> !parkourManager.getParkourMap().containsKey(parkour.getId()))
                    .forEach(parkour -> parkourManager.getParkourMap().put(parkour.getId(), parkour));

            System.out.println("Transferred " + parkourManager.getParkourMap().size() + " arenas into the cache memory!");
        }else System.out.println("Problem with config value! Probably null, so 0 arenas goes to the memory.");
    }

    @SneakyThrows
    public void transferUsersIntoConfiguration(){
        User[] users = userManager
                .getUserConcurrentHashMap()
                .values().toArray(new User[0]);

        configurationFactory.writeObjectToJackson(flatDataManager.getUserConfigurationFile(), users);

        System.out.println("Transferred " + users.length + " users into the configuration!");
    }

    @SneakyThrows
    public void transferArenasIntoConfiguration(){
        Parkour[] arenas = parkourManager
                .getParkourMap()
                .values().toArray(new Parkour[0]);

        configurationFactory.writeObjectToJackson(flatDataManager.getParkourConfigurationFile(), arenas);

        System.out.println("Transferred " + arenas.length + " arenas into the configuration!");
    }
}
