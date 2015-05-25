package works.chatterbox.chatterbox.wrappers;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.titles.Titles;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

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

    /**
     * Returns {@code thing} if it is not null. If it is null, {@code other} will be returned, which should not be null.
     *
     * @param thing Thing to return if it isn't null
     * @param other Thing to return if {@code thing} is null (not null)
     * @return {@code thing} or {@code other}
     */
    private <T> T or(@Nullable final T thing, @NotNull final T other) {
        Preconditions.checkNotNull(other, "other was null");
        return thing == null ? other : thing;
    }

    /**
     * {@inheritDoc}
     * <p>Note: the set returned is immutable.
     */
    @NotNull
    @Override
    public Set<Channel> getChannels() {
        return ImmutableSet.copyOf(this.joinedChannels);
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

    @NotNull
    @Override
    public Titles getTitles() {
        return this.chatterbox.getAPI().getTitleAPI().getTitles(this.getUUID());
    }

    @Override
    public void ifOnline(@NotNull final Consumer<Player> function) {
        Preconditions.checkNotNull(function, "function was null");
        final Player player = this.getPlayer();
        if (player == null) return;
        function.accept(player);
    }

    @Override
    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    @Override
    public void joinChannel(@NotNull final Channel channel) {
        Preconditions.checkNotNull(channel, "channel was null");
        if (this.joinedChannels.contains(channel)) return;
        if (this.joinedChannels.isEmpty()) {
            this.setMainChannel(channel);
        }
        this.joinedChannels.add(channel);
        channel.addMember(this);
    }

    @Override
    public void joinPreviousChannels() {
        final ConfigurationNode memberships = this.chatterbox.getAPI().getChannelAPI().getMemberships();
        for (final String channelName : this.chatterbox.getAPI().getChannelAPI().getAllChannelNames()) {
            final ConfigurationNode channelNode = memberships.getNode(channelName).getNode(this.getUUID().toString());
            // Filter out channels with memberships without the player
            if (channelNode.isVirtual()) continue;
            // Map the names to channels
            final Channel channel = this.chatterbox.getAPI().getChannelAPI().getChannelByName(channelName);
            // Remove any nulls
            if (channel == null) continue;
            // Join each channel
            this.joinChannel(channel);
            if (channelNode.getLong() < 0L) {
                // If the lastSeenTime is negative, it was the main channel
                this.setMainChannel(channel);
            }
        }
    }

    @Override
    public void leaveChannel(@NotNull final Channel channel) {
        Preconditions.checkNotNull(channel, "channel was null");
        if (!this.joinedChannels.contains(channel)) return;
        this.joinedChannels.remove(channel);
        channel.removeMember(this);
    }

    /**
     * Gets the UUID that this CPlayer represents.
     *
     * @return UUID
     */
    @NotNull
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("uuid", this.uuid)
            .toString();
    }
}
