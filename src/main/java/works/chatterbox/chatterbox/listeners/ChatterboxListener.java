package works.chatterbox.chatterbox.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.wrappers.CPlayer;

public class ChatterboxListener implements Listener {

    private final Chatterbox chatterbox;

    public ChatterboxListener(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent event) {
        // Make a Message from this event
        final Message message = this.chatterbox.getAPI().getMessageAPI().makeMessage(event);
        // Pass it through the pipeline
        this.chatterbox.getAPI().getMessageAPI().getMessagePipeline().send(message);
        // Set the new format
        event.setFormat(message.getFormat());
        // Set the new message
        event.setMessage(message.getMessage());
        // Set the new recipients
        event.getRecipients().clear();
        event.getRecipients().addAll(message.getRecipients());
        if (message.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent event) {
        final CPlayer cp = this.chatterbox.getAPI().getPlayerAPI().getCPlayer(event.getPlayer());
        if (!cp.getChannels().isEmpty()) return;
        // TODO: CPlayer#joinChannel(Channel) method?
        cp.getChannels().add(this.chatterbox.getAPI().getChannelAPI().getDefaultChannel());
    }

}
