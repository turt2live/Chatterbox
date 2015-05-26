/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.sanitize;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.messages.PlayerMessage;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

public class SanitizeStage implements Stage {

    private String sanitize(@NotNull final String string) {
        return string.replace("%", "%%");
    }

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        // Even if the message is cancelled, this is important.
        if (!(message instanceof PlayerMessage)) return;
        message.setFormat(this.sanitize(message.getFormat()));
        message.setMessage(this.sanitize(message.getMessage()));
    }
}
