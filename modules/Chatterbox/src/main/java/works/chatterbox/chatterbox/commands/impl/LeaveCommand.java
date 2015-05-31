/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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

import java.util.Iterator;

@ReflectCommand(
    name = "leave",
    description = "Leaves a channel.",
    usage = "/<command> [channel]",
    keys = {
        "chatterbox leave",
        "chat leave"
    }
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
        if (channel.isPermanent()) {
            cs.sendMessage(ChatColor.RED + this.plugin.getLanguage().getAString("CANNOT_LEAVE_CHANNEL"));
            return true;
        }
        final boolean wasMainChannel = channel.equals(cp.getMainChannel());
        if (!cp.leaveChannel(channel)) {
            cs.sendMessage(ChatColor.RED + this.plugin.getLanguage().getFormattedString("COULD_NOT_LEAVE_CHANNEL"));
            return true;
        }
        cs.sendMessage(ChatColor.BLUE + this.plugin.getLanguage().getFormattedString("LEFT_CHANNEL", ChatColor.GRAY + channel.getName() + ChatColor.BLUE));
        if (wasMainChannel) {
            final Iterator<Channel> channels = cp.getChannels().iterator();
            final Channel newMain = channels.hasNext() ? channels.next() : this.plugin.getAPI().getChannelAPI().getDefaultChannel();
            cp.setMainChannel(newMain);
            cs.sendMessage(ChatColor.BLUE + this.plugin.getLanguage().getFormattedString("NEW_MAIN_CHANNEL", ChatColor.GRAY + newMain.getName() + ChatColor.BLUE));
        }
        return true;
    }
}
