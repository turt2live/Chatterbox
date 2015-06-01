/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.rythm;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.JSONSectionMessage;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.function.Function;
import java.util.regex.Pattern;

public class SpecialStage implements Stage {

    private final ChatterboxSpecialUtilities specialUtilities = new ChatterboxSpecialUtilities();

    private void cancelJSON(@NotNull final Message message) {
        Preconditions.checkNotNull(message, "message was null");
        if (!(message instanceof JSONSectionMessage)) return;
        if (!message.getFormat().contains(this.specialUtilities.cancelJSON())) return;
        message.setCancelled(true);
    }

    private void processSpecials(@NotNull final Message message) {
        Preconditions.checkNotNull(message, "message was null");
        this.trim(message);
        this.removeMultipleNewlines(message);
        this.removeMultipleSpaces(message);
        this.cancelJSON(message);
    }

    private void removeMultipleNewlines(@NotNull final Message message) {
        Preconditions.checkNotNull(message, "message was null");
        this.replace(message, this.specialUtilities.removeMultipleNewlines(), string -> string.replaceAll("(\\r?\\n){2,}", "$1"));
    }

    private void removeMultipleSpaces(@NotNull final Message message) {
        Preconditions.checkNotNull(message, "message was null");
        this.replace(message, this.specialUtilities.removeMultipleSpaces(), string -> string.replaceAll("(\\s){2,}", "$1"));
    }

    private void replace(@NotNull final Message message, @NotNull final String contains, @NotNull final Function<String, String> mutator) {
        Preconditions.checkNotNull(message, "message was null");
        Preconditions.checkNotNull(contains, "contains was null");
        Preconditions.checkNotNull(mutator, "mutator was null");
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

    private void trim(@NotNull final Message message) {
        Preconditions.checkNotNull(message, "message was null");
        this.replace(message, this.specialUtilities.trim(), String::trim);
    }

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled()) return;
        this.processSpecials(message);
    }
}
