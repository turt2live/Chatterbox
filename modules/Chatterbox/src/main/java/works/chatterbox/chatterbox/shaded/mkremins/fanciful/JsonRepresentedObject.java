/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.shaded.mkremins.fanciful;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Represents an object that can be serialized to a JSON writer instance.
 */
interface JsonRepresentedObject {

    /**
     * Writes the JSON representation of this object to the specified writer.
     *
     * @param writer The JSON writer which will receive the object.
     * @throws IOException If an error occurs writing to the stream.
     */
    void writeJson(JsonWriter writer) throws IOException;

}
