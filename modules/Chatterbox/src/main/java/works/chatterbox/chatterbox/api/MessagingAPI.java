package works.chatterbox.chatterbox.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.api.impl.messaging.MessagingChannel;
import works.chatterbox.chatterbox.api.impl.messaging.PrivateMessage;

import java.util.Map;
import java.util.UUID;

public interface MessagingAPI {

    /**
     * Gets an immutable copy of the map of last senders. This is a map with a recipient to sender structure, used
     * mainly for replies.
     *
     * @return Immutable map
     */
    @NotNull
    Map<UUID, UUID> getLastSenders();

    /**
     * Gets the messaging channel, since it is not officially registered.
     *
     * @return Messaging channel
     */
    @NotNull
    MessagingChannel getMessagingChannel();

    /**
     * Creates a private message with the given information.
     *
     * @param sender    Sender of the message
     * @param recipient Recipient of the message
     * @param message   Message being sent
     * @return A private message
     */
    @NotNull
    PrivateMessage makeMessage(@NotNull Player sender, @NotNull Player recipient, @NotNull String message);

    /**
     * Creates a message with the given information, then calls {@link #processMessage(PrivateMessage)} on it.
     *
     * @param sender    Sender of the message
     * @param recipient Recipient of the message
     * @param message   Message being sent
     * @see #processMessage(PrivateMessage)
     */
    void processMessage(@NotNull Player sender, @NotNull Player recipient, @NotNull String message);

    /**
     * Sends the given message through the message pipeline, registers the last sender, then sends the message to the
     * sender and recipient.
     *
     * @param message Message to process
     * @see #sendMessageToPipeline(PrivateMessage)
     * @see #registerLastSender(Player, Player)
     * @see #sendMessage(PrivateMessage)
     */
    void processMessage(@NotNull PrivateMessage message);

    /**
     * Registers the recipient's last sender, for uses of replying.
     *
     * @param recipient Recipient
     * @param sender    Sender
     */
    void registerLastSender(@NotNull Player recipient, @NotNull Player sender);

    /**
     * Sends the message to the sender and recipient, if it is not cancelled.
     *
     * @param message Message to send
     */
    void sendMessage(@NotNull PrivateMessage message);

    /**
     * Sends the message through the message pipeline with the necessary contextual options. This should always be
     * called when sending a private message through the pipeline.
     *
     * @param message Message to send
     */
    void sendMessageToPipeline(@NotNull PrivateMessage message);
}
