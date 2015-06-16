package works.chatterbox.chatterbox.api;

import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.wrappers.UUIDCPlayer;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ChannelAPI {

    /**
     * Adds a channel to the registry. Use this to register a custom channel.
     * <p>This will return false if a channel with the same name is already registered.
     *
     * @param name    Channel name
     * @param channel Channel associated with that name
     * @return true if registered, false if not
     */
    boolean addChannel(@NotNull String name, @NotNull Channel channel);

    /**
     * Gets all channel names currently defined ({@link #getAllDefinedChannelNames()} in the config.yml and all channel
     * names representative of all currently-loaded channels. This is returned in a set to prevent duplicates.
     *
     * @return Collection of channel names
     */
    @NotNull
    Set<String> getAllChannelNames();

    /**
     * Gets all channel tags currently defined ({@link #getAllDefinedChannelTags()} in the config.yml and all channel
     * tags representative of all currently-loaded channels. This is returned in a set to prevent duplicates.
     *
     * @return Collection of channel tags
     */
    @NotNull
    Set<String> getAllChannelTags();

    /**
     * Gets all channel names defined in the currently loaded config.yml.
     *
     * @return Collection of channel names
     */
    @NotNull
    List<String> getAllDefinedChannelNames();

    /**
     * Gets all channel tags defined in the currently loaded config.yml.
     *
     * @return Collection of channel tags
     */
    @NotNull
    List<String> getAllDefinedChannelTags();

    /**
     * Gets all channels that have been loaded.
     *
     * @return Collection of channels
     */
    @NotNull
    Collection<Channel> getAllLoadedChannels();

    /**
     * Gets a channel by its name or by its tag. This will check names before tags. It is <strong>much</strong> more
     * efficient to get a channel by either its name or tag, if possible. This method loops through all names and all
     * tags in an effort to find a name or tag that matches, then calls the appropriate method to get the channel.
     *
     * @param nameOrTag Either the name or the tag of the channel
     * @return Channel or null if none matched
     * @see #getChannelByName(String)
     * @see #getChannelByTag(String)
     */
    @Nullable
    Channel getChannel(@NotNull String nameOrTag);

    /**
     * Gets a channel by its name. If no channel can be found by that name, null will be returned.
     *
     * @param name Name of the channel
     * @return Channel or null
     */
    @Nullable
    Channel getChannelByName(@NotNull String name);

    /**
     * Gets a channel by its tag. If no channel can be found by that tag, null will be returned.
     *
     * @param tag Tag of the channel
     * @return Channel or null
     */
    @Nullable
    Channel getChannelByTag(@NotNull String tag);

    /**
     * Gets the default channel, or the first defined channel in the config.yml.
     *
     * @return Default channel
     * @throws NullPointerException If there is no default channel defined
     */
    @NotNull
    Channel getDefaultChannel();

    /**
     * Gets the master channel configuration node. It should contain all defaults for options not specified per-channel.
     *
     * @return ConfigurationNode
     */
    @NotNull
    ConfigurationNode getMaster();

    @NotNull
    ConfigurationNode getMemberships();

    /**
     * Removes the given channel from the registry.
     *
     * @param channel Channel to remove
     */
    void removeChannel(@NotNull Channel channel);

    /**
     * Removes the given channel from the registry.
     *
     * @param name Name of the channel to remove
     */
    void removeChannel(@NotNull String name);

    void saveMemberships();

    /**
     * This updates the memberships for a player without saving to the disk. This is useful when a player quits, to save
     * all memberships.
     *
     * @param cp UUIDCPlayer to save memberships for
     */
    void updateMembershipsWithoutSave(@NotNull UUIDCPlayer cp);
}
