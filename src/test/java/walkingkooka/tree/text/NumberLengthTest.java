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

public final class NumberLengthTest extends LengthTestCase<NumberLength, Double> {

    @Test
    public void testParseInvalidNumberFails() {
        this.parseStringInvalidCharacterFails(
            "A",
            'A'
        );
    }

    @Test
    public void testParseIncorrectUnitFails() {
        this.parseStringFails(
            "12EM",
            IllegalArgumentException.class
        );
    }

    @Test
    public void testParseIncorrectUnitCaseFails() {
        this.parseStringFails(
            "12PX",
            IllegalArgumentException.class
        );
    }

    @Test
    public void testParse() {
        this.parseStringAndCheck(
            "12",
            NumberLength.with(12.0)
        );
    }

    @Test
    public void testParseWithDecimal() {
        this.parseStringAndCheck(
            "12.5",
            NumberLength.with(12.5)
        );
    }

    @Override
    public NumberLength parseString(final String text) {
        return NumberLength.parseNumber(text);
    }

    // with.............................................................................................................

    @Test
    public void testWith() {
        final Double value = 12.5;

        this.valueAndCheck(
            NumberLength.with(value),
            value
        );
    }

    @Test
    public void testWithZero() {
        final Double value = 0.0;

        this.valueAndCheck(
            NumberLength.with(value),
            value
        );
    }

    @Test
    public void testWithNegative() {
        final Double value = -23.4;

        this.valueAndCheck(
            NumberLength.with(value),
            value
        );
    }

    // clamp............................................................................................................

    @Test
    public void testClampWithNegativeNumberMin() {
        this.clampAndCheck(
            NumberLength.with(55.0),
            Length.number(-1.0),
            Length.number(222.0)
        );
    }

    @Test
    public void testClampWithNegativePixelMin() {
        this.clampAndCheck(
            NumberLength.with(55.0),
            Length.pixel(-1.0),
            Length.number(222.0)
        );
    }

    @Test
    public void testClampWithNegativeNumberMax() {
        final NumberLength max = Length.number(-1.0);

        this.clampAndCheck(
            NumberLength.with(22.0),
            Length.number(-1.0),
            max,
            max
        );
    }

    @Test
    public void testClampWithGreaterThanPixelMax() {
        final double max = 2.0;

        this.clampAndCheck(
            NumberLength.with(333.0),
            Length.number(-1.0),
            Length.pixel(max),
            NumberLength.with(max)
        );
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(
            NumberLength.with(99.0)
        );
    }

    // LengthVisitor....................................................................................................

    @Test
    public void testVisitor() {
        final StringBuilder b = new StringBuilder();

        final NumberLength length = this.createLength();
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
            protected void visit(final NumberLength l) {
                assertSame(length, l, "length");
                b.append("3");
            }
        }.accept(length);

        this.checkEquals("132", b.toString());
    }

    @Override
    NumberLength createLength() {
        return NumberLength.with(123.0);
    }

    @Override
    Optional<LengthUnit<Double, Length<Double>>> unit() {
        return Optional.empty();
    }

    // json.............................................................................................................

    @Override
    public NumberLength unmarshall(final JsonNode from,
                                   final JsonNodeUnmarshallContext context) {
        return Length.unmarshallNumber(from);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            NumberLength.with(0.0),
            "0"
        );
    }

    @Test
    public void testToStringDecimal() {
        this.toStringAndCheck(
            NumberLength.with(10.0),
            "10"
        );
    }

    // canBeEmpty.......................................................................................................

    @Test
    public void testIsEmpty() {
        this.isEmptyAndCheck(
            NumberLength.with(123.0),
            false
        );
    }

    @Test
    public void testIsEmptyWithZero() {
        this.isEmptyAndCheck(
            NumberLength.with(0.0),
            true
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<NumberLength> type() {
        return NumberLength.class;
    }
}
