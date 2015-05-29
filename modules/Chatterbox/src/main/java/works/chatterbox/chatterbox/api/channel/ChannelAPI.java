/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.api.channel;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Files;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.channels.ConfigChannel;
import works.chatterbox.chatterbox.wrappers.UUIDCPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Channel API handles the creation and parsing of chat channels.
 */
public class ChannelAPI {

    private final Chatterbox chatterbox;
    private final LoadingCache<String, Channel> channels = CacheBuilder.newBuilder()
        .softValues()
        .build(new CacheLoader<String, Channel>() {
            @Override
            public Channel load(@NotNull final String key) throws Exception {
                return new ConfigChannel(ChannelAPI.this.chatterbox, key);
            }
        });
    private final ConfigurationNode master;
    private ConfigurationNode memberships;

    public ChannelAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.master = this.chatterbox.getConfiguration().getNode("master");
    }

    @Nullable
    private Channel getDefaultChannelOrNull() {
        final List<String> channelNames = this.getAllChannelNames();
        return channelNames.isEmpty() ? null : this.getChannelByName(channelNames.get(0));
    }

    /**
     * Adds a channel to the registry. Use this to register a custom channel.
     * <p>This will return false if a channel with the same name is already registered.
     *
     * @param name    Channel name
     * @param channel Channel associated with that name
     * @return true if registered, false if not
     */
    public boolean addChannel(@NotNull final String name, @NotNull final Channel channel) {
        Preconditions.checkNotNull(name, "name was null");
        Preconditions.checkNotNull(channel, "channel was null");
        if (this.channels.asMap().containsKey(name)) return false;
        this.channels.put(name, channel);
        return true;
    }

    /**
     * Gets all channel names defined in the currently loaded config.yml.
     *
     * @return Collection of channel names
     */
    @NotNull
    public List<String> getAllChannelNames() {
        return this.chatterbox.getConfiguration().getNode("channels").getChildrenList().stream()
            .map(node -> node.getNode("name").getString())
            .filter(name -> name != null)
            .collect(Collectors.toList());
    }

    /**
     * Gets all channel tags defined in the currently loaded config.yml.
     *
     * @return Collection of channel tags
     */
    @NotNull
    public List<String> getAllChannelTags() {
        return this.chatterbox.getConfiguration().getNode("channels").getChildrenList().stream()
            .map(node -> node.getNode("tag").getString())
            .filter(name -> name != null)
            .collect(Collectors.toList());
    }

    /**
     * Gets all channels that been loaded.
     *
     * @return Collection of channels
     */
    @NotNull
    public Collection<Channel> getAllLoadedChannels() {
        return this.channels.asMap().values();
    }

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
    public Channel getChannel(@NotNull final String nameOrTag) {
        Preconditions.checkNotNull(nameOrTag, "nameOrTag was null");
        if (this.getAllChannelNames().stream().anyMatch(name -> name.equalsIgnoreCase(nameOrTag))) {
            return this.getChannelByName(nameOrTag);
        }
        if (this.getAllChannelTags().stream().anyMatch(tag -> tag.equalsIgnoreCase(nameOrTag))) {
            return this.getChannelByTag(nameOrTag);
        }
        return null;
    }

    /**
     * Gets a channel by its name. If no channel can be found by that name, null will be returned.
     *
     * @param name Name of the channel
     * @return Channel or null
     */
    @Nullable
    public Channel getChannelByName(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        try {
            return this.channels.get(name);
        } catch (final Exception ex) {
            return null;
        }
    }

    /**
     * Gets a channel by its tag. If no channel can be found by that tag, null will be returned.
     *
     * @param tag Tag of the channel
     * @return Channel or null
     */
    @Nullable
    public Channel getChannelByTag(@NotNull final String tag) {
        Preconditions.checkNotNull(tag, "tag was null");
        final String channelName = this.chatterbox.getConfiguration().getNode("channels").getChildrenList().stream()
            .filter(node -> tag.equalsIgnoreCase(node.getNode("tag").getString()))
            .map(node -> node.getNode("name").getString())
            .filter(name -> name != null)
            .findFirst()
            .orElse(null);
        return channelName == null ? null : this.getChannelByName(channelName);
    }

    /**
     * Gets the default channel, or the first defined channel in the config.yml.
     *
     * @return Default channel
     * @throws NullPointerException If there is no default channel defined
     */
    @NotNull
    public Channel getDefaultChannel() {
        return Preconditions.checkNotNull(this.getDefaultChannelOrNull(), "No channels specified.");
    }

    /**
     * Gets the master channel configuration node. It should contain all defaults for options not specified per-channel.
     *
     * @return ConfigurationNode
     */
    @NotNull
    public ConfigurationNode getMaster() {
        return this.master;
    }

    @NotNull
    public ConfigurationNode getMemberships() {
        if (this.memberships == null) {
            final YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setFile(new File(this.chatterbox.getDataFolder(), "memberships.yml")).build();
            try {
                this.memberships = loader.load();
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.memberships;
    }

    /**
     * Removes the given channel from the registry.
     *
     * @param channel Channel to remove
     */
    public void removeChannel(@NotNull final Channel channel) {
        Preconditions.checkNotNull(channel, "channel was null");
        this.removeChannel(channel.getName());
    }

    /**
     * Removes the given channel from the registry.
     *
     * @param name Name of the channel to remove
     */
    public void removeChannel(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        this.channels.invalidate(name);
    }

    public void saveMemberships() {
        if (this.memberships == null) return;
        final File source = new File(this.chatterbox.getDataFolder(), "memberships.yml");
        if (!source.exists()) {
            try {
                Preconditions.checkState(source.createNewFile(), "Could not save memberships file");
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        final YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder()
            .setFile(source)
            .setSink(Files.asCharSink(source, StandardCharsets.UTF_8))
            .build();
        try {
            loader.save(this.memberships);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This updates the memberships for a player without saving to the disk. This is useful when a player quits, to save
     * all memberships.
     *
     * @param cp UUIDCPlayer to save memberships for
     */
    public void updateMembershipsWithoutSave(@NotNull final UUIDCPlayer cp) {
        Preconditions.checkNotNull(cp, "cp was null");
        final ConfigurationNode memberships = this.getMemberships();
        // Get a single time to use as the last seen time during this run
        final long useAsLastSeen = System.currentTimeMillis();
        // Loop through all LOADED channels (no one is in a channel that has not been loaded)
        for (final Channel channel : cp.getChannels()) {
            // Get the membership node
            final ConfigurationNode node = memberships.getNode(channel.getName());
            // Set the current time
            node.getNode(cp.getUUID().toString()).setValue(cp.getMainChannel().getName().equals(channel.getName()) ? -useAsLastSeen : useAsLastSeen);
        }
    }
}
