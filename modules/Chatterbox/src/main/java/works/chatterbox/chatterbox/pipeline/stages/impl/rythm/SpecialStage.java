package works.chatterbox.chatterbox.pipeline.stages.impl.rythm;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.JSONSectionMessage;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.function.Function;
import java.util.regex.Pattern;

public class SpecialStage implements Stage {

    private final ChatterboxSpecialUtilities specialUtilities = new ChatterboxSpecialUtilities();

    private void cancelJSON(final Message message) {
        if (!(message instanceof JSONSectionMessage)) return;
        if (!message.getFormat().contains(this.specialUtilities.cancelJSON())) return;
        message.setCancelled(true);
    }

    private void processSpecials(final Message message) {
        this.trim(message);
        this.removeMultipleNewlines(message);
        this.removeMultipleSpaces(message);
        this.cancelJSON(message);
    }

    private void removeMultipleNewlines(final Message message) {
        this.replace(message, this.specialUtilities.removeMultipleNewlines(), string -> string.replaceAll("(\\r?\\n){2,}", "$1"));
    }

    private void removeMultipleSpaces(final Message message) {
        this.replace(message, this.specialUtilities.removeMultipleSpaces(), string -> string.replaceAll("(\\s){2,}", "$1"));
    }

    private void replace(final Message message, final String contains, final Function<String, String> mutator) {
        if (message.getFormat().contains(contains)) {
            message.setFormat(
                mutator.apply(
                    message.getFormat().replaceFirst(Pattern.quote(contains), "")
                )
            );
        }
        if (message.getMessage().contains(contains)) {
            message.setMessage(
                mutator.apply(
                    message.getMessage().replaceFirst(Pattern.quote(contains), "")
                )
            );
        }
    }

    private void trim(final Message message) {
        this.replace(message, this.specialUtilities.trim(), String::trim);
    }

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled()) return;
        this.processSpecials(message);
    }
}
