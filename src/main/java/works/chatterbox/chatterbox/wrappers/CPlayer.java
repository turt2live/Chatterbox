package works.chatterbox.chatterbox.wrappers;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.channels.Channel;

import java.util.Set;
import java.util.function.Consumer;

public interface CPlayer {

    /**
     * Gets the set of channels that this player is currently joined on.
     *
     * @return Set of channels
     */
    @NotNull
    Set<Channel> getChannels();

    /**
     * Gets this player's main channel. The main channel is the channel that the player will send messages to by
     * default.
     *
     * @return Main channel
     */
    @NotNull
    Channel getMainChannel();

    /**
     * Sets this player's main channel.
     *
     * @param channel Channel
     */
    void setMainChannel(@NotNull final Channel channel);

    /**
     * Gets this player as an OfflinePlayer.
     *
     * @return OfflinePlayer
     */
    @NotNull
    OfflinePlayer getOfflinePlayer();

    /**
     * Gets this player as a Player, if online.
     *
     * @return Player
     */
    @Nullable
    Player getPlayer();

    void ifOnline(@NotNull final Consumer<Player> function);

    /**
     * Checks if this player is online.
     *
     * @return true if on the server, false if not
     */
    boolean isOnline();

    /**
     * Adds this player to the given channel.
     *
     * @param channel Channel to join
     */
    void joinChannel(@NotNull final Channel channel);

    /**
     * Removes this player from the given channel.
     *
     * @param channel Channel to leave
     */
    void leaveChannel(@NotNull final Channel channel);

}
