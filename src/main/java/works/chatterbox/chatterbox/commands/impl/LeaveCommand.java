package works.chatterbox.chatterbox.commands.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.commands.ChannelTabCommand;
import works.chatterbox.chatterbox.commands.ReflectCommand;
import works.chatterbox.chatterbox.wrappers.CPlayer;

@ReflectCommand(
    name = "leave",
    description = "Leaves a channel.",
    usage = "/<command> [channel]"
)
public class LeaveCommand extends ChannelTabCommand {

    public LeaveCommand(final Chatterbox instance, final String name) {
        super(instance, name, true, new Short[0]);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (eargs.length < 1 || !(cs instanceof Player)) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player player = (Player) cs;
        final String channelName = eargs[0];
        final Channel channel = this.plugin.getAPI().getChannelAPI().getChannel(channelName);
        if (channel == null) {
            cs.sendMessage(ChatColor.RED + this.plugin.getLanguage().getAString("NO_SUCH_CHANNEL"));
            return true;
        }
        final CPlayer cp = this.plugin.getAPI().getPlayerAPI().getCPlayer(player);
        if (!cp.getChannels().contains(channel)) {
            cs.sendMessage(ChatColor.RED + this.plugin.getLanguage().getAString("NOT_IN_CHANNEL"));
            return true;
        }
        cp.leaveChannel(channel);
        cs.sendMessage(ChatColor.BLUE + this.plugin.getLanguage().getFormattedString("LEFT_CHANNEL", ChatColor.GRAY + channel.getName() + ChatColor.BLUE));
        return true;
    }
}
