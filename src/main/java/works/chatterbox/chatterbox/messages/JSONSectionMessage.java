package works.chatterbox.chatterbox.messages;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.channels.radius.Radius;
import works.chatterbox.chatterbox.channels.worlds.WorldRecipients;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.Set;

public class JSONSectionMessage implements Message {

    private final Message base;
    private String section;
    private boolean cancelled;

    public JSONSectionMessage(@NotNull final Message base, @NotNull final String section) {
        Preconditions.checkNotNull(base, "base was null");
        Preconditions.checkNotNull(section, "section was null");
        this.base = base;
        this.section = section;
    }

    @NotNull
    @Override
    public Channel getChannel() {
        return new JSONChannel();
    }

    @Override
    public void setChannel(@NotNull final Channel channel) {
        // no-op
    }

    @NotNull
    @Override
    public String getFormat() {
        return this.section;
    }

    @Override
    public void setFormat(@NotNull final String format) {
        Preconditions.checkNotNull(format, "format was null");
        this.section = format;
    }

    @NotNull
    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public void setMessage(@NotNull final String message) {
        // no-op
    }

    @NotNull
    @Override
    public Set<Player> getRecipients() {
        return Sets.newHashSet();
    }

    @NotNull
    @Override
    public CPlayer getSender() {
        return this.base.getSender();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public class JSONChannel implements Channel {

        @Nullable
        @Override
        public String getJSONSection(@NotNull final String sectionName) {
            return null;
        }

        @Override
        public void addMember(@NotNull final CPlayer cp) {

        }

        @NotNull
        @Override
        public String getFormat() {
            return JSONSectionMessage.this.getFormat();
        }

        @NotNull
        @Override
        public Set<CPlayer> getMembers() {
            return Sets.newHashSet();
        }

        @NotNull
        @Override
        public String getName() {
            return "json";
        }

        @Nullable
        @Override
        public Radius getRadius() {
            return null;
        }

        @NotNull
        @Override
        public String getTag() {
            return "json";
        }

        @NotNull
        @Override
        public WorldRecipients getWorldRecipients() {
            return new WorldRecipients(Maps.newHashMap(), false, false);
        }

        @Override
        public boolean isPermanent() {
            return false;
        }

        @Override
        public void removeMember(@NotNull final CPlayer cp) {

        }
    }
}
