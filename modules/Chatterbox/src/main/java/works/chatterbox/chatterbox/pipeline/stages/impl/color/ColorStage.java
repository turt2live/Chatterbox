/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.color;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

/**
 * The color stage. This stage handles all color code processing.
 */
public class ColorStage implements Stage {

    /**
     * Processes all the colors in the message's format, regardless of who is sending the message. Then, if the player
     * sending the message has permission, processes the message's content for colors.
     * <p>Intended effect: colored format and possibly colored message
     */
    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled()) return;
        // Always color the format, since it has been defined by the config
        message.setFormat(ChatColor.translateAlternateColorCodes('&', message.getFormat()));
    }
}
