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
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.Set;

public class PlayerMessage implements Message {

    private final CPlayer sender;
    private final Set<Player> recipients = Sets.newHashSet();
    private Channel channel;
    private String format, message;
    private boolean cancelled;

    public PlayerMessage(@NotNull final String format, @NotNull final String message, @NotNull final Set<Player> recipients, @NotNull final Channel channel, @NotNull final CPlayer sender) {
        Preconditions.checkNotNull(format, "format was null");
        Preconditions.checkNotNull(message, "message was null");
        Preconditions.checkNotNull(recipients, "recipients was null");
        Preconditions.checkNotNull(channel, "channel was null");
        Preconditions.checkNotNull(sender, "sender was null");
        this.format = format;
        this.message = message;
        this.recipients.addAll(recipients);
        this.channel = channel;
        this.sender = sender;
    }

    @NotNull
    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(@NotNull final Channel channel) {
        Preconditions.checkNotNull(channel, "channel was null");
        this.channel = channel;
    }

    @NotNull
    @Override
    public String getFormat() {
        return this.format;
    }

    @Override
    public void setFormat(@NotNull final String format) {
        Preconditions.checkNotNull(format, "format was null");
        this.format = format;
    }

    @NotNull
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(@NotNull final String message) {
        Preconditions.checkNotNull(message, "message was null");
        this.message = message;
    }

    @NotNull
    @Override
    public Set<Player> getRecipients() {
        return this.recipients;
    }

    @NotNull
    @Override
    public CPlayer getSender() {
        return this.sender;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
