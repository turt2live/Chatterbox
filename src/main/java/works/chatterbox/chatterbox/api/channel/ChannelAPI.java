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
import java.util.concurrent.ExecutionException;

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

    public ChannelAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
    }

    @Nullable
    private Channel getDefaultChannelOrNull() {
        final List<? extends ConfigurationNode> channels = this.chatterbox.getConfiguration().getNode("channels").getChildrenList();
        return channels.isEmpty() ? null : new ConfigChannel(this.chatterbox, channels.get(0));
    }

    @NotNull
    public Collection<Channel> getAllChannels() {
        return this.channels.asMap().values();
    }

    @Nullable
    public Channel getChannel(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        try {
            return this.channels.get(name);
        } catch (final IllegalStateException | ExecutionException ex) {
            return null;
        }
    }

    @NotNull
    public Channel getDefaultChannel() {
        return Preconditions.checkNotNull(this.getDefaultChannelOrNull(), "No channels specified.");
    }
}
