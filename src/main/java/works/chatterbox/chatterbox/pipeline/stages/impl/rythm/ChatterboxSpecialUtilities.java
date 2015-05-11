package works.chatterbox.chatterbox.pipeline.stages.impl.rythm;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

// TODO: Maybe make these methods one enum?

/**
 * Special utilities to be made available for Rythm rendering.
 */
public class ChatterboxSpecialUtilities {

    /**
     * A String that signifies the start of a special directive to the renderer.
     */
    private final static String SIGNIFIER = "\u0001\u0002\u0003";

    /**
     * Surrounds anything with the signifier String.
     *
     * @param something Thing to be surrounded
     * @return Thing surrounded
     */
    @NotNull
    private String surround(@NotNull final Object something) {
        Preconditions.checkNotNull(something, "something was null");
        return ChatterboxSpecialUtilities.SIGNIFIER + something.toString() + ChatterboxSpecialUtilities.SIGNIFIER;
    }

    /**
     * Gets the String that indicates that the JSON that normally would have been applied should be disregarded.
     *
     * @return String
     */
    @NotNull
    public String cancelJSON() {
        return this.surround("Cancel JSON");
    }

    /**
     * Gets the String that indicates that whatever is being rendered at the moment should be ignored.
     *
     * @return String
     */
    @NotNull
    public String doNotRender() {
        return this.surround("Do not render");
    }

    /**
     * Gets the String that indicates that multiple spaces in a row (regex \s{2,}) should be replaced by one space.
     *
     * @return String
     */
    @NotNull
    public String removeMultipleSpaces() {
        return this.surround("Remove multiple spaces");
    }

    /**
     * Gets the String that indicates that the {@link String#trim()} method should be run on the result.
     *
     * @return String
     */
    @NotNull
    public String trim() {
        return this.surround("Trim");
    }

}
