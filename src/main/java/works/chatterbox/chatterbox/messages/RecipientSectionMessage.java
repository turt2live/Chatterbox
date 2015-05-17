package works.chatterbox.chatterbox.messages;

import org.jetbrains.annotations.NotNull;

public class RecipientSectionMessage extends SectionMessage {

    public RecipientSectionMessage(@NotNull final Message base, @NotNull final String section) {
        super(base, section);
    }
}
