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
import walkingkooka.Value;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Value class that holds a font size.
 */
public final class FontSize implements Comparable<FontSize>,
        Value<Integer> {

    private final static int CONSTANT_COUNT = 40;

    /**
     * A read only cache of already prepared {@link FontSize sizes}.
     */
    private final static FontSize[] CONSTANTS = registerConstants();

    /**
     * Creates and adds a new {@link FontSize} to the cache being built.
     */
    private static FontSize[] registerConstants() {
        return IntStream.rangeClosed(0, CONSTANT_COUNT)
                .mapToObj(FontSize::new)
                .toArray(FontSize[]::new);
    }

    /**
     * Factory that creates a {@link FontSize}.
     */
    public static FontSize with(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid value=" + value);
        }

        return value < CONSTANT_COUNT ?
                CONSTANTS[value] :
                new FontSize(value);
    }

    /**
     * Private constructor use static factory
     */
    private FontSize(final int value) {
        super();
        this.value = value;
    }

    /**
     * Getter that returns the size value as an integer
     */
    @Override
    public Integer value() {
        return this.value;
    }

    private final int value;

    // JsonNodeContext..................................................................................................

    /**
     * Factory that creates a {@link FontSize} from the given node.
     */
    static FontSize unmarshall(final JsonNode node,
                               final JsonNodeUnmarshallContext context) {
        return with(node.numberOrFail().intValue());
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.number(this.value);
    }

    static {
        JsonNodeContext.register("font-size",
                FontSize::unmarshall,
                FontSize::marshall,
                FontSize.class);
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final FontSize size) {
        Objects.requireNonNull(size, "size");

        return this.value - size.value;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof FontSize &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final FontSize other) {
        return this.value == other.value;
    }

    /**
     * Dumps the size value.
     */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
