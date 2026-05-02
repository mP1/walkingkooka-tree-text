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
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.InvalidCharacterExceptionFactory;
import walkingkooka.text.cursor.parser.LongParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.math.MathContext;
import java.util.Objects;

/**
 * Value class that holds a font weight.
 */
public final class FontWeight implements Comparable<FontWeight>,
    Value<Integer> {

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    private final static int NORMAL_VALUE = 400;
    private final static int BOLD_VALUE = 700;

    // VisibleFor TextStylePropertyValueHandlerFontWeight
    final static String NORMAL_TEXT = "NORMAL";
    final static String BOLD_TEXT = "BOLD";

    /**
     * A constant holding the font-weight of normal text
     */
    public final static FontWeight NORMAL = new FontWeight(NORMAL_VALUE);

    /**
     * A constant holding the font-weight of bold text
     */
    public final static FontWeight BOLD = new FontWeight(BOLD_VALUE);

    public static FontWeight parse(final String text) {
        return CASE_SENSITIVITY.equals(BOLD_TEXT, text) ?
            FontWeight.BOLD :
            CASE_SENSITIVITY.equals(NORMAL_TEXT, text) ?
                FontWeight.NORMAL :
                FontWeight.with(
                    PARSER.parseText(
                            text,
                            PARSER_CONTEXT
                        ).cast(LongParserToken.class)
                        .value()
                        .intValue()
                );
    }

    private final static Parser<ParserContext> PARSER = Parsers.longParser(10);

    private final static ParserContext PARSER_CONTEXT = ParserContexts.basic(
        false, // canNumbersHaveGroupSeparator
        InvalidCharacterExceptionFactory.POSITION,
        ',', // valueSeparator
        DateTimeContexts.fake(),
        DecimalNumberContexts.american(MathContext.DECIMAL32)
    );

    /**
     * Factory that creates a {@link FontWeight}.
     */
    public static FontWeight with(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid font-weight " + value + " < 0");
        }

        return NORMAL_VALUE == value ?
            NORMAL :
            BOLD_VALUE == value ?
                BOLD :
                new FontWeight(value);
    }

    /**
     * Private constructor use static factory
     */
    private FontWeight(final int value) {
        super();
        this.value = value;
    }

    /**
     * Getter that returns the weight value as an integer
     */
    @Override
    public Integer value() {
        return this.value;
    }

    private final int value;

    /**
     * Returns true if this is BOLD.
     */
    public boolean isBold() {
        return this.value == BOLD_VALUE;
    }

    /**
     * Returns true if this is NORMAL.
     */
    public boolean isNormal() {
        return this.value == NORMAL_VALUE;
    }

    // JsonNodeCon .....................................................................................................

    /**
     * Factory that creates a {@link FontWeight} from the given node.
     */
    static FontWeight unmarshall(final JsonNode node,
                                 final JsonNodeUnmarshallContext context) {
        return node.isString() ?
            unmarshallString(node.stringOrFail()) :
            with(node.numberOrFail().intValue());
    }

    private static FontWeight unmarshallString(final String value) {
        return CASE_SENSITIVITY.equals(BOLD_TEXT, value) ?
            BOLD :
            CASE_SENSITIVITY.equals(NORMAL_TEXT, value) ?
                NORMAL :
                unmarshallString0(value);
    }

    private static FontWeight unmarshallString0(final String value) {
        throw new IllegalArgumentException("Unknown font weight " + CharSequences.quote(value));
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return this.isNormal() ?
            NORMAL_JSON :
            this.isBold() ?
                BOLD_JSON :
                JsonNode.number(this.value);
    }

    private final static JsonNode NORMAL_JSON = JsonNode.string(NORMAL_TEXT);
    private final static JsonNode BOLD_JSON = JsonNode.string(BOLD_TEXT);

    static {
        JsonNodeContext.register("font-weight",
            FontWeight::unmarshall,
            FontWeight::marshall,
            FontWeight.class);
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final FontWeight weight) {
        Objects.requireNonNull(weight, "weight");

        return this.value - weight.value;
    }

    // Object .........................................................................................................

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof FontWeight &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final FontWeight other) {
        return this.value == other.value;
    }

    /**
     * Dumps the weight value.
     */
    @Override
    public String toString() {
        final int value = this.value;
        return NORMAL_VALUE == value ?
            NORMAL_TEXT :
            BOLD_VALUE == value ?
                BOLD_TEXT :
                String.valueOf(value);
    }
}
