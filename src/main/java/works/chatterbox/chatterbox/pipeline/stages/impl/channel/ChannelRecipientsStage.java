package works.chatterbox.chatterbox.pipeline.stages.impl.channel;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;
import works.chatterbox.chatterbox.wrappers.CPlayer;

public class ChannelRecipientsStage implements Stage {

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        // Clear the original participants
        message.getRecipients().clear();
        message.getChannel().getMembers().stream() // Get all the members in the channel
            .map(CPlayer::getPlayer) // Map them to actual Player objects
            .filter(player -> player != null) // Filter out the null ones
            .forEach(message.getRecipients()::add); // Add the online players to the recipients set
    }
}
