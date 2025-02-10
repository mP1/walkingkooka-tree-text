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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FontWeightTest extends TextStylePropertyValueTestCase2<FontWeight>
    implements ComparableTesting2<FontWeight>, ConstantsTesting<FontWeight> {

    private final static int VALUE = 456;

    @Test
    public void testWithNegativeValueFails() {
        assertThrows(IllegalArgumentException.class, () -> FontWeight.with(-1));
    }

    @Test
    public void testWith() {
        final int value = 400;
        final FontWeight size = FontWeight.with(value);
        this.checkEquals(value, size.value(), "value");
    }

    @Test
    public void testBold() {
        assertSame(FontWeight.BOLD, FontWeight.with(FontWeight.BOLD.value()));
    }

    @Test
    public void testNormal() {
        assertSame(FontWeight.NORMAL, FontWeight.with(FontWeight.NORMAL.value()));
    }

    // parseValue......................................................................................................

    @Test
    public void testParseValueBold() {
        this.checkEquals(
            FontWeight.BOLD,
            this.textStylePropertyName().parseValue("bold")
        );
    }

    @Test
    public void testParseValueNumber() {
        this.checkEquals(
            FontWeight.with(23),
            this.textStylePropertyName().parseValue("23")
        );
    }

    // HasJsonNode......................................................................................................

    @Test
    public void testUnmarshallBooleanFails() {
        this.unmarshallFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testUnmarshallInvalidStringFails() {
        this.unmarshallFails(JsonNode.string("not bold or normal"));
    }

    @Test
    public void testUnmarshallArrayFails() {
        this.unmarshallFails(JsonNode.array());
    }

    @Test
    public void testUnmarshallObjectFails() {
        this.unmarshallFails(JsonNode.object());
    }

    @Test
    public void testUnmarshallNumberInvalidFails() {
        this.unmarshallFails(JsonNode.number(-1));
    }

    @Test
    public void testUnmarshallBold() {
        this.unmarshallAndCheck(JsonNode.string("bold"),
            FontWeight.BOLD);
    }

    @Test
    public void testUnmarshallNormal() {
        this.unmarshallAndCheck(JsonNode.string("normal"),
            FontWeight.NORMAL);
    }

    @Test
    public void testUnmarshallNormal2() {
        this.unmarshallAndCheck(JsonNode.array()
                .appendChild(JsonNode.string("normal"))
                .get(0),
            FontWeight.NORMAL);
    }

    @Test
    public void testUnmarshallNumber() {
        final int value = 20;
        this.unmarshallAndCheck(JsonNode.number(value),
            FontWeight.with(value));
    }

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
            this.createComparable(),
            JsonNode.number(VALUE)
        );
    }

    @Test
    public void testMarshallBold() {
        this.marshallAndCheck(
            FontWeight.BOLD,
            JsonNode.string(FontWeight.BOLD_TEXT)
        );
    }

    @Test
    public void testMarshallNormal() {
        this.marshallAndCheck(
            FontWeight.NORMAL,
            JsonNode.string(FontWeight.NORMAL_TEXT)
        );
    }

    @Test
    @Override
    public void testMarshallRoundtripTwice() {
        this.marshallRoundTripTwiceAndCheck(this.createObject());
    }

    @Test
    public void testMarshallRoundtripBold() {
        this.marshallRoundTripTwiceAndCheck(FontWeight.BOLD);
    }

    @Test
    public void testMarshallRoundtripNormal() {
        this.marshallRoundTripTwiceAndCheck(FontWeight.NORMAL);
    }

    // Object...........................................................................................................

    @Test
    public void testToStringNormal() {
        this.toStringAndCheck(FontWeight.NORMAL, "normal");
    }

    @Test
    public void testToStringBold() {
        this.toStringAndCheck(FontWeight.BOLD, "bold");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(FontWeight.with(456), "456");
    }

    @Override
    FontWeight createTextStylePropertyValue() {
        return FontWeight.with(VALUE);
    }

    @Override
    TextStylePropertyName<FontWeight> textStylePropertyName() {
        return TextStylePropertyName.FONT_WEIGHT;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<FontWeight> type() {
        return FontWeight.class;
    }

    // ComparableTesting................................................................................................

    @Override
    public FontWeight createComparable() {
        return this.createTextStylePropertyValue();
    }

    // ConstantsTesting.................................................................................................

    @Override
    public Set<FontWeight> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // JsonNodeMarshallingTesting...........................................................................................

    @Override
    public FontWeight unmarshall(final JsonNode jsonNode,
                                 final JsonNodeUnmarshallContext context) {
        return FontWeight.unmarshall(jsonNode, context);
    }
}
