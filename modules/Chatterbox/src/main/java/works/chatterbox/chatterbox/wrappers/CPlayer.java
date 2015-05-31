/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.wrappers;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.titles.Titles;

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

    /**
     * Gets any titles associated with this player.
     *
     * @return Titles
     */
    @NotNull
    Titles getTitles();

    /**
     * Runs the given consumer with the Player object that this CPlayer represents, if and only if {@link #isOnline()}
     * returns true.
     *
     * @param function Consumer to run
     */
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
     * @return If the channel was joined
     */
    boolean joinChannel(@NotNull final Channel channel);

    /**
     * Joins channels contained in the memberships database.
     */
    void joinPreviousChannels();

    /**
     * Removes this player from the given channel.
     *
     * @param channel Channel to leave
     * @return If the channel was left
     */
    boolean leaveChannel(@NotNull final Channel channel);

}
