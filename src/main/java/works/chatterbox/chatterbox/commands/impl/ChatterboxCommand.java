package works.chatterbox.chatterbox.commands.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.commands.ChannelTabCommand;
import works.chatterbox.chatterbox.commands.ReflectCommand;

@ReflectCommand(
    name = "chatterbox",
    description = "Reloads the plugin."
)
public class ChatterboxCommand extends ChannelTabCommand {

    public ChatterboxCommand(final Chatterbox instance, final String name, final boolean checkPermissions, final Short[] cts) {
        super(instance, name, checkPermissions, cts);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        this.plugin.onDisable();
        this.plugin.load(true);
        cs.sendMessage(ChatColor.BLUE + this.plugin.getLanguage().getAString("RELOADED"));
        return true;
    }
}
