package works.chatterbox.chatterbox.pipeline.stages.impl.channel;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

public class ChannelStage implements Stage {

    /**
     * Sets the format of the message to the channel's format. Note that the format will not yet be run through the
     * Rythm engine, but it <em>should</em> be valid Rythm syntax after this stage completes (assuming that the channel
     * is configured properly).
     * <p>Intended effect: Message's format is now the channel's format
     */
    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        // Set the format of the message to the channel format
        message.setFormat(message.getChannel().getFormat());
    }
}
