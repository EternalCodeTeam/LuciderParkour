package com.duck.user;

import com.duck.feature.timer.TimerType;
import org.bukkit.Bukkit;
import panda.std.Option;
import panda.std.Result;

import java.util.UUID;

public class UserFactory {

    private final UserManager userManager;


    public UserFactory(UserManager userManager){
        this.userManager = userManager;
    }


    public Result<User, User> createUserOrGet(UUID uuid){
        return userManager.getUser(uuid)
                .map(user -> Result.<User, User>error(userManager.getUser(uuid).get()))
                .orElseGet(Result.ok(
                        User.builder()
                                .uuid(uuid)
                                .username(
                                        Option.of(Bukkit.getPlayer(uuid))
                                                .get().getName()
                                )
                                .coins(0)
                                .level(1)
                                .xp(0)
                                .activeId(0)
                                .scoresLimit(10)
                                .timerType(TimerType.EXP_BAR)
                                .build()
                        )
                )
                .peek(userManager::addUser);
    }


    public UserManager getUserManager() {
        return userManager;
    }
}
