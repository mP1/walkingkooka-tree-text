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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.visit.Visiting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NoneLengthTest extends LengthTestCase<NoneLength, Void> {

    @Test
    public void testParseInvalidTextFails() {
        this.parseStringFails("12px", IllegalArgumentException.class);
    }

    @Test
    public void testParse() {
        this.parseStringAndCheck("none", NoneLength.INSTANCE);
    }

    @Test
    public void testWith() {
        assertThrows(UnsupportedOperationException.class, NoneLength.INSTANCE::value);
    }

    @Test
    public void testPixelValue() {
        this.pixelLengthAndCheck(NoneLength.INSTANCE, 0);
    }

    // LengthVisitor....................................................................................................

    @Test
    public void testVisitor() {
        final StringBuilder b = new StringBuilder();

        final NoneLength length = this.createLength();
        new FakeLengthVisitor() {
            @Override
            protected Visiting startVisit(final Length<?> l) {
                assertSame(length, l, "length");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Length<?> l) {
                assertSame(length, l, "length");
                b.append("2");
            }

            @Override
            protected void visit(final NoneLength l) {
                assertSame(length, l, "length");
                b.append("3");
            }
        }.accept(length);

        this.checkEquals("132", b.toString());
    }

    // toString........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(NoneLength.INSTANCE, "none");
    }

    @Override
    NoneLength createLength() {
        return NoneLength.INSTANCE;
    }

    @Override
    Optional<LengthUnit<Void, Length<Void>>> unit() {
        return Optional.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<NoneLength> type() {
        return NoneLength.class;
    }

    // ParseStringTesting...............................................................................................

    @Override
    public NoneLength parseString(final String text) {
        return Length.parseNone(text);
    }

    // JsonNodeMarshallTesting..........................................................................................

    @Override
    public NoneLength unmarshall(final JsonNode from,
                                 final JsonNodeUnmarshallContext context) {
        return Length.unmarshallNone(from);
    }
}
