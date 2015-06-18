package works.chatterbox.chatterbox.commands.impl.messaging;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.commands.CommandUtils;
import works.chatterbox.chatterbox.commands.ReflectCommand;
import works.chatterbox.chatterbox.commands.TabCommand;

import java.util.List;

@ReflectCommand(
    name = "message",
    description = "Sends a private message to another player",
    usage = "/<command> [recipient] [message]",
    aliases = {"msg", "privmsg", "pm"},
    keys = {
        "chatterbox message", "chat message",
        "chatterbox msg", "chat msg",
        "chatterbox privmsg", "chat privmsg",
        "chatterbox pm", "chat pm"
    }
)
public class MessageCommand extends TabCommand<Chatterbox> {

    public MessageCommand(final Chatterbox instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.ONLINE_PLAYER.getShort()});
    }

    @Override
    protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
        return super.customList(cs, cmd, label, args, arg); // TODO
    }

    @Override
    protected boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (eargs.length < 2 || !(cs instanceof Player)) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player p = (Player) cs;
        final Player t = this.plugin.getServer().getPlayer(eargs[0]);
        if (t == null) {
            cs.sendMessage(ChatColor.RED + this.plugin.getAPI().getLanguageAPI().getLanguage(cs).getAString("NO_SUCH_PLAYER"));
            return true;
        }
        final String message = CommandUtils.joinFrom(eargs, 1);
        this.plugin.getAPI().getMessagingAPI().processMessage(p, t, message);
        return true;
    }
}
