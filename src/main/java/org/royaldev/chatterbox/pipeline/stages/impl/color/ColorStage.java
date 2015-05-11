package org.royaldev.chatterbox.pipeline.stages.impl.color;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.royaldev.chatterbox.messages.Message;
import org.royaldev.chatterbox.pipeline.stages.Stage;

/**
 * The color stage. This stage handles all color code processing.
 */
public class ColorStage implements Stage {

    private boolean canUseColors(@NotNull final CommandSender cs) {
        Preconditions.checkNotNull(cs, "cs was null");
        return cs.hasPermission("chatterbox.color");
    }

    @Override
    public void process(@NotNull final Message message) {
        // Always color the format, since it has been defined by the config
        message.setFormat(ChatColor.translateAlternateColorCodes('&', message.getFormat()));
        // Get the player represented by the CPlayer
        final Player p = message.getSender().getPlayer();
        // If the player isn't online, don't colorize the message // TODO: Should we throw an exception?
        if (p == null) return;
        // If the player sending the message doesn't have permission, don't process colors
        if (!this.canUseColors(p)) return;
        // Color the message
        message.setMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
    }
}
