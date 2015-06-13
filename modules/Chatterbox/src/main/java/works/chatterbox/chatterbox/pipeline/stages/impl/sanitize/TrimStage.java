package works.chatterbox.chatterbox.pipeline.stages.impl.sanitize;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.regex.Pattern;

public class TrimStage implements Stage {

    private final static Pattern startSpaces = Pattern.compile("^(?:(§[0-9A-FK-OR])\\s+)+", Pattern.CASE_INSENSITIVE);
    private final static Pattern endSpaces = Pattern.compile("§[0-9A-FK-OR]\\s+$", Pattern.CASE_INSENSITIVE);

    public String findAndReplaceColoredLeadingOrTrailingSpaces(@NotNull String message) {
        Preconditions.checkNotNull(message, "message was null");
        // Can't have this issue if it isn't long enough
        if (message.length() < 3) return message;
        message = TrimStage.startSpaces.matcher(message).replaceAll("$1");
        message = TrimStage.endSpaces.matcher(message).replaceAll("");
        return message;
    }

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        // Even if the message is cancelled, this is important.
        // Trim the format
        message.setFormat(message.getFormat().trim());
        // Trim the message
        message.setMessage(
            this.findAndReplaceColoredLeadingOrTrailingSpaces(message.getMessage().trim())
        );
    }
}
