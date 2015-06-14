package works.chatterbox.chatterbox.api.messaging;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.pipeline.PipelineContext;

import java.util.Map;
import java.util.UUID;

public class MessagingAPI {

    // Receiver -> Sender
    private final static Map<UUID, UUID> lastSenders = Maps.newHashMap();
    private final Chatterbox chatterbox;
    private final MessagingChannel messagingChannel;

    public MessagingAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.messagingChannel = new MessagingChannel(this.chatterbox, this.chatterbox.getConfiguration().getNode("messaging").getNode("channel"));
    }

    /**
     * Gets an immutable copy of the map of last senders. This is a map with a recipient to sender structure, used
     * mainly for replies.
     *
     * @return Immutable map
     */
    @NotNull
    public Map<UUID, UUID> getLastSenders() {
        return ImmutableMap.copyOf(MessagingAPI.lastSenders);
    }

    /**
     * Gets the messaging channel, since it is not officially registered.
     *
     * @return Messaging channel
     */
    @NotNull
    public MessagingChannel getMessagingChannel() {
        return this.messagingChannel;
    }

    /**
     * Creates a private message with the given information.
     *
     * @param sender    Sender of the message
     * @param recipient Recipient of the message
     * @param message   Message being sent
     * @return A private message
     */
    @NotNull
    public PrivateMessage makeMessage(@NotNull final Player sender, @NotNull final Player recipient, @NotNull final String message) {
        Preconditions.checkNotNull(sender, "sender was null");
        Preconditions.checkNotNull(recipient, "recipient was null");
        Preconditions.checkNotNull(message, "message was null");
        final MessagingChannel mc = this.getMessagingChannel();
        return new PrivateMessage(
            this.chatterbox,
            sender,
            recipient,
            mc.getFormat(),
            message
        );
    }

    /**
     * Creates a message with the given information, then calls {@link #processMessage(PrivateMessage)} on it.
     *
     * @param sender    Sender of the message
     * @param recipient Recipient of the message
     * @param message   Message being sent
     * @see #processMessage(PrivateMessage)
     */
    public void processMessage(@NotNull final Player sender, @NotNull final Player recipient, @NotNull final String message) {
        Preconditions.checkNotNull(sender, "sender was null");
        Preconditions.checkNotNull(recipient, "recipient was null");
        Preconditions.checkNotNull(message, "message was null");
        this.processMessage(this.makeMessage(sender, recipient, message));
    }

    /**
     * Sends the given message through the message pipeline, registers the last sender, then sends the message to the
     * sender and recipient.
     *
     * @param message Message to process
     * @see #sendMessageToPipeline(PrivateMessage)
     * @see #registerLastSender(Player, Player)
     * @see #sendMessage(PrivateMessage)
     */
    public void processMessage(@NotNull final PrivateMessage message) {
        Preconditions.checkNotNull(message, "message was null");
        this.sendMessageToPipeline(message);
        this.registerLastSender(message.getPlayerRecipient(), message.getPlayerSender());
        this.sendMessage(message);
    }

    /**
     * Registers the recipient's last sender, for uses of replying.
     *
     * @param recipient Recipient
     * @param sender    Sender
     */
    public void registerLastSender(@NotNull final Player recipient, @NotNull final Player sender) {
        Preconditions.checkNotNull(recipient, "recipient was null");
        Preconditions.checkNotNull(sender, "sender was null");
        MessagingAPI.lastSenders.put(recipient.getUniqueId(), sender.getUniqueId());
    }

    /**
     * Sends the message to the sender and recipient, if it is not cancelled.
     *
     * @param message Message to send
     */
    public void sendMessage(@NotNull final PrivateMessage message) {
        Preconditions.checkNotNull(message, "message was null");
        if (message.isCancelled()) return; // likely JSON or recipient
        message.getRecipients().forEach(player -> player.sendMessage(message.getFormat()));
    }

    /**
     * Sends the message through the message pipeline with the necessary contextual options. This should always be
     * called when sending a private message through the pipeline.
     *
     * @param message Message to send
     */
    public void sendMessageToPipeline(@NotNull final PrivateMessage message) {
        Preconditions.checkNotNull(message, "message was null");
        final PipelineContext pc = new PipelineContext();
        pc.getCustomVariables().put("recipient", message.getPlayerRecipient());
        pc.getProperties().getNode("chatterbox_skip_stages").setValue(
            Lists.newArrayList(
                "works.chatterbox.chatterbox.pipeline.stages.impl.channel.ChannelRecipientsStage",
                "works.chatterbox.chatterbox.pipeline.stages.impl.channel.TagStage",
                "works.chatterbox.chatterbox.pipeline.stages.impl.channel.ChannelStage"
            )
        );
        this.chatterbox.getAPI().getMessageAPI().getMessagePipeline().send(message, pc);
    }

}
