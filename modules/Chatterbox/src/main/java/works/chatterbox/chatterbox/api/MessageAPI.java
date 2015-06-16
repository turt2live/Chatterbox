package works.chatterbox.chatterbox.api;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.MessagePipeline;

public interface MessageAPI {

    /**
     * Gets the message pipeline. All messages should pass through this pipeline to be properly formatted.
     *
     * @return Message pipeline
     */
    @NotNull
    MessagePipeline getMessagePipeline();

    /**
     * Makes a message from an async chat event.
     *
     * @param event Event to make message from
     * @return New message object
     */
    @NotNull
    Message makeMessage(@NotNull AsyncPlayerChatEvent event);

    /**
     * Parses literals, like escapes ({@code \}), and color codes (for example, {@code &e}).
     *
     * @param original Non-parsed String
     * @return Parsed String
     */
    String parseLiterals(@NotNull String original);

    String parseLiteralsIgnoreEscapes(@NotNull String original);
}
