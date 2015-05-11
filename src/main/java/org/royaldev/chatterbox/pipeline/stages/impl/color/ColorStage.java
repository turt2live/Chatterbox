package org.royaldev.chatterbox.pipeline.stages.impl.color;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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
        // If the player sending the message doesn't have permission, don't process colors
        if (!this.canUseColors(message.getPlayer())) return;
        // Color the message
        message.setMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
    }
}
