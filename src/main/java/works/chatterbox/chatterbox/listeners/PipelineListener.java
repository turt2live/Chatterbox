package works.chatterbox.chatterbox.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.messages.Message;

public class PipelineListener implements Listener {

    private final Chatterbox chatterbox;

    public PipelineListener(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent event) {
        final Message message = this.chatterbox.getAPI().getMessageAPI().makeMessage(event);
        this.chatterbox.getAPI().getMessageAPI().getMessagePipeline().send(message);
        event.setFormat(message.getFormat());
        event.setMessage(message.getMessage());
    }

}
