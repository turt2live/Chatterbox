package org.royaldev.chatterbox;

import org.bukkit.plugin.java.JavaPlugin;
import org.royaldev.chatterbox.commands.ReflectiveCommandRegistrar;

public class Chatterbox extends JavaPlugin {

    @Override
    public void onEnable() {
        final ReflectiveCommandRegistrar<Chatterbox> rcr = new ReflectiveCommandRegistrar<>(this);
        rcr.registerCommands();
    }
}
