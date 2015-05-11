package org.royaldev.chatterbox;

import org.bukkit.plugin.java.JavaPlugin;
import org.royaldev.chatterbox.commands.ReflectiveCommandRegistrar;
import org.royaldev.chatterbox.pipeline.MessagePipeline;
import org.royaldev.chatterbox.pipeline.stages.impl.color.ColorStage;
import org.royaldev.chatterbox.pipeline.stages.impl.rythm.RythmStage;

import java.util.Arrays;

public class Chatterbox extends JavaPlugin {

    // TODO: Should this go somewhere else? Perhaps the somewhere in the API.
    private final MessagePipeline pipeline = new MessagePipeline();

    private void addInternalPipelineStages() {
        Arrays.asList(
            new RythmStage(),
            new ColorStage()
        ).forEach(this.pipeline::addStage);
    }

    @Override
    public void onEnable() {
        final ReflectiveCommandRegistrar<Chatterbox> rcr = new ReflectiveCommandRegistrar<>(this);
        rcr.registerCommands();
        this.addInternalPipelineStages();
    }
}
