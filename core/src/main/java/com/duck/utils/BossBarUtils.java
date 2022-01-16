package com.duck.utils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

public final class BossBarUtils {


    public static BossBar create(String message, BossBar.Color color, BossBar.Overlay overlay, float progress){
        final Component messageToComponent = ChatUtils.component(message);

        return BossBar.bossBar(messageToComponent, progress, color, overlay);
    }

    public static void send(Audience audience, BossBar bossBar){
        Validate.notNull(audience);
        Validate.notNull(bossBar);

        audience.showBossBar(bossBar);
    }

    public static void send(Player player, BossBar bossBar){
        Validate.notNull(player);
        Validate.notNull(bossBar);

        player.showBossBar(bossBar);
    }


    public static void hideBossBar(Audience audience, BossBar bossBar){
        Validate.notNull(audience);
        Validate.notNull(bossBar);

        audience.hideBossBar(bossBar);
    }

    public static void hideBossBar(Player player, BossBar bossBar){
        Validate.notNull(player);
        Validate.notNull(bossBar);

        player.hideBossBar(bossBar);
    }

    public static void showBossBar(Audience audience, BossBar bossBar){
        Validate.notNull(audience);
        Validate.notNull(bossBar);

        audience.showBossBar(bossBar);
    }

    public static void showBossBar(Player player, BossBar bossBar){
        Validate.notNull(player);
        Validate.notNull(bossBar);

        player.showBossBar(bossBar);
    }
}
