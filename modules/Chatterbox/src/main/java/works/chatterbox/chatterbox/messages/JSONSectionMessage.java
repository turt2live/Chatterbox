/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.messages;

import org.jetbrains.annotations.NotNull;

public class JSONSectionMessage extends SectionMessage {

    public JSONSectionMessage(@NotNull final Message base, @NotNull final String section) {
        super(base, section);
    }
}
