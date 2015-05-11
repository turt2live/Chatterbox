package org.royaldev.chatterbox.pipeline.stages.impl.rythm;

import com.google.common.collect.Maps;
import org.rythmengine.RythmEngine;

import java.util.Map;

public class RythmStage {

    private final RythmEngine rythm;

    public RythmStage() {
        final Map<String, Object> vars = Maps.newHashMap();
        vars.put("chatterbox", new ChatterboxSpecialUtilities());
        this.rythm = new RythmEngine(vars);
    }

}
