/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.rythm;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * Special utilities to be made available for Rythm rendering.
 */
public class ChatterboxSpecialUtilities {

    /**
     * A String that signifies the start of a special directive to the renderer.
     */
    private final static String SIGNIFIER = "\u0001\u0002\u0003";

    public static String getSignifier() {
        return ChatterboxSpecialUtilities.SIGNIFIER;
    }

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

    @NotNull
    public String endJSON(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        return this.surround("End JSON " + name.toLowerCase());
    }

    @NotNull
    public String recipient(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        return this.surround("[Recipient section: " + name + "]");
    }

    /**
     * Gets the String that indicates that multiple newlines in a row (regex (\r?\n){2,}) should be replaced by one
     * newline.
     *
     * @return String
     */
    @NotNull
    public String removeMultipleNewlines() {
        return this.surround("Remove multiple newlines");
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

    @NotNull
    public String startJSON(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        return this.surround("Start JSON " + name.toLowerCase());
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
