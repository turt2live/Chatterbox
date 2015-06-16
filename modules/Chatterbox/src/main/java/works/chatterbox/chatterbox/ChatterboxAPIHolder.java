package works.chatterbox.chatterbox;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.api.ChatterboxAPI;

public interface ChatterboxAPIHolder {

    @NotNull
    ChatterboxAPI getAPI();

}
