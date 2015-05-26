/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.channels.radius;

/**
 * A radius, used for {@link works.chatterbox.chatterbox.channels.Channel Channel}s.
 */
public class Radius {

    private final double horizontal, vertical;

    public Radius(final double horizontal, final double vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    /**
     * The horizontal component of this radius.
     *
     * @return Horizontal
     */
    public double getHorizontal() {
        return this.horizontal;
    }

    /**
     * The vertical component of this radius.
     *
     * @return Radius
     */
    public double getVertical() {
        return this.vertical;
    }
}
