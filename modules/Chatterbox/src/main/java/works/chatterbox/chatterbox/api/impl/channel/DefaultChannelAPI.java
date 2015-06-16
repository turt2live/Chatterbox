/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.api.impl.channel;

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
import works.chatterbox.chatterbox.api.ChannelAPI;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.channels.ConfigChannel;
import works.chatterbox.chatterbox.events.channels.ChannelCreateEvent;
import works.chatterbox.chatterbox.wrappers.UUIDCPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Channel API handles the creation and parsing of chat channels.
 */
public class DefaultChannelAPI implements ChannelAPI {

    private final Chatterbox chatterbox;
    private final LoadingCache<String, Channel> channels = CacheBuilder.newBuilder()
        .softValues()
        .build(new CacheLoader<String, Channel>() {
            @Override
            public Channel load(@NotNull final String key) throws Exception {
                final Channel def = new ConfigChannel(DefaultChannelAPI.this.chatterbox, key);
                final ChannelCreateEvent createEvent = new ChannelCreateEvent(def);
                DefaultChannelAPI.this.chatterbox.getServer().getPluginManager().callEvent(createEvent);
                return createEvent.getChannel();
            }
        });
    private final ConfigurationNode master;
    private ConfigurationNode memberships;

    public DefaultChannelAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.master = this.chatterbox.getConfiguration().getNode("master");
    }

    @Nullable
    private Channel getDefaultChannelOrNull() {
        final List<String> channelNames = this.getAllDefinedChannelNames();
        return channelNames.isEmpty() ? null : this.getChannelByName(channelNames.get(0));
    }

    @Nullable
    private String getDefinedChannelNameByTag(@NotNull final String tag) {
        Preconditions.checkNotNull(tag, "tag was null");
        return this.chatterbox.getConfiguration().getNode("channels").getChildrenList().stream()
            .filter(node -> tag.equalsIgnoreCase(node.getNode("tag").getString()))
            .map(node -> node.getNode("name").getString())
            .filter(name -> name != null)
            .findFirst()
            .orElse(null);
    }

    @Nullable
    private String getLoadedChannelNameByTag(@NotNull final String tag) {
        Preconditions.checkNotNull(tag, "tag was null");
        return this.channels.asMap().entrySet().stream()
            .filter(entry -> entry.getValue().getTag().equalsIgnoreCase(tag))
            .map(Entry::getKey)
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean addChannel(@NotNull final String name, @NotNull final Channel channel) {
        Preconditions.checkNotNull(name, "name was null");
        Preconditions.checkNotNull(channel, "channel was null");
        if (this.channels.asMap().containsKey(name)) return false;
        this.channels.put(name, channel);
        return true;
    }

    @Override
    @NotNull
    public Set<String> getAllChannelNames() {
        return Stream.concat(
            this.getAllDefinedChannelNames().stream(),
            this.channels.asMap().values().stream().map(Channel::getName)
        )
            .filter(name -> name != null)
            .collect(Collectors.toSet());
    }

    @Override
    @NotNull
    public Set<String> getAllChannelTags() {
        return Stream.concat(
            this.getAllDefinedChannelTags().stream(),
            this.channels.asMap().values().stream().map(Channel::getTag)
        )
            .filter(name -> name != null)
            .collect(Collectors.toSet());
    }

    @Override
    @NotNull
    public List<String> getAllDefinedChannelNames() {
        return this.chatterbox.getConfiguration().getNode("channels").getChildrenList().stream()
            .map(node -> node.getNode("name").getString())
            .filter(name -> name != null)
            .collect(Collectors.toList());
    }

    @Override
    @NotNull
    public List<String> getAllDefinedChannelTags() {
        return this.chatterbox.getConfiguration().getNode("channels").getChildrenList().stream()
            .map(node -> node.getNode("tag").getString())
            .filter(name -> name != null)
            .collect(Collectors.toList());
    }

    @Override
    @NotNull
    public Collection<Channel> getAllLoadedChannels() {
        return this.channels.asMap().values();
    }

    @Override
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

    @Override
    @Nullable
    public Channel getChannelByName(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        try {
            return this.channels.get(name);
        } catch (final Exception ex) {
            return null;
        }
    }

    @Override
    @Nullable
    public Channel getChannelByTag(@NotNull final String tag) {
        Preconditions.checkNotNull(tag, "tag was null");
        // First try defined channels. If null, try loaded channels.
        final String channelName = Optional.ofNullable(this.getDefinedChannelNameByTag(tag))
            // Use #orElseGet so we don't iterate unless necessary
            .orElseGet(() -> this.getLoadedChannelNameByTag(tag));
        // If no channel name, return null, otherwise get the channel by its name
        return channelName == null ? null : this.getChannelByName(channelName);
    }

    @Override
    @NotNull
    public Channel getDefaultChannel() {
        return Preconditions.checkNotNull(this.getDefaultChannelOrNull(), "No channels specified.");
    }

    @Override
    @NotNull
    public ConfigurationNode getMaster() {
        return this.master;
    }

    @Override
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

    @Override
    public void removeChannel(@NotNull final Channel channel) {
        Preconditions.checkNotNull(channel, "channel was null");
        this.removeChannel(channel.getName());
    }

    @Override
    public void removeChannel(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        this.channels.invalidate(name);
    }

    @Override
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

    @Override
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
