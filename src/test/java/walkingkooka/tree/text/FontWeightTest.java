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
import walkingkooka.test.SerializationTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FontWeightTest extends TextStylePropertyValueTestCase2<FontWeight>
        implements ComparableTesting2<FontWeight>,
        ConstantsTesting<FontWeight>,
        SerializationTesting<FontWeight> {

    private final static int VALUE = 456;

    @Test
    public void testWithNegativeValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            FontWeight.with(-1);
        });
    }

    @Test
    public void testWith() {
        final int value = 400;
        final FontWeight size = FontWeight.with(value);
        assertEquals(value, size.value(), "value");
    }

    @Test
    public void testBold() {
        assertSame(FontWeight.BOLD, FontWeight.with(FontWeight.BOLD.value()));
    }

    @Test
    public void testNormal() {
        assertSame(FontWeight.NORMAL, FontWeight.with(FontWeight.NORMAL.value()));
    }

    // HasJsonNode......................................................................................

    @Test
    public void testJsonNodeUnmarshallBooleanFails() {
        this.unmarshallFails(JsonNode.booleanNode(true), JsonNodeException.class);
    }

    @Test
    public void testJsonNodeUnmarshallInvalidStringFails() {
        this.unmarshallFails(JsonNode.string("not bold or normal"), IllegalArgumentException.class);
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
    public void testFromJsonBold() {
        this.unmarshallAndCheck(JsonNode.string("bold"),
                FontWeight.BOLD);
    }

    @Test
    public void testFromJsonNormal() {
        this.unmarshallAndCheck(JsonNode.string("normal"),
                FontWeight.NORMAL);
    }

    @Test
    public void testFromJsonNormal2() {
        this.unmarshallAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string("normal"))
                        .get(0),
                FontWeight.NORMAL);
    }

    @Test
    public void testFromJsonNumber() {
        final int value = 20;
        this.unmarshallAndCheck(JsonNode.number(value),
                FontWeight.with(value));
    }

    @Test
    public void testJsonNodeMarshall() {
        this.marshallAndCheck(this.createComparable(), JsonNode.number(VALUE));
    }

    @Test
    public void testJsonNodeMarshallRoundtripTwice() {
        this.marshallRoundTripTwiceAndCheck(this.createObject());
    }

    @Test
    public void testJsonNodeMarshallRoundtripBold() {
        this.marshallRoundTripTwiceAndCheck(FontWeight.BOLD);
    }

    @Test
    public void testJsonNodeMarshallRoundtripNormal() {
        this.marshallRoundTripTwiceAndCheck(FontWeight.NORMAL);
    }

    // Serializable.....................................................................................................

    @Test
    public void testSerializeBold() throws Exception {
        this.serializeSingletonAndCheck(FontWeight.BOLD);
    }

    @Test
    public void testSerializeNormal() throws Exception {
        this.serializeSingletonAndCheck(FontWeight.NORMAL);
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

    // SerializationTesting.............................................................................................

    @Override
    public FontWeight serializableInstance() {
        return this.createComparable();
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
