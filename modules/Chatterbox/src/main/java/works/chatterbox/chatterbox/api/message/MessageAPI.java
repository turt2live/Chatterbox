/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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

    /**
     * Parses literals, like escapes ({@code \}), and color codes (for example, {@code &e}).
     *
     * @param original Non-parsed String
     * @return Parsed String
     */
    public String parseLiterals(@NotNull final String original) {
        Preconditions.checkNotNull(original, "original was null");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            final char atIndex = original.charAt(i);
            if (i + 1 < original.length()) {
                final char plusOne = original.charAt(i + 1);
                if (atIndex == '&') {
                    if (i - 1 >= 0) {
                        if (original.charAt(i - 1) == '\\') continue;
                    }
                    if ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(plusOne) > -1) {
                        sb.append('§').append(plusOne);
                        i++;
                        continue;
                    }
                }
            }
            sb.append(atIndex);
        }
        return sb.toString();
    }
}
