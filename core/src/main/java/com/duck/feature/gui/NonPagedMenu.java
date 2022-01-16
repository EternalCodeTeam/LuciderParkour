package com.duck.feature.gui;

import com.duck.user.User;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

public interface NonPagedMenu<P extends Plugin> {
    void setup(User user);

    default void openInventory(User user) {
        Validate.notNull(user);

        setup(user);
    }
}
