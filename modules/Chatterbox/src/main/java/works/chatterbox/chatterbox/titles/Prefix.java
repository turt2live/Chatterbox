/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.titles;

public class Prefix extends Title {

    private final String title;

    public Prefix(final String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }
}
