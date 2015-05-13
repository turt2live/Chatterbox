package works.chatterbox.chatterbox.pipeline.stages.impl.color;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

/**
 * The color stage. This stage handles all color code processing.
 */
public class ColorStage implements Stage {

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

    /**
     * Processes all the colors in the message's format, regardless of who is sending the message. Then, if the player
     * sending the message has permission, processes the message's content for colors.
     * <p>Intended effect: colored format and possibly colored message
     */
    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
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
