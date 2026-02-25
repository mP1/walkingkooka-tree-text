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
import walkingkooka.InvalidCharacterException;
import walkingkooka.Value;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.HasText;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallException;

import java.util.Objects;

/**
 * Value class that holds an opacity value.
 */
public final class Opacity implements Comparable<Opacity>,
    Value<Double>,
    HasText {

    private final static double TRANSPARENT_VALUE = 0;
    private final static double OPAQUE_VALUE = 1;

    final static String TRANSPARENT_TEXT = "transparent";
    final static String OPAQUE_TEXT = "opaque";

    private final static double PERCENTAGE_FACTOR = 100;

    /**
     * A constant holding the opacity of TRANSPARENT text
     */
    public final static Opacity TRANSPARENT = new Opacity(TRANSPARENT_VALUE);

    /**
     * A constant holding the opacity of OPAQUE text
     */
    public final static Opacity OPAQUE = new Opacity(OPAQUE_VALUE);

    private static final CharacterConstant PERCENT = CharacterConstant.with('%');

    /**
     * Parses the opacity which includes support as a percentage.
     * <pre>
     * opaque
     * transparent
     * 25%
     * 0.25
     * </pre>
     */
    public static Opacity parse(final String text) {
        final Opacity opacity;

        if (TRANSPARENT_TEXT.equals(text)) {
            opacity = TRANSPARENT;
        } else {
            if (OPAQUE_TEXT.equals(text)) {
                opacity = OPAQUE;
            } else {
                CharSequences.failIfNullOrEmpty(text, "text");

                final int length = text.length();
                final int last = length - 1;

                if (PERCENT.character() == text.charAt(last)) {
                    opacity = with(
                        parseDouble(
                            text.substring(
                                0,
                                last
                            )
                        ) / PERCENTAGE_FACTOR
                    );
                } else {
                    opacity = with(
                        parseDouble(text)
                    );
                }
            }
        }

        return opacity;
    }

    private static Double parseDouble(final String text) {
        try {
            return Double.parseDouble(text);
        } catch (final NumberFormatException cause) {
            final int length = text.length();

            for (int i = 0; i < length; i++) {
                final char c = text.charAt(i);
                if (c >= '0' && c <= '9') {
                    continue;
                }
                if ('.' == c) {
                    continue;
                }
                throw new InvalidCharacterException(text, i);
            }

            throw new InvalidCharacterException(text, 0);
        }
    }

    /**
     * Factory that creates a {@link Opacity}.
     */
    public static Opacity with(final double value) {
        if (value < 0 || value > 1.0) {
            throw new IllegalArgumentException("Invalid value " + value + " not between 0.0 and 1.0");
        }

        return TRANSPARENT_VALUE == value ?
            TRANSPARENT :
            OPAQUE_VALUE == value ?
                OPAQUE :
                new Opacity(value);
    }

    /**
     * Private constructor use static factory
     */
    private Opacity(final double value) {
        super();
        this.value = value;
    }

    /**
     * Getter that returns the weight value as an Double
     */
    @Override
    public Double value() {
        return this.value;
    }

    private final double value;

    // HasText..........................................................................................................

    @Override
    public String text() {
        return this.marshall(null).text();
    }

    // JsonNodeContext..................................................................................................

    /**
     * Factory that creates a {@link Opacity} from the given node.
     */
    static Opacity unmarshall(final JsonNode node,
                              final JsonNodeUnmarshallContext context) {
        return node.isString() ?
            unmarshallString(node.stringOrFail(), node) :
            with(node.numberOrFail().doubleValue());
    }

    private static Opacity unmarshallString(final String value,
                                            final JsonNode node) {
        return OPAQUE_TEXT.equals(value) ? OPAQUE :
            TRANSPARENT_TEXT.equals(value) ? TRANSPARENT :
                failUnknownOpacity(value, node);
    }

    private static Opacity failUnknownOpacity(final String value, final JsonNode node) {
        throw new JsonNodeUnmarshallException("Unknown opacity " + CharSequences.quote(value), node);
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return TRANSPARENT_VALUE == this.value ?
            TRANSPARENT_JSON :
            OPAQUE_VALUE == this.value ?
                OPAQUE_JSON :
                JsonNode.number(this.value);
    }

    private final static JsonNode TRANSPARENT_JSON = JsonNode.string(TRANSPARENT_TEXT);
    private final static JsonNode OPAQUE_JSON = JsonNode.string(OPAQUE_TEXT);

    static {
        JsonNodeContext.register("opacity",
            Opacity::unmarshall,
            Opacity::marshall,
            Opacity.class);
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final Opacity other) {
        Objects.requireNonNull(other, "other");

        return Double.compare(this.value, other.value);
    }

    // Object .........................................................................................................

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof Opacity &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final Opacity other) {
        return this.value == other.value;
    }

    /**
     * Dumps the opacity as a percentage.
     * <pre>
     * opaque
     * transparent
     * 25%
     * </pre>
     */
    @Override
    public String toString() {
        return TRANSPARENT == this ?
            TRANSPARENT_TEXT :
            OPAQUE == this ?
                OPAQUE_TEXT :
                formatPercentage();
    }

    private String formatPercentage() {
        final String percentage = "" + PERCENTAGE_FACTOR * this.value;
        return (percentage.endsWith(".0") ?
            percentage.substring(0, percentage.length() - 2) :
            percentage).concat(PERCENT.string());
    }
}
