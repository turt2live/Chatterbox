package works.chatterbox.chatterbox.api.impl.messaging;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.MessagingAPI;
import works.chatterbox.chatterbox.pipeline.PipelineContext;

import java.util.Map;
import java.util.UUID;

public class DefaultMessagingAPI implements MessagingAPI {

    // Receiver -> Sender
    private final static Map<UUID, UUID> lastSenders = Maps.newHashMap();
    private final Chatterbox chatterbox;
    private final MessagingChannel messagingChannel;

    public DefaultMessagingAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.messagingChannel = new MessagingChannel(this.chatterbox, this.chatterbox.getConfiguration().getNode("messaging").getNode("channel"));
    }

    @Override
    @NotNull
    public Map<UUID, UUID> getLastSenders() {
        return ImmutableMap.copyOf(DefaultMessagingAPI.lastSenders);
    }

    @Override
    @NotNull
    public MessagingChannel getMessagingChannel() {
        return this.messagingChannel;
    }

    @Override
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

    @Override
    public void processMessage(@NotNull final Player sender, @NotNull final Player recipient, @NotNull final String message) {
        Preconditions.checkNotNull(sender, "sender was null");
        Preconditions.checkNotNull(recipient, "recipient was null");
        Preconditions.checkNotNull(message, "message was null");
        this.processMessage(this.makeMessage(sender, recipient, message));
    }

    @Override
    public void processMessage(@NotNull final PrivateMessage message) {
        Preconditions.checkNotNull(message, "message was null");
        this.sendMessageToPipeline(message);
        this.registerLastSender(message.getPlayerRecipient(), message.getPlayerSender());
        this.sendMessage(message);
    }

    @Override
    public void registerLastSender(@NotNull final Player recipient, @NotNull final Player sender) {
        Preconditions.checkNotNull(recipient, "recipient was null");
        Preconditions.checkNotNull(sender, "sender was null");
        DefaultMessagingAPI.lastSenders.put(recipient.getUniqueId(), sender.getUniqueId());
    }

    @Override
    public void sendMessage(@NotNull final PrivateMessage message) {
        Preconditions.checkNotNull(message, "message was null");
        if (message.isCancelled()) return; // likely JSON or recipient
        message.getRecipients().forEach(player -> player.sendMessage(message.getFormat()));
    }

    @Override
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
