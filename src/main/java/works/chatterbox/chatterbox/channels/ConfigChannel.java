package works.chatterbox.chatterbox.channels;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.radius.Radius;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.Set;

public class ConfigChannel implements Channel {

    private final ConfigurationNode node;
    private final Set<CPlayer> members = Sets.newHashSet();

    public ConfigChannel(@NotNull final Chatterbox chatterbox, @NotNull final String name) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        Preconditions.checkNotNull(name, "name was null");
        this.node = chatterbox.getConfiguration().getNode("channels").getChildrenList().stream()
            .filter(node -> name.equals(node.getNode("name").getString()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No channel by the name " + name));
    }

    public ConfigChannel(@NotNull final ConfigurationNode node) {
        Preconditions.checkNotNull(node, "node was null");
        this.node = node;
    }

    @NotNull
    @Override
    public Set<CPlayer> getMembers() {
        return this.members;
    }

    @NotNull
    @Override
    public String getName() {
        return this.node.getNode("name").getString();
    }

    @Nullable
    @Override
    public Radius getRadius() {
        return this.node.getNode("radius").getValue(input -> {
            if (!(input instanceof ConfigurationNode)) return null;
            final ConfigurationNode node = (ConfigurationNode) input;
            if (!node.getNode("enabled").getBoolean()) return null;
            return new Radius(node.getNode("horizontal").getDouble(), node.getNode("vertical").getDouble());
        });
    }

    @NotNull
    @Override
    public String getTag() {
        return this.node.getNode("tag").getString();
    }
}
