/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.validation;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.messages.PlayerMessage;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

public class ValidationStage implements Stage {

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        // No empty messages from players!
        if (message instanceof PlayerMessage && ChatColor.stripColor(message.getMessage()).isEmpty()) {
            message.setCancelled(true);
        }
    }
}
