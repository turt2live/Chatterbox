package works.chatterbox.chatterbox.api.impl.messaging;

import com.google.common.collect.Maps;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.ConfigChannel;
import works.chatterbox.chatterbox.channels.radius.Radius;
import works.chatterbox.chatterbox.channels.worlds.WorldRecipients;
import works.chatterbox.chatterbox.shaded.mkremins.fanciful.FancyMessage;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.Collections;
import java.util.Set;

public class MessagingChannel extends ConfigChannel {

    public MessagingChannel(@NotNull final Chatterbox chatterbox, @NotNull final ConfigurationNode node) {
        super(chatterbox, node);
    }

    @Override
    public boolean addMember(@NotNull final CPlayer cp) {
        return true; // members can be added, but we don't actually send to people
    }

    @Override
    public int getMaximumMembers() {
        return 0;
    }

    @NotNull
    @Override
    public Set<CPlayer> getMembers() {
        return Collections.emptySet();
    }

    @NotNull
    @Override
    public String getName() {
        return "messaging";
    }

    @Nullable
    @Override
    public Radius getRadius() {
        return null;
    }

    @NotNull
    @Override
    public String getTag() {
        return "msg";
    }

    @NotNull
    @Override
    public WorldRecipients getWorldRecipients() {
        return new WorldRecipients(Maps.newHashMap(), true, true);
    }

    @Override
    public boolean isPermanent() {
        return true;
    }

    @Override
    public boolean removeMember(@NotNull final CPlayer cp) {
        return true;
    }

    @Override
    public void sendMessage(@NotNull final String message) {
        // no-op
    }

    @Override
    public void sendMessage(@NotNull final FancyMessage message) {
        // no-op
    }
}
