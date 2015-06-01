/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.messages;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.channels.radius.Radius;
import works.chatterbox.chatterbox.channels.worlds.WorldRecipients;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.Set;

public abstract class SectionMessage implements Message {

    private final Message base;
    private String section;
    private boolean cancelled;

    public SectionMessage(@NotNull final Message base, @NotNull final String section) {
        Preconditions.checkNotNull(base, "base was null");
        Preconditions.checkNotNull(section, "section was null");
        this.base = base;
        this.section = section;
    }

    @NotNull
    @Override
    public Channel getChannel() {
        return new SectionChannel();
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

    public class SectionChannel implements Channel {

        @Override
        public boolean addMember(@NotNull final CPlayer cp) {
            return SectionMessage.this.base.getChannel().addMember(cp);
        }

        @NotNull
        @Override
        public String getFormat() {
            return SectionMessage.this.getFormat();
        }

        @Nullable
        @Override
        public String getJSONSection(@NotNull final String sectionName) {
            return SectionMessage.this.base.getChannel().getJSONSection(sectionName);
        }

        @Override
        public int getMaximumMembers() {
            return 0;
        }

        @NotNull
        @Override
        public Set<CPlayer> getMembers() {
            return SectionMessage.this.base.getChannel().getMembers();
        }

        @NotNull
        @Override
        public String getName() {
            return SectionMessage.this.base.getChannel().getName();
        }

        @Nullable
        @Override
        public Radius getRadius() {
            return SectionMessage.this.base.getChannel().getRadius();
        }

        @Nullable
        @Override
        public String getRecipientSection(@NotNull final String sectionName) {
            return SectionMessage.this.base.getChannel().getRecipientSection(sectionName);
        }

        @NotNull
        @Override
        public String getTag() {
            return SectionMessage.this.base.getChannel().getTag();
        }

        @NotNull
        @Override
        public WorldRecipients getWorldRecipients() {
            return SectionMessage.this.base.getChannel().getWorldRecipients();
        }

        @Override
        public boolean isPermanent() {
            return SectionMessage.this.base.getChannel().isPermanent();
        }

        @Override
        public boolean removeMember(@NotNull final CPlayer cp) {
            return SectionMessage.this.base.getChannel().removeMember(cp);
        }
    }

}
