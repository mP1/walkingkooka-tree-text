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
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

public final class LengthTest implements ClassTesting2<Length<?>>,
        JsonNodeMarshallingTesting<Length<?>>,
        ParseStringTesting<Length<?>> {

    @Test
    public void testParseIncorrectUnitFails() {
        this.parseStringFails("12EM", IllegalArgumentException.class);
    }

    @Test
    public void testParseNone() {
        this.parseStringAndCheck("none", Length.none());
    }

    @Test
    public void testParseNormal() {
        this.parseStringAndCheck("normal", Length.normal());
    }

    @Test
    public void testParseNumber() {
        this.parseStringAndCheck("123", Length.number(123L));
    }

    @Test
    public void testParsePixels() {
        this.parseStringAndCheck("12.5px", Length.pixel(12.5));
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<Length<?>> type() {
        return Cast.to(Length.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ParseStringTesting...............................................................................................

    @Override
    public Length<?> parseString(final String text) {
        return Length.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
    }

    // JsonNodeMarshallTesting..........................................................................................

    @Override
    public Length unmarshall(final JsonNode from,
                             final JsonNodeUnmarshallContext context) {
        return Length.unmarshall(from, context);
    }

    @Override
    public Length createJsonNodeMappingValue() {
        return Length.pixel(123.0);
    }
}
