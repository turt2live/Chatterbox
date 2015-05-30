/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.channels.tasks;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.wrappers.CPlayer;
import works.chatterbox.chatterbox.wrappers.UUIDCPlayer;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MembershipTask implements Runnable {

    private final Chatterbox chatterbox;

    public MembershipTask(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
    }

    @Override
    public void run() {
        final Collection<Channel> loadedChannels = this.chatterbox.getAPI().getChannelAPI().getAllLoadedChannels();
        final ConfigurationNode memberships = this.chatterbox.getAPI().getChannelAPI().getMemberships();
        // Get a single time to use as the last seen time during this run
        final long useAsLastSeen = System.currentTimeMillis();
        // Loop through all LOADED channels (no one is in a channel that has not been loaded)
        for (final Channel channel : loadedChannels) {
            // Get the membership node
            final ConfigurationNode node = memberships.getNode(channel.getName());
            // Make a mapping between UUIDs in String form and CPlayers
            final Map<String, CPlayer> uuids = Maps.newHashMap();
            // Fill in the mapping
            for (final CPlayer member : channel.getMembers()) {
                if (!(member instanceof UUIDCPlayer)) continue;
                uuids.put(((UUIDCPlayer) member).getUUID().toString(), member);
            }
            // Make a set of UUIDs (in String form) to remove
            final Set<String> remove = Sets.newHashSet();
            // Convert the map to String/Long
            final Map<String, Long> childrenMap = node.getChildrenMap().entrySet().stream()
                .collect(Collectors.toMap(
                    entry -> entry.getKey().toString(),
                    entry -> entry.getValue().getLong()
                ));
            // Get a similar map of all the current members, with the useAsLastSeen timestamp as the long
            final Map<String, Long> currentMembers = uuids.keySet().stream()
                .collect(Collectors.toMap(
                    uuid -> uuid,
                    uuid -> useAsLastSeen
                ));
            // Add all current members to the children map
            childrenMap.putAll(currentMembers);
            // Loop over it
            for (final Entry<String, Long> entry : childrenMap.entrySet()) {
                final String uuid = entry.getKey();
                final long lastSeen = Math.abs(entry.getValue());
                final ConfigurationNode childNode = node.getNode(uuid);
                // If not in the channel and has been absent for 10 days, add to the remove set
                if (!uuids.containsKey(uuid) && (lastSeen == 0L || System.currentTimeMillis() - lastSeen > TimeUnit.DAYS.toMillis(10L))) {
                    remove.add(uuid);
                    // Remove from the file
                    childNode.setValue(null);
                    continue;
                }
                // If still in the channel, update the last seen
                final CPlayer cp = uuids.get(uuid);
                if (cp != null) {
                    // If uuids contains the key, this will be useAsLastSeen
                    childNode.setValue(cp.getMainChannel().getName().equals(channel.getName()) ? -lastSeen : lastSeen);
                }
            }
            // Remove any member to remove
            remove.stream()
                .map(uuid -> uuids.compute(uuid, (k, v) -> v == null ? null : v))
                .filter(cp -> cp != null)
                .forEach(channel::removeMember);
        }
        // And save it
        this.chatterbox.getAPI().getChannelAPI().saveMemberships();
    }
}
