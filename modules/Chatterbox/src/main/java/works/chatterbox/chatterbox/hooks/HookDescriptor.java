package works.chatterbox.chatterbox.hooks;

import com.google.common.base.Preconditions;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HookDescriptor {

    private final ConfigurationNode node;

    public HookDescriptor(final ConfigurationNode node) {
        this.node = node;
    }

    @Nullable
    private String getString(@NotNull final String node) {
        Preconditions.checkNotNull(node, "node was null");
        return this.getNode().getNode(node).getString();
    }

    @Nullable
    public String getAuthor() {
        return this.getString("author");
    }

    @NotNull
    public String getMain() {
        return Preconditions.checkNotNull(this.getString("main"));
    }

    @NotNull
    public String getName() {
        return Preconditions.checkNotNull(this.getString("name"));
    }

    public ConfigurationNode getNode() {
        return this.node;
    }


}
