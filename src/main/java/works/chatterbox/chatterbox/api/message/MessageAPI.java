package works.chatterbox.chatterbox.api.message;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.messages.PlayerMessage;
import works.chatterbox.chatterbox.pipeline.MessagePipeline;
import works.chatterbox.chatterbox.wrappers.CPlayer;

/**
 * The Message API handles all things messages.
 */
public class MessageAPI {

    private final Chatterbox chatterbox;
    private final MessagePipeline pipeline;

    public MessageAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.pipeline = new MessagePipeline();
    }

    /**
     * Gets the message pipeline. All messages should pass through this pipeline to be properly formatted.
     *
     * @return Message pipeline
     */
    @NotNull
    public MessagePipeline getMessagePipeline() {
        return this.pipeline;
    }

    /**
     * Makes a message from an async chat event.
     *
     * @param event Event to make message from
     * @return New message object
     */
    @NotNull
    public Message makeMessage(@NotNull final AsyncPlayerChatEvent event) {
        Preconditions.checkNotNull(event, "event was null");
        final CPlayer cp = this.chatterbox.getAPI().getPlayerAPI().getCPlayer(event.getPlayer());
        return new PlayerMessage(
            event.getFormat(),
            event.getMessage(),
            Sets.newHashSet(event.getRecipients()), // clone the original
            cp.getMainChannel(),
            cp
        );
    }
}
