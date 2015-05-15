package works.chatterbox.chatterbox.pipeline.stages.impl.world;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.worlds.WorldRecipients;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.Iterator;

public class WorldStage implements Stage {

    /**
     * Gets the world status for this world. If this is false, the message should not be sent to this world. If this is
     * true, the message should be sent to this world. If this is null, there is no preference.
     *
     * @param wr    WorldRecipients
     * @param world World name to check
     * @return true if should be sent, false if not, null if no preference
     */
    private Boolean individualWorldStatus(final WorldRecipients wr, final String world) {
        return wr.getIndividualWorlds().get(world);
    }

    private boolean shouldBeSentToWorld(final WorldRecipients wr, final String recipientWorld, final String senderWorld) {
        if (wr.isToAll()) return true;
        final Boolean individualWorldStatus = this.individualWorldStatus(wr, recipientWorld);
        final boolean sameWorld = senderWorld.equals(recipientWorld);
        // If sending to self, has no individual status, and is not in the same world, return false
        if (wr.isToSelf() && individualWorldStatus == null && !sameWorld) {
            return false;
            // If sending to self, has no individual status, and is in the same world, return true
        } else if (wr.isToSelf() && individualWorldStatus == null) {
            return true;
            // If has no individual status, return true
        } else if (individualWorldStatus == null) {
            return true;
        }
        // Otherwise, just return the individual status
        return individualWorldStatus;
    }

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        final Player sender = message.getSender().getPlayer();
        if (sender == null) return;
        final WorldRecipients wr = message.getChannel().getWorldRecipients();
        if (wr.isToAll()) return;
        final Iterator<Player> recipients = message.getRecipients().iterator();
        while (recipients.hasNext()) {
            final Player recipient = recipients.next();
            final String recipientWorldName = recipient.getWorld().getName();
            if (!this.shouldBeSentToWorld(wr, recipientWorldName, sender.getWorld().getName())) {
                recipients.remove();
            }
        }
    }
}
