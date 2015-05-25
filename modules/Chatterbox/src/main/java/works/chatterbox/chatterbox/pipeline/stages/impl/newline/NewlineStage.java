package works.chatterbox.chatterbox.pipeline.stages.impl.newline;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

public class NewlineStage implements Stage {

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled()) return;
        message.setFormat(message.getFormat().replace("\n", "").replace("%n", "\n"));
    }
}
