package works.chatterbox.chatterbox.channels;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.files.FormatFiles;
import works.chatterbox.chatterbox.channels.radius.Radius;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.io.File;
import java.util.Set;

public class ConfigChannel implements Channel {

    private final static FormatFiles formatFiles = new FormatFiles();
    private final ConfigurationNode node;
    private final Set<CPlayer> members = Sets.newHashSet();
    private final Chatterbox chatterbox;

    public ConfigChannel(@NotNull final Chatterbox chatterbox, @NotNull final String name) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        Preconditions.checkNotNull(name, "name was null");
        this.chatterbox = chatterbox;
        this.node = chatterbox.getConfiguration().getNode("channels").getChildrenList().stream()
            .filter(node -> name.equals(node.getNode("name").getString()) || name.equals(node.getNode("tag").getString()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No channel by the name " + name));
    }

    public ConfigChannel(@NotNull final Chatterbox chatterbox, @NotNull final ConfigurationNode node) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        Preconditions.checkNotNull(node, "node was null");
        this.chatterbox = chatterbox;
        this.node = node;
    }

    /**
     * Determines the correct node under {@code format} to use for the format. If a {@code file} node is specified, this
     * will load the file and return its contents. If not, this will return the contents of the {@code text} node. If
     * both are missing, this returns null.
     *
     * @return Format or null
     */
    @Nullable
    private String determineFormat() {
        final ConfigurationNode format = this.node.getNode("format");
        if (!format.getNode("file").isVirtual()) {
            return this.getFileFormat(format);
        }
        if (!format.getNode("text").isVirtual()) {
            return this.getTextFormat(format);
        }
        return null;
    }

    /**
     * Gets the cached contents of the {@code file} node in the given node. If there is any error, null will be
     * returned.
     *
     * @param formatNode Format node
     * @return File contents or null
     */
    @Nullable
    private String getFileFormat(@NotNull final ConfigurationNode formatNode) {
        Preconditions.checkNotNull(formatNode, "formatNode was null");
        return ConfigChannel.formatFiles.getFileContents(
            new File(this.chatterbox.getDataFolder(), formatNode.getNode("file").getString())
        );
    }

    /**
     * Returns the value of the {@code text} node inside of the given node. If such a node is not present, null will be
     * returned.
     *
     * @param formatNode Format node
     * @return Node contents or null
     */
    @Nullable
    private String getTextFormat(@NotNull final ConfigurationNode formatNode) {
        Preconditions.checkNotNull(formatNode, "formatNode was null");
        return formatNode.getNode("text").getString();
    }

    @Override
    public void addMember(@NotNull final CPlayer cp) {
        Preconditions.checkNotNull(cp, "cp was null");
        if (this.members.contains(cp)) return;
        this.members.add(cp);
        cp.joinChannel(this);
    }

    @NotNull
    @Override
    public String getFormat() {
        final String format = this.determineFormat();
        return format == null ? "" : format;
    }

    /**
     * {@inheritDoc}
     * <p>Note: the set returned is immutable.
     */
    @NotNull
    @Override
    public Set<CPlayer> getMembers() {
        return ImmutableSet.copyOf(this.members);
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

    @Override
    public void removeMember(@NotNull final CPlayer cp) {
        Preconditions.checkNotNull(cp, "cp was null");
        if (!this.members.contains(cp)) return;
        this.members.remove(cp);
        cp.leaveChannel(this);
    }
}
