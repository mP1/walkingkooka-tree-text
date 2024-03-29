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

public final class NormalLengthTest extends LengthTestCase<NormalLength, Void> {

    @Test
    public void testParseInvalidTextFails() {
        this.parseStringFails("12px", IllegalArgumentException.class);
    }

    @Test
    public void testParse() {
        this.parseStringAndCheck("normal", NormalLength.INSTANCE);
    }

    @Test
    public void testWith() {
        assertThrows(UnsupportedOperationException.class, NormalLength.INSTANCE::value);
    }

    @Test
    public void testPixelValueFails() {
        this.pixelLengthFails(NormalLength.INSTANCE);
    }

    // LengthVisitor....................................................................................................

    @Test
    public void testVisitor() {
        final StringBuilder b = new StringBuilder();

        final NormalLength length = this.createLength();
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
            protected void visit(final NormalLength l) {
                assertSame(length, l, "length");
                b.append("3");
            }
        }.accept(length);

        this.checkEquals("132", b.toString());
    }

    // toString........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(NormalLength.INSTANCE, "normal");
    }

    @Override
    NormalLength createLength() {
        return NormalLength.INSTANCE;
    }

    @Override
    Optional<LengthUnit<Void, Length<Void>>> unit() {
        return Optional.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<NormalLength> type() {
        return NormalLength.class;
    }

    // ParseStringTesting...............................................................................................

    @Override
    public NormalLength parseString(final String text) {
        return NormalLength.parseNormal(text);
    }

    // JsonNodeMarshallingTesting...........................................................................................

    @Override
    public NormalLength unmarshall(final JsonNode from,
                                   final JsonNodeUnmarshallContext context) {
        return Length.unmarshallNormal(from);
    }
}
