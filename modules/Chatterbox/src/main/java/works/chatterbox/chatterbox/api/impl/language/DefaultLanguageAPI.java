package works.chatterbox.chatterbox.api.impl.language;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.LanguageAPI;
import works.chatterbox.chatterbox.commands.CommandUtils;
import works.chatterbox.chatterbox.localization.Language;
import works.chatterbox.chatterbox.localization.LanguageBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultLanguageAPI implements LanguageAPI {

    private final Chatterbox chatterbox;
    private final Cache<String, Language> languages = CacheBuilder.newBuilder()
        .build();

    public DefaultLanguageAPI(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    @Nullable
    private String findLocale(@NotNull final String base) {
        Preconditions.checkNotNull(base, "base was null");
        final String baseBase = base.split("_")[0];
        final File langFolder = new File(this.chatterbox.getDataFolder(), "lang");
        final List<String> matches = Arrays.stream(langFolder.list())
            .filter(file -> file.startsWith(baseBase))
            .map(file -> {
                final String[] parts = file.split("\\.");
                return CommandUtils.joinUntil(parts, parts.length - 1);
            })
            .collect(Collectors.toList());
        if (matches.isEmpty()) return null;
        if (matches.contains(base)) return base;
        if (matches.contains(baseBase)) return baseBase;
        return matches.get(0);
    }

    @NotNull
    @Override
    public Language getLanguage() {
        return Preconditions.checkNotNull(
            this.getLanguage(this.getLocale()),
            "The default language could not be loaded"
        );
    }

    @NotNull
    @Override
    public Language getLanguage(@NotNull final CommandSender cs) {
        Preconditions.checkNotNull(cs, "cs was null");
        return Optional.ofNullable(this.getLanguage(this.getLocaleOrDefault(cs))).orElseGet(this::getLanguage);
    }

    @Nullable
    @Override
    public Language getLanguage(@NotNull final String locale) {
        Preconditions.checkNotNull(locale, "locale was null");
        final String matchedLocale = Optional.ofNullable(this.findLocale(locale)).orElse(locale);
        final Language cached = this.languages.getIfPresent(matchedLocale);
        if (cached != null) {
            return cached;
        }
        try {
            final String langLocation = String.format(
                "lang/%s.properties",
                matchedLocale
            );
            final Language language = LanguageBuilder.create()
                .languageFile(new File(this.chatterbox.getDataFolder(), langLocation))
                .languageFolder("lang")
                .saveLanguageFiles(true)
                .dataFolderHolder(this.chatterbox)
                .build();
            this.languages.put(matchedLocale, language);
            return language;
        } catch (final IOException ex) {
            return null;
        }
    }

    @NotNull
    @Override
    public String getLocale() {
        return Preconditions.checkNotNull(
            this.chatterbox.getConfiguration().getNode("language").getNode("locale").getString(),
            "No default locale was specified"
        );
    }

    @Nullable
    @Override
    public String getLocale(@NotNull final CommandSender cs) {
        if (!(cs instanceof Player)) {
            return this.getLocale();
        }
        final Player player = (Player) cs;
        try {
            final Method m = player.getClass().getDeclaredMethod("getHandle");
            final Object handle = m.invoke(player);
            return (String) handle.getClass().getDeclaredField("locale").get(handle);
        } catch (final ReflectiveOperationException ex) {
            return null;
        }
    }

    @NotNull
    @Override
    public String getLocaleOrDefault(@NotNull final CommandSender cs) {
        return Optional.ofNullable(this.getLocale(cs)).orElseGet(this::getLocale);
    }
}
