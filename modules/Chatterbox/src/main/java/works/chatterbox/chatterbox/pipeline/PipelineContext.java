/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline;

import com.google.common.collect.Maps;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PipelineContext {

    private final Map<String, Object> customVariables = Maps.newHashMap();
    private final ConfigurationNode properties = SimpleConfigurationNode.root();

    /**
     * Gets a modifiable map of custom variables for the message passing through the pipeline. These variables only last
     * for the current message.
     * <p>These will be passed to the {@link org.rythmengine.RythmEngine RythmEngine} provided by
     * {@link works.chatterbox.chatterbox.api.RythmAPI RythmAPI} during the
     * {@link works.chatterbox.chatterbox.pipeline.stages.impl.rythm.RythmStage RythmStage}. They may be used by custom
     * stages, as well.
     *
     * @return Map of variables
     */
    @NotNull
    public Map<String, Object> getCustomVariables() {
        return this.customVariables;
    }

    /**
     * Gets a ConfigurationNode, specific to this context, that can be used to store information that should be made
     * available between stages. This context is passed to every stage in one pipeline process.
     *
     * @return ConfigurationNode
     */
    @NotNull
    public ConfigurationNode getProperties() {
        return this.properties;
    }
}
