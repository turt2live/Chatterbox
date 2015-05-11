package org.royaldev.chatterbox.messages;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public interface Message {

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
     * Gets the Player that sent this message.
     * TODO: Determine if we should use wrappers
     *
     * @return Player
     */
    @NotNull
    Player getPlayer();

}
