/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.tree.text;

import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.util.Optional;

final class TextOverflowNonString extends TextOverflow {

    /**
     * Factory that creates a constant.
     */
    static TextOverflowNonString constant(final String value) {
        return new TextOverflowNonString(value);
    }

    /**
     * Private constructor use static factory
     */
    private TextOverflowNonString(final String value) {
        super();
        this.value = value;
        this.jsonNode = JsonNode.string(value);
    }

    @Override
    public Optional<String> value() {
        return Optional.empty();
    }

    // isXXX............................................................................................................

    @Override
    public boolean isClip() {
        return CLIP_TEXT.equals(this.value);
    }

    @Override
    public boolean isEllipse() {
        return ELLIPSIS_TEXT.equals(this.value);
    }

    // Object .........................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof TextOverflowNonString &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextOverflowNonString other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    final String value;

    // JsonNodeContext..................................................................................................

    @Override
    JsonNode marshall(final JsonNodeMarshallContext context) {
        return this.jsonNode;
    }

    private final JsonNode jsonNode;
}
