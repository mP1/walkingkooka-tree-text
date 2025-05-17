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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OpacityTest implements ClassTesting2<Opacity>,
    ComparableTesting2<Opacity>,
    ConstantsTesting<Opacity>,
    JsonNodeMarshallingTesting<Opacity>,
    ToStringTesting<Opacity> {

    private final static double VALUE = 0.25;

    @Test
    public void testWithNegativeValueFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> Opacity.with(-0.1)
        );

        this.checkEquals(
            "Invalid value -0.1 not between 0.0 and 1.0",
            thrown.getMessage()
        );
    }

    @Test
    public void testWithGreaterValueFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> Opacity.with(1.01)
        );

        this.checkEquals(
            "Invalid value 1.01 not between 0.0 and 1.0",
            thrown.getMessage()
        );
    }

    @Test
    public void testWith() {
        final double value = 0.5;
        final Opacity size = Opacity.with(value);
        this.checkEquals(value, size.value(), "value");
    }

    @Test
    public void testTransparent() {
        assertSame(Opacity.TRANSPARENT, Opacity.with(Opacity.TRANSPARENT.value()));
    }

    @Test
    public void testOpaque() {
        assertSame(Opacity.OPAQUE, Opacity.with(Opacity.OPAQUE.value()));
    }

    // HasJsonNode......................................................................................

    @Test
    public void testUnmarshallBooleanFails() {
        this.unmarshallFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testUnmarshallInvalidStringFails() {
        this.unmarshallFails(JsonNode.string("not transparent or opaque"));
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
    public void testUnmarshallTransparent() {
        this.unmarshallAndCheck(JsonNode.string("transparent"),
            Opacity.TRANSPARENT);
    }

    @Test
    public void testUnmarshallOpaque() {
        this.unmarshallAndCheck(JsonNode.string("opaque"),
            Opacity.OPAQUE);
    }

    @Test
    public void testUnmarshallOpaque2() {
        this.unmarshallAndCheck(JsonNode.array()
                .appendChild(JsonNode.string("opaque"))
                .get(0),
            Opacity.OPAQUE);
    }

    @Test
    public void testUnmarshallNumber() {
        final double value = 0.25;
        this.unmarshallAndCheck(JsonNode.number(value),
            Opacity.with(value));
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

    @Test
    public void testMarshallRoundtripTransparent() {
        this.marshallRoundTripTwiceAndCheck(Opacity.TRANSPARENT);
    }

    @Test
    public void testMarshallRoundtripOpaque() {
        this.marshallRoundTripTwiceAndCheck(Opacity.OPAQUE);
    }

    // Object...........................................................................................................

    @Test
    public void testToStringOpaque() {
        this.toStringAndCheck(Opacity.OPAQUE, "opaque");
    }

    @Test
    public void testToStringTransparent() {
        this.toStringAndCheck(Opacity.TRANSPARENT, "transparent");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(Opacity.with(0.25), "25%");
    }

    @Override
    public Set<Opacity> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    @Override
    public Opacity createComparable() {
        return Opacity.with(VALUE);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<Opacity> type() {
        return Opacity.class;
    }

    // JsonNodeMarshallTesting..........................................................................................

    @Override
    public Opacity createJsonNodeMarshallingValue() {
        return this.createObject();
    }

    @Override
    public Opacity unmarshall(final JsonNode jsonNode,
                              final JsonNodeUnmarshallContext context) {
        return Opacity.unmarshall(jsonNode, context);
    }
}
