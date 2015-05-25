package works.chatterbox.chatterbox.pipeline.stages.impl.radius;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.radius.Radius;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.Set;
import java.util.stream.Collectors;

public class RadiusStage implements Stage {

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled()) return;
        final Player p = message.getSender().getPlayer();
        if (p == null) return;
        final Radius radius = message.getChannel().getRadius();
        if (radius == null) return;
        final Set<Player> players = p.getNearbyEntities(radius.getHorizontal(), radius.getVertical(), radius.getHorizontal()).stream()
            .filter(entity -> entity.getType() == EntityType.PLAYER)
            .map(entity -> (Player) entity)
            .collect(Collectors.toSet());
        players.add(p);
        message.getRecipients().retainAll(players);
    }
}
