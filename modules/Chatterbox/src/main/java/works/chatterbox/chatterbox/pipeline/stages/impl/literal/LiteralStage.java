/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.literal;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LiteralStage implements Stage {

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled()) return;
        final String newlined = message.getFormat().replace("\n", "").replace("%n", "\n");
        message.setFormat(
            Arrays.stream(newlined.split("\\n"))
                .map(part -> part.trim().replace("%w", " "))
                .collect(Collectors.joining("\n"))
        );
    }
}
