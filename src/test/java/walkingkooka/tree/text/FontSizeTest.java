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
import walkingkooka.test.ConstantsTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertEquals(value, size.value(), "value");
    }

    @Test
    public void testWithNonConstant() {
        final int value = 1000;
        final FontSize size = FontSize.with(value);
        assertEquals(value, size.value(), "value");

        assertNotSame(FontSize.with(value), size);
    }

    // HasJsonNode......................................................................................

    @Test
    public void testJsonNodeUnmarshallBooleanFails() {
        this.unmarshallFails(JsonNode.booleanNode(true), JsonNodeException.class);
    }

    @Test
    public void testJsonNodeUnmarshallStringFails() {
        this.unmarshallFails(JsonNode.string("fails!"), JsonNodeException.class);
    }

    @Test
    public void testJsonNodeUnmarshallArrayFails() {
        this.unmarshallFails(JsonNode.array(), JsonNodeException.class);
    }

    @Test
    public void testJsonNodeUnmarshallObjectFails() {
        this.unmarshallFails(JsonNode.object(), JsonNodeException.class);
    }

    @Test
    public void testJsonNodeUnmarshallNumberInvalidFails() {
        this.unmarshallFails(JsonNode.number(-1), IllegalArgumentException.class);
    }

    @Test
    public void testFromJsonNumber() {
        final int value = 20;
        this.unmarshallAndCheck(JsonNode.number(value),
                FontSize.with(value));
    }

    @Test
    public void testJsonNodeMarshall() {
        this.marshallAndCheck(this.createComparable(), JsonNode.number(VALUE));
    }

    @Test
    public void testJsonNodeMarshallRoundtripTwice() {
        this.marshallRoundTripTwiceAndCheck(this.createObject());
    }

    // Object...........................................................................................................

    @Test
    public void testDifferent() {
        this.checkNotEquals(FontSize.with(99));
    }

    @Test
    public void testDifferentNonConstants() {
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
