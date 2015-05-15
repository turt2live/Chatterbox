package works.chatterbox.chatterbox.api.channel;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.channels.ConfigChannel;

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
}
