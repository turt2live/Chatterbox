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
        final Message message = this.chatterbox.getAPI().getMessageAPI().makeMessage(event);
        this.chatterbox.getAPI().getMessageAPI().getMessagePipeline().send(message);
        event.setFormat(message.getFormat());
        event.setMessage(message.getMessage());
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent event) {
        final CPlayer cp = this.chatterbox.getAPI().getPlayerAPI().getCPlayer(event.getPlayer());
        if (!cp.getChannels().isEmpty()) return;
        // TODO: CPlayer#joinChannel(Channel) method?
        cp.getChannels().add(this.chatterbox.getAPI().getChannelAPI().getDefaultChannel());
    }

}
