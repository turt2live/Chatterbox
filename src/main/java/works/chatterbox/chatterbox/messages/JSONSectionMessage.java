package works.chatterbox.chatterbox.messages;

import org.jetbrains.annotations.NotNull;

public class JSONSectionMessage extends SectionMessage {

    public JSONSectionMessage(@NotNull final Message base, @NotNull final String section) {
        super(base, section);
    }
}
