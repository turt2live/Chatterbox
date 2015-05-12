package works.chatterbox.chatterbox.hooks;

import org.jetbrains.annotations.NotNull;
import ro.fortsoft.pf4j.ExtensionPoint;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.ChatterboxAPI;

public abstract class ChatterboxHook implements ExtensionPoint {

    private Chatterbox chatterbox;

    /**
     * This is called if the hook is ever disabled.
     */
    public void deinit() {
    }

    /**
     * Gets the instance of Chatterbox that is currently loaded.
     *
     * @return Chatterbox
     */
    @NotNull
    public Chatterbox getChatterbox() {
        return this.chatterbox;
    }

    /**
     * Convenience method that is equivalent to {@code getChatterbox().getAPI()}.
     *
     * @return ChatterboxAPI
     */
    @NotNull
    public ChatterboxAPI getChatterboxAPI() {
        return this.getChatterbox().getAPI();
    }

    /**
     * This is called when a hook is first loaded.
     */
    public void init() {
    }

    final void internalInit(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

}
