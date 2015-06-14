/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.channel;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.stream.Collectors;

public class ChannelRecipientsStage implements Stage {

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled()) return;
        message.getRecipients().retainAll(
            message.getChannel().getMembers().stream() // Get all the members in the channel
                .map(CPlayer::getPlayer) // Map them to actual Player objects
                .filter(player -> player != null) // Filter out the null ones
                .collect(Collectors.toSet()) // Collect them into a set
        );
        final Player p = message.getSender().getPlayer();
        if (p == null || message.getRecipients().contains(p)) return;
        message.getRecipients().add(p); // Ensure that the sender sees his own message
    }
}
