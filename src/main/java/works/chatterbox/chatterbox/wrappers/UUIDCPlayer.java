package works.chatterbox.chatterbox.wrappers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;

import java.util.Set;
import java.util.UUID;

public class UUIDCPlayer implements CPlayer {

    private final UUID uuid;
    private final Set<Channel> joinedChannels = Sets.newHashSet();
    private final Chatterbox chatterbox;
    private Channel mainChannel;

    public UUIDCPlayer(@NotNull final UUID uuid, @NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(uuid, "uuid was null");
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.uuid = uuid;
        this.chatterbox = chatterbox;
    }

    private <T> T or(@Nullable final T thing, @NotNull final T other) {
        Preconditions.checkNotNull(other, "other was null");
        return thing == null ? other : thing;
    }

    @NotNull
    @Override
    public Set<Channel> getChannels() {
        return this.joinedChannels;
    }

    @NotNull
    @Override
    public Channel getMainChannel() {
        return this.or(this.mainChannel, this.chatterbox.getAPI().getChannelAPI().getDefaultChannel());
    }

    @Override
    public void setMainChannel(@NotNull final Channel channel) {
        Preconditions.checkNotNull(channel, "channel was null");
        this.mainChannel = channel;
    }

    @NotNull
    @Override
    public OfflinePlayer getOfflinePlayer() {
        return this.chatterbox.getServer().getOfflinePlayer(this.getUUID());
    }

    @Nullable
    @Override
    public Player getPlayer() {
        return this.chatterbox.getServer().getPlayer(this.getUUID());
    }

    @Override
    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    @NotNull
    public UUID getUUID() {
        return this.uuid;
    }
}
