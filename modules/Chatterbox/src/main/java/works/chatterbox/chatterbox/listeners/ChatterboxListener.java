/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.listeners;

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
        final CPlayer cp = this.chatterbox.getAPI().getPlayerAPI().getCPlayer(event.getPlayer());
        cp.joinPreviousChannels();
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
        // Run a ChannelPreMessageEvent
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
        // Run a ChannelPostMessageEvent
    }

}
