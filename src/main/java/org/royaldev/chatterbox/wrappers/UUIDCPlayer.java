package org.royaldev.chatterbox.wrappers;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.royaldev.chatterbox.channels.Channel;

import java.util.Set;
import java.util.UUID;

public class UUIDCPlayer implements CPlayer {

    // TODO: Implement

    private final UUID uuid;

    public UUIDCPlayer(final UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull
    @Override
    public Set<Channel> getChannels() {
        return null;
    }

    @NotNull
    @Override
    public Channel getMainChannel() {
        return null;
    }

    @NotNull
    @Override
    public OfflinePlayer getOfflinePlayer() {
        return null;
    }

    @Nullable
    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return false;
    }
}
