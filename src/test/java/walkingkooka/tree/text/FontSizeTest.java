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

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FontSizeTest extends TextStylePropertyValueTestCase2<FontSize>
    implements ComparableTesting2<FontSize>,
    ConstantsTesting<FontSize> {

    private final static int VALUE = 10;

    @Test
    public void testWithNegativeValueFails() {
        assertThrows(IllegalArgumentException.class, () -> FontSize.with(-1));
    }

    @Test
    public void testWith() {
        final int value = 10;
        final FontSize size = FontSize.with(value);
        this.checkEquals(value, size.value(), "value");
    }

    @Test
    public void testWithNonConstant() {
        final int value = 1000;
        final FontSize size = FontSize.with(value);
        this.checkEquals(value, size.value(), "value");

        assertNotSame(FontSize.with(value), size);
    }

    // parseValue......................................................................................................

    @Test
    public void testParseValue() {
        this.checkEquals(
            FontSize.with(1),
            this.textStylePropertyName().parseValue("1")
        );
    }

    @Test
    public void testParseValue2() {
        this.checkEquals(
            FontSize.with(23),
            this.textStylePropertyName().parseValue("23")
        );
    }

    // HasJsonNode......................................................................................................

    @Test
    public void testUnmarshallBooleanFails() {
        this.unmarshallFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testUnmarshallStringFails() {
        this.unmarshallFails(JsonNode.string("fails!"));
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
    public void testUnmarshallNumber() {
        final int value = 20;
        this.unmarshallAndCheck(JsonNode.number(value),
            FontSize.with(value));
    }

    @Test
    public void testMarshall() {
        this.marshallAndCheck(this.createComparable(), JsonNode.number(VALUE));
    }

    @Test
    @Override
    public void testMarshallRoundtripTwice() {
        this.marshallRoundTripTwiceAndCheck(this.createObject());
    }

    // Object...........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(FontSize.with(99));
    }

    @Test
    public void testEqualsDifferentNonConstants() {
        this.checkNotEquals(FontSize.with(999), FontSize.with(888));
    }

    @Test
    public void testEqualsNonConstants() {
        final int value = 777;
        this.checkEquals(FontSize.with(value), FontSize.with(value));
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(FontSize.with(10), "10");
    }

    @Override
    TextStylePropertyName<FontSize> textStylePropertyName() {
        return TextStylePropertyName.FONT_SIZE;
    }

    @Override
    FontSize createTextStylePropertyValue() {
        return FontSize.with(VALUE);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<FontSize> type() {
        return FontSize.class;
    }

    // ComparableTesting.................................................................................................

    @Override
    public FontSize createComparable() {
        return this.createTextStylePropertyValue();
    }

    // ConstantsTesting.................................................................................................

    @Override
    public Set<FontSize> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // JsonNodeMarshallTesting..........................................................................................

    @Override
    public FontSize unmarshall(final JsonNode jsonNode,
                               final JsonNodeUnmarshallContext context) {
        return FontSize.unmarshall(jsonNode, context);
    }
}
