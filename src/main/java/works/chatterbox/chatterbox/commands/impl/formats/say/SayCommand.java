package works.chatterbox.chatterbox.commands.impl.formats.say;

import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.commands.ReflectCommand;
import works.chatterbox.chatterbox.commands.impl.formats.FormatCommand;

@ReflectCommand(
    name = "say",
    description = "Displays a message to the entire server.",
    usage = "/<command> [message]"
)
public class SayCommand extends FormatCommand {

    public SayCommand(final Chatterbox instance, final String name) {
        super(instance, name, true, "say");
    }
}
