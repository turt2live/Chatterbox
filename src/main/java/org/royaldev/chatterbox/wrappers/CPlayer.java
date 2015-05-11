package org.royaldev.chatterbox.wrappers;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.royaldev.chatterbox.channels.Channel;

import java.util.Set;

public interface CPlayer {

    @NotNull
    Set<Channel> getChannels();

    @NotNull
    Channel getMainChannel();

    @NotNull
    OfflinePlayer getOfflinePlayer();

    @Nullable
    Player getPlayer();

    boolean isOnline();

}
