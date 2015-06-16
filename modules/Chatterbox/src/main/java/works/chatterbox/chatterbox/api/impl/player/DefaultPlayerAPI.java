/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.api.impl.player;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.PlayerAPI;
import works.chatterbox.chatterbox.wrappers.CPlayer;
import works.chatterbox.chatterbox.wrappers.UUIDCPlayer;

import java.util.UUID;

/**
 * The player API has methods relating to players in Chatterbox.
 */
public class DefaultPlayerAPI implements PlayerAPI {

    private final Chatterbox chatterbox;

    private final LoadingCache<UUID, CPlayer> players = CacheBuilder.newBuilder()
        .softValues()
        .removalListener(new RemovalListener<UUID, CPlayer>() {
            @Override
            public void onRemoval(@NotNull final RemovalNotification<UUID, CPlayer> notification) {
                final UUID uuid = notification.getKey();
                if (uuid == null) return;
                // Invalidate the titles being kept with this player
                DefaultPlayerAPI.this.chatterbox.getAPI().getTitleAPI().invalidate(uuid);
            }
        })
        .build(new CacheLoader<UUID, CPlayer>() {
            @Override
            @NotNull
            public CPlayer load(@NotNull final UUID key) throws Exception {
                return new UUIDCPlayer(key, DefaultPlayerAPI.this.chatterbox);
            }
        });

    public DefaultPlayerAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
    }

    @Override
    @NotNull
    public CPlayer getCPlayer(@NotNull final UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid was null");
        return this.players.getUnchecked(uuid);
    }

    @Override
    @NotNull
    public CPlayer getCPlayer(@NotNull final OfflinePlayer player) {
        Preconditions.checkNotNull(player, "player was null");
        return this.getCPlayer(player.getUniqueId());
    }

}
