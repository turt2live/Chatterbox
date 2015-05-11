package works.chatterbox.chatterbox.api.player;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.wrappers.CPlayer;
import works.chatterbox.chatterbox.wrappers.CPlayers;

import java.util.UUID;

/**
 * The player API has methods relating to players in Chatterbox.
 */
public class PlayerAPI {

    /**
     * Gets a CPlayer (Chatterbox Player) wrapper for the given UUID, representative of a Minecraft account's UUID. This
     * wrapper is used to get Chatterbox-specific information from one easy class. These are cached using soft
     * references.
     *
     * @param uuid UUID of the player to get the CPlayer of
     * @return CPlayer
     */
    @NotNull
    public CPlayer getCPlayer(@NotNull final UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid was null");
        return CPlayers.getCPlayer(uuid);
    }

    /**
     * Calls {@link #getCPlayer(UUID)} with {@link OfflinePlayer#getUniqueId()}.
     *
     * @param player OfflinePlayer to get CPlayer of
     * @return CPlayer
     * @see #getCPlayer(UUID)
     */
    @NotNull
    public CPlayer getCPlayer(@NotNull final OfflinePlayer player) {
        Preconditions.checkNotNull(player, "player was null");
        return this.getCPlayer(player.getUniqueId());
    }

}
