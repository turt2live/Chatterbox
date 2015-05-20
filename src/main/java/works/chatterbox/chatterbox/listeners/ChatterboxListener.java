package works.chatterbox.chatterbox.listeners;

import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.wrappers.CPlayer;
import works.chatterbox.chatterbox.wrappers.UUIDCPlayer;

public class ChatterboxListener implements Listener {

    private final Chatterbox chatterbox;

    public ChatterboxListener(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    @EventHandler(ignoreCancelled = true)
    public void joinDefaultChannel(final PlayerJoinEvent event) {
        // Get the CPlayer for this event
        final CPlayer cp = this.chatterbox.getAPI().getPlayerAPI().getCPlayer(event.getPlayer());
        // If the CPlayer is in any channels, ignore
        if (!cp.getChannels().isEmpty()) return;
        // Join the default channel
        cp.joinChannel(this.chatterbox.getAPI().getChannelAPI().getDefaultChannel());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void joinPermanentChannels(final PlayerJoinEvent event) {
        // Get the CPlayer for this event
        final CPlayer cp = this.chatterbox.getAPI().getPlayerAPI().getCPlayer(event.getPlayer());
        // Get all permanent channels and join them
        this.chatterbox.getAPI().getChannelAPI().getAllChannelNames().stream()
            .map(this.chatterbox.getAPI().getChannelAPI()::getChannelByName)
            .filter(channel -> channel != null)
            .filter(Channel::isPermanent)
            .forEach(cp::joinChannel);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void joinPreviousChannels(final PlayerJoinEvent event) {
        // Get the Player and CPlayer
        final Player p = event.getPlayer();
        final CPlayer cp = this.chatterbox.getAPI().getPlayerAPI().getCPlayer(p);
        // Get all saved memberships
        final ConfigurationNode memberships = this.chatterbox.getAPI().getChannelAPI().getMemberships();
        for (final String channelName : this.chatterbox.getAPI().getChannelAPI().getAllChannelNames()) {
            final ConfigurationNode channelNode = memberships.getNode(channelName).getNode(p.getUniqueId().toString());
            // Filter out channels with memberships without the player
            if (channelNode.isVirtual()) continue;
            // Map the names to channels
            final Channel channel = this.chatterbox.getAPI().getChannelAPI().getChannelByName(channelName);
            // Remove any nulls
            if (channel == null) continue;
            // Join each channel
            cp.joinChannel(channel);
            if (channelNode.getLong() < 0L) {
                // If the lastSeenTime is negative, it was the main channel
                cp.setMainChannel(channel);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void leaveOnQuit(final PlayerQuitEvent event) {
        final CPlayer cp = this.chatterbox.getAPI().getPlayerAPI().getCPlayer(event.getPlayer());
        if (cp instanceof UUIDCPlayer) {
            this.chatterbox.getAPI().getChannelAPI().updateMembershipsWithoutSave((UUIDCPlayer) cp);
        }
        cp.getChannels().forEach(cp::leaveChannel);
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

}
