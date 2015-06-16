package works.chatterbox.chatterbox.api;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.titles.Titles;

import java.util.UUID;

public interface TitleAPI {

    @NotNull
    Titles getTitles(@NotNull UUID uuid);

    void invalidate(@NotNull UUID uuid);
}
