/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.commands;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReflectCommand {

    /**
     * Gets an array of names that can be used for this command.
     *
     * @return Array
     */
    @NotNull String[] aliases() default {};

    /**
     * A description of what this command does.
     *
     * @return Description
     */
    @NotNull String description() default "";

    @NotNull String[] keys() default {};

    /**
     * The name of this command.
     *
     * @return Name
     */
    @NotNull String name();

    /**
     * The usage of this command.
     *
     * @return Usage
     */
    @NotNull String usage() default "/<command>";

}
