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
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.events.channels.messages.ChannelPostMessageEvent;
import works.chatterbox.chatterbox.events.channels.messages.ChannelPreMessageEvent;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.wrappers.CPlayer;
import works.chatterbox.chatterbox.wrappers.UUIDCPlayer;

import java.util.stream.Collectors;

public class ChatterboxListener implements Listener {

    private final Chatterbox chatterbox;

    public ChatterboxListener(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    @EventHandler(ignoreCancelled = true)
    public void atTagTabComplete(final PlayerChatTabCompleteEvent event) {
        final String message = event.getChatMessage();
        final String token = event.getLastToken();
        if (!message.equals(token) && !token.startsWith("@")) return;
        final String partialChannelTag = token.substring(1).toLowerCase();
        event.getTabCompletions().addAll(
            this.chatterbox.getAPI().getChannelAPI().getAllChannelTags().stream()
                .filter(tag -> tag.toLowerCase().startsWith(partialChannelTag))
                .map(tag -> "@" + tag)
                .collect(Collectors.toSet())
        );
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
        final ChannelPreMessageEvent preMessageEvent = new ChannelPreMessageEvent(message.getChannel(), message.getSender(), message);
        preMessageEvent.setCancelled(event.isCancelled());
        this.chatterbox.getServer().getPluginManager().callEvent(preMessageEvent);
        if (preMessageEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        // Pass it through the pipeline
        this.chatterbox.getAPI().getMessageAPI().getMessagePipeline().send(message);
        // Set the new format
        event.setFormat(message.getFormat().replace("%", "%%"));
        // Set the new message
        event.setMessage(message.getMessage());
        // Set the new recipients
        event.getRecipients().clear();
        event.getRecipients().addAll(message.getRecipients());
        if (message.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        // Run a ChannelPostMessageEvent
        final ChannelPostMessageEvent postMessageEvent = new ChannelPostMessageEvent(message.getChannel(), message.getSender(), message);
        postMessageEvent.setCancelled(event.isCancelled());
        this.chatterbox.getServer().getPluginManager().callEvent(postMessageEvent);
        if (postMessageEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

}
