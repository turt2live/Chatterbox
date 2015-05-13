package works.chatterbox.chatterbox.pipeline;

import com.google.common.collect.Maps;

import java.util.Map;

public class PipelineContext {

    private final Map<String, Object> customVariables = Maps.newHashMap();

    /**
     * Gets a modifiable map of custom variables for the message passing through the pipeline. These variables only last
     * for the current message.
     * <p>These will be passed to the {@link org.rythmengine.RythmEngine RythmEngine} provided by
     * {@link works.chatterbox.chatterbox.api.rythm.RythmAPI RythmAPI} during the
     * {@link works.chatterbox.chatterbox.pipeline.stages.impl.rythm.RythmStage RythmStage}. They may be used by custom
     * stages, as well.
     *
     * @return Map of variables
     */
    public Map<String, Object> getCustomVariables() {
        return this.customVariables;
    }
}
