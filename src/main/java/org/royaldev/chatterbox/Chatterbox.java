package org.royaldev.chatterbox;

import org.bukkit.plugin.java.JavaPlugin;
import org.royaldev.chatterbox.api.ChatterboxAPI;
import org.royaldev.chatterbox.commands.ReflectiveCommandRegistrar;
import org.royaldev.chatterbox.pipeline.MessagePipeline;
import org.royaldev.chatterbox.pipeline.stages.impl.color.ColorStage;
import org.royaldev.chatterbox.pipeline.stages.impl.rythm.RythmStage;

import java.util.Arrays;

// TODO: Make task for loading dependencies, which brings me to number two
// TODO: Dependencies

public class Chatterbox extends JavaPlugin {

    // TODO: Move to API
    private final MessagePipeline pipeline = new MessagePipeline();
    private ChatterboxAPI api;

    private void addInternalPipelineStages() {
        Arrays.asList(
            new RythmStage(),
            new ColorStage()
        ).forEach(this.pipeline::addStage);
    }

    public ChatterboxAPI getAPI() {
        return this.api;
    }

    @Override
    public void onEnable() {
        this.api = new ChatterboxAPI(this);
        final ReflectiveCommandRegistrar<Chatterbox> rcr = new ReflectiveCommandRegistrar<>(this);
        rcr.registerCommands();
        this.addInternalPipelineStages();
    }
}
