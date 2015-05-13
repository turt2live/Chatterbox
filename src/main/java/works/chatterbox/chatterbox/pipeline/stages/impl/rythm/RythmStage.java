package works.chatterbox.chatterbox.pipeline.stages.impl.rythm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.HashMap;
import java.util.Map;

public class RythmStage implements Stage {

    private final Chatterbox chatterbox;
    private final Map<String, Object> vars = Maps.newHashMap();

    public RythmStage(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.vars.put("chatterbox", new ChatterboxSpecialUtilities());
    }

    private Map<String, Object> getVariablesFor(@NotNull final Message message) {
        Preconditions.checkNotNull(message, "message was null");
        final Map<String, Object> vars = Maps.newHashMap();
        vars.put("message", message.getMessage());
        vars.put("player", message.getSender().getPlayer());
        vars.put("channel", message.getChannel());
        // TODO: Old-style variables like playerName, etc.
        return vars;
    }

    /**
     * This will process the message's format using the RythmEngine provided by
     * {@link works.chatterbox.chatterbox.api.rythm.RythmAPI RythmAPI}. Certain variables, such as the player, channel,
     * and message, are provided to the engine.
     * <p>Intended effect: The message's format is processed by Rythm
     */
    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        final Map<String, Object> vars = new HashMap<>(context.getCustomVariables());
        vars.putAll(this.getVariablesFor(message));
        vars.putAll(this.vars);
        message.setFormat(
            this.chatterbox.getAPI().getRythmAPI().render(message.getFormat(), vars)
        );
    }
}
