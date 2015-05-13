package works.chatterbox.chatterbox.pipeline;

import com.google.common.collect.Maps;

import java.util.Map;

public class PipelineContext {

    private final Map<String, Object> customVariables = Maps.newHashMap();

    public Map<String, Object> getCustomVariables() {
        return this.customVariables;
    }
}
