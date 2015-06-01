/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.color;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

public class ColorStripStage implements Stage {

    /**
     * Checks if a CommandSender can use color in messages.
     *
     * @param cs CommandSender
     * @return true if he can use color
     */
    private boolean canUseColors(@NotNull final CommandSender cs) {
        Preconditions.checkNotNull(cs, "cs was null");
        return cs.hasPermission("chatterbox.color");
    }

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        // Get the player represented by the CPlayer
        final Player p = message.getSender().getPlayer();
        // If the player isn't online, don't colorize the message
        if (p == null) return;
        // Color the message
        message.setMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
        // If the player sending the message doesn't have permission, strip the colors
        if (!this.canUseColors(p)) {
            message.setMessage(ChatColor.stripColor(message.getMessage()));
        }
    }
}
