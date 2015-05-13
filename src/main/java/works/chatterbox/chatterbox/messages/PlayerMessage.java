package works.chatterbox.chatterbox.messages;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.wrappers.CPlayer;

public class PlayerMessage implements Message {

    private final CPlayer sender;
    private Channel channel;
    private String format, message;

    public PlayerMessage(@NotNull final String format, @NotNull final String message, @NotNull final Channel channel, @NotNull final CPlayer sender) {
        Preconditions.checkNotNull(format, "format was null");
        Preconditions.checkNotNull(message, "message was null");
        Preconditions.checkNotNull(channel, "channel was null");
        Preconditions.checkNotNull(sender, "sender was null");
        this.format = format;
        this.message = message;
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
    public CPlayer getSender() {
        return this.sender;
    }
}
