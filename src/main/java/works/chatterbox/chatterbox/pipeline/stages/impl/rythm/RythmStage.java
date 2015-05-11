package works.chatterbox.chatterbox.pipeline.stages.impl.rythm;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.stages.Stage;
import org.rythmengine.RythmEngine;

import java.util.Map;

public class RythmStage implements Stage {

    private final RythmEngine rythm;

    public RythmStage() {
        final Map<String, Object> vars = Maps.newHashMap();
        vars.put("chatterbox", new ChatterboxSpecialUtilities());
        this.rythm = new RythmEngine(vars);
    }

    @Override
    public void process(@NotNull final Message message) {
    }
}
