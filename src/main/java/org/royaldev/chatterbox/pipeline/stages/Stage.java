package org.royaldev.chatterbox.pipeline.stages;

import org.jetbrains.annotations.NotNull;
import org.royaldev.chatterbox.messages.Message;

public interface Stage {

    /**
     * Process the given message.
     *
     * @param message Message
     */
    void process(@NotNull final Message message);

}
