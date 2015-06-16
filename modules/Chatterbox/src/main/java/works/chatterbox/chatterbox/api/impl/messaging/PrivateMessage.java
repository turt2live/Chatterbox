package works.chatterbox.chatterbox.api.impl.messaging;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.Set;

public class PrivateMessage implements Message {

    private final Chatterbox chatterbox;
    private final Player sender, recipient;
    private String format, message;
    private boolean cancelled;

    public PrivateMessage(@NotNull final Chatterbox chatterbox, @NotNull final Player sender, @NotNull Player recipient, @NotNull final String format, @NotNull final String message) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        Preconditions.checkNotNull(sender, "sender was null");
        Preconditions.checkNotNull(recipient, "recipient was null");
        Preconditions.checkNotNull(format, "format was null");
        Preconditions.checkNotNull(message, "message was null");
        this.chatterbox = chatterbox;
        this.sender = sender;
        this.recipient = recipient;
        this.format = format;
        this.message = message;
    }

    @NotNull
    @Override
    public Channel getChannel() {
        return this.chatterbox.getAPI().getMessagingAPI().getMessagingChannel();
    }

    @Override
    public void setChannel(@NotNull final Channel channel) {
        // no-op
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
        return Sets.newHashSet(this.getPlayerSender(), this.getPlayerRecipient());
    }

    @NotNull
    @Override
    public CPlayer getSender() {
        return this.chatterbox.getAPI().getPlayerAPI().getCPlayer(this.getPlayerSender());
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    public Player getPlayerRecipient() {
        return this.recipient;
    }

    @NotNull
    public Player getPlayerSender() {
        return this.sender;
    }
}
