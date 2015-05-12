package works.chatterbox.chatterbox.pipeline.stages.impl.rythm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

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
        return Maps.newHashMap();
    }

    @Override
    public void process(@NotNull final Message message) {
        final Map<String, Object> vars = this.getVariablesFor(message);
        vars.putAll(this.vars);
        message.setFormat(
            this.chatterbox.getAPI().getRythmAPI().render(message.getFormat(), vars)
        );
    }
}
