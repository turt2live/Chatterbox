package works.chatterbox.chatterbox.api;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.player.PlayerAPI;

public class ChatterboxAPI {

    private final Chatterbox chatterbox;
    private final PlayerAPI playerAPI = new PlayerAPI();

    public ChatterboxAPI(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    /**
     * Gets the player API, which has methods pertaining to players and their various functions in Chatterbox.
     *
     * @return PlayerAPI
     */
    @NotNull
    public PlayerAPI getPlayerAPI() {
        return this.playerAPI;
    }
}
