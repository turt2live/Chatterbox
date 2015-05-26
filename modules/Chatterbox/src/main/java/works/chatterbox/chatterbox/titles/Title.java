/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.titles;

public abstract class Title {

    abstract public String getTitle();

    @Override
    public String toString() {
        return this.getTitle();
    }
}
