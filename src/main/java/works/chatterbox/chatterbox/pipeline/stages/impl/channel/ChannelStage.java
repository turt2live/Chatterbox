package works.chatterbox.chatterbox.pipeline.stages.impl.channel;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

public class ChannelStage implements Stage {

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        // Set the format of the message to the channel format
        message.setFormat(message.getChannel().getFormat());
    }
}
