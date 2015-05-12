package works.chatterbox.chatterbox.wrappers;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.channels.Channel;

import java.util.Set;

public interface CPlayer {

    @NotNull
    Set<Channel> getChannels();

    @NotNull
    Channel getMainChannel();

    void setMainChannel(@NotNull final Channel channel);

    @NotNull
    OfflinePlayer getOfflinePlayer();

    @Nullable
    Player getPlayer();

    boolean isOnline();

}
