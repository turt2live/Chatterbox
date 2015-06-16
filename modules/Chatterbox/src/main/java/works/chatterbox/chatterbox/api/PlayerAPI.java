package works.chatterbox.chatterbox.api;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.UUID;

public interface PlayerAPI {

    /**
     * Gets a CPlayer (Chatterbox Player) wrapper for the given UUID, representative of a Minecraft account's UUID. This
     * wrapper is used to get Chatterbox-specific information from one easy class. These are cached using soft
     * references.
     *
     * @param uuid UUID of the player to get the CPlayer of
     * @return CPlayer
     */
    @NotNull
    CPlayer getCPlayer(@NotNull UUID uuid);

    /**
     * Calls {@link #getCPlayer(UUID)} with {@link OfflinePlayer#getUniqueId()}.
     *
     * @param player OfflinePlayer to get CPlayer of
     * @return CPlayer
     * @see #getCPlayer(UUID)
     */
    @NotNull
    CPlayer getCPlayer(@NotNull OfflinePlayer player);
}
