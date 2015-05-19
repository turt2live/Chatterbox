package works.chatterbox.chatterbox.localization;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

public class Language extends PropertyResourceBundle {

    public Language(final InputStream stream) throws IOException {
        super(stream);
    }

    public Language(final Reader reader) throws IOException {
        super(reader);
    }

    @Nullable
    public String getFormattedString(@NotNull final String key, @NotNull final Object... objects) {
        Preconditions.checkNotNull(key, "key was null");
        Preconditions.checkNotNull(objects, "objects was null");
        final String string;
        try {
            string = this.getString(key);
        } catch (final MissingResourceException ex) {
            return null;
        }
        return String.format(string, objects);
    }
}
