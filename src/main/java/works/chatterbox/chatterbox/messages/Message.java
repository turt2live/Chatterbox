package works.chatterbox.chatterbox.messages;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.wrappers.CPlayer;

public interface Message {

    /**
     * Gets the channel that this message is destined for.
     *
     * @return Channel
     */
    @NotNull
    Channel getChannel();

    /**
     * Sets the channel that this message is destined for.
     *
     * @param channel Channel to send message to
     */
    void setChannel(@NotNull final Channel channel);

    /**
     * Gets the format of this message. In most cases, it is originally a copy of
     * {@link AsyncPlayerChatEvent#getFormat()}.
     *
     * @return Format of the message
     */
    @NotNull
    String getFormat();

    /**
     * Sets the format of this message.
     *
     * @param format New format for this message
     */
    void setFormat(@NotNull final String format);

    /**
     * Gets the message content of this message. In most cases, it is originally a copy of
     * {@link AsyncPlayerChatEvent#getMessage()}.
     *
     * @return Message content of the message
     */
    @NotNull
    String getMessage();

    /**
     * Sets the message content of this message.
     *
     * @param message New message content for this message
     */
    void setMessage(@NotNull final String message);

    /**
     * Gets the CPlayer that sent this message.
     *
     * @return CPlayer
     */
    @NotNull
    CPlayer getSender();

}
