package works.chatterbox.chatterbox.commands.impl.messaging;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.commands.CommandUtils;
import works.chatterbox.chatterbox.commands.ReflectCommand;
import works.chatterbox.chatterbox.commands.TabCommand;

import java.util.UUID;

@ReflectCommand(
    name = "reply",
    description = "Replies to the last person that sent you a message",
    usage = "/<command> [message]",
    aliases = {"r"},
    keys = {
        "chatterbox reply", "chat reply",
        "chatterbox r", "chat r"
    }
)
public class ReplyCommand extends TabCommand<Chatterbox> {

    public ReplyCommand(final Chatterbox instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.ONLINE_PLAYER.getShort()});
    }

    @Override
    protected boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (eargs.length < 1 || !(cs instanceof Player)) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player p = (Player) cs;
        final UUID uuid = this.plugin.getAPI().getMessagingAPI().getLastSenders().get(p.getUniqueId());
        if (uuid == null) {
            cs.sendMessage(ChatColor.RED + this.plugin.getAPI().getLanguageAPI().getLanguage(cs).getAString("NO_REPLY"));
            return true;
        }
        final Player t = this.plugin.getServer().getPlayer(uuid);
        if (t == null) {
            cs.sendMessage(ChatColor.RED + this.plugin.getAPI().getLanguageAPI().getLanguage(cs).getAString("NO_SUCH_PLAYER"));
            return true;
        }
        final String message = CommandUtils.getSpaceJoiner().join(eargs);
        this.plugin.getAPI().getMessagingAPI().processMessage(p, t, message);
        return true;
    }
}
