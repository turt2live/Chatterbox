package works.chatterbox.chatterbox.api;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.localization.Language;

public interface LanguageAPI {

    /**
     * Gets the default Language object for the plugin.
     *
     * @return Language
     */
    @NotNull
    Language getLanguage();

    /**
     * Gets the Language object for this CommandSender, based on its locale. If the locale or corresponding language
     * file could not be found, returns the default language for the plugin.
     *
     * @param cs CommandSender to get Language for
     * @return Language or default Language
     * @see #getLanguage()
     */
    @NotNull
    Language getLanguage(@NotNull final CommandSender cs);

    /**
     * Gets the Language object for the given locale String. If the language file for this locale cannot be found,
     * returns {@code null}.
     * <p>This will get a base language if locales are not available. This will get a locale if the requested base or
     * locale is not available.
     * <p>If "en" is the locale to be returned, but only "en_US" is available, "en_US" will be returned<br>
     * If "en_US" is the locale to be returned, but only "en_GB" is available, "en_GB" will be returned<br>
     * If "en_US" is the locale to be returned, but only "en" is available, "en" will be returned
     *
     * @param locale Locale string, like "en" or "en_US"
     * @return Language or {@code null}
     */
    @Nullable
    Language getLanguage(@NotNull final String locale);

    /**
     * Gets the default locale String for the plugin.
     *
     * @return Locale string, like "en" or "en_US"
     */
    @NotNull
    String getLocale();

    /**
     * Gets the locale for this CommandSender. If its locale could not be determined, this returns null.
     *
     * @param cs CommandSender to get locale for
     * @return Locale string
     * @see #getLocale()
     */
    @Nullable
    String getLocale(@NotNull final CommandSender cs);

    /**
     * Gets the result of {@link #getLocale(CommandSender)}, and if that is null, returns the result of
     * {@link #getLocale()}.
     *
     * @param cs CommandSender to get locale for
     * @return Locale string
     */
    @NotNull
    String getLocaleOrDefault(@NotNull final CommandSender cs);

}
