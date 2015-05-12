package works.chatterbox.chatterbox.api;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.message.MessageAPI;
import works.chatterbox.chatterbox.api.player.PlayerAPI;

public class ChatterboxAPI {

    private final Chatterbox chatterbox;
    private final PlayerAPI playerAPI;
    private final MessageAPI messageAPI;

    public ChatterboxAPI(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
        this.playerAPI = new PlayerAPI(this.chatterbox);
        this.messageAPI = new MessageAPI(this.chatterbox);
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

    @NotNull
    public MessageAPI getMessageAPI() {
        return this.messageAPI;
    }
}
