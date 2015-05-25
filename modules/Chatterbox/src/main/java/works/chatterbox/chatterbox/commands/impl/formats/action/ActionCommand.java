package works.chatterbox.chatterbox.commands.impl.formats.action;

import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.commands.ReflectCommand;
import works.chatterbox.chatterbox.commands.impl.formats.FormatCommand;

@ReflectCommand(
    name = "action",
    description = "Performs an action.",
    aliases = {"me"},
    usage = "/<command> [message]"
)
public class ActionCommand extends FormatCommand {

    public ActionCommand(final Chatterbox instance, final String name) {
        super(instance, name, true, "action");
    }
}
