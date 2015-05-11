package works.chatterbox.chatterbox.api.message;

import com.google.common.base.Preconditions;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.messages.PlayerMessage;
import works.chatterbox.chatterbox.pipeline.MessagePipeline;
import works.chatterbox.chatterbox.wrappers.CPlayer;

public class MessageAPI {

    private final Chatterbox chatterbox;
    private final MessagePipeline pipeline = new MessagePipeline();

    public MessageAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
    }

    @NotNull
    public MessagePipeline getMessagePipeline() {
        return this.pipeline;
    }

    @NotNull
    public Message makeMessage(@NotNull final AsyncPlayerChatEvent event) {
        Preconditions.checkNotNull(event, "event was null");
        final CPlayer cp = this.chatterbox.getAPI().getPlayerAPI().getCPlayer(event.getPlayer());
        return new PlayerMessage(event.getFormat(), event.getMessage(), cp.getMainChannel(), cp);
    }
}
