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
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.HasValueTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.IsMethodTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasTextTesting;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;

import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class LengthTestCase<L extends Length<V>, V> implements ClassTesting2<L>,
    HashCodeEqualsDefinedTesting2<L>,
    IsMethodTesting<L>,
    JsonNodeMarshallingTesting<L>,
    ParseStringTesting<L>,
    ToStringTesting<L>,
    TypeNameTesting<L>,
    HasTextTesting,
    HasValueTesting,
    CanBeEmptyTesting {

    LengthTestCase() {
        super();
    }

    // clamp............................................................................................................

    @Test
    public void testClampWithNullMinFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createLength()
                .clamp(
                    null,
                    Length.none()
                )
        );
    }

    @Test
    public void testClampWithNormalMinFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createLength()
                .clamp(
                    Length.normal(),
                    Length.none()
                )
        );
    }

    @Test
    public void testClampWithNullMaxFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createLength()
                .clamp(
                    Length.none(),
                    null
                )
        );
    }

    @Test
    public void testClampWithNormalMaxFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createLength()
                .clamp(
                    Length.none(),
                    Length.normal()
                )
        );
    }

    @Test
    public void testClampWithMinGreaterThanMaxFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createLength()
                .clamp(
                    Length.number(111.0),
                    Length.number(23.0)
                )
        );
    }

    @Test
    public void testClampWithMinGreaterThanMaxFails2() {
        assertThrows(
            IllegalArgumentException
                .class,
            () -> this.createLength()
                .clamp(
                    Length.pixel(111.0),
                    Length.number(23.0)
                )
        );
    }

    final void clampAndCheck(final L length,
                             final Length<?> min,
                             final Length<?> max) {
        this.clampAndCheck(
            length,
            min,
            max,
            length
        );
    }

    final void clampAndCheck(final L length,
                             final Length<?> min,
                             final Length<?> max,
                             final Length<?> expected) {
        this.checkEquals(
            expected,
            length.clamp(
                min,
                max
            ),
            () -> length + " clamp " + min + " " + max
        );
    }

    // unit.............................................................................................................

    @Test
    public final void testUnit() {
        final L length = this.createLength();
        this.checkEquals(
            this.unit(),
            length.unit(),
            "unit"
        );
    }

    @Test
    public final void testText() {
        final L length = this.createLength();

        this.textAndCheck(
            length,
            length.toString()
        );
    }

    abstract L createLength();

    abstract Optional<LengthUnit<V, Length<V>>> unit();

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefinedTesting.....................................................................................

    @Override
    public final L createObject() {
        return this.createLength();
    }

    // JsonNodeMarshallTesting..........................................................................................

    @Override
    public final L createJsonNodeMarshallingValue() {
        return this.createLength();
    }

    // IsMethodTesting..................................................................................................

    @Override
    public final L createIsMethodObject() {
        return this.createLength();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (String name) -> "isEmpty".equals(name) || "isNotEmpty".equals(name);
    }

    @Override
    public final String toIsMethodName(final String typeName) {
        return this.toIsMethodNameWithPrefixSuffix(
            typeName,
            "",
            Length.class.getSimpleName()
        );
    }

    // ParseStringTesting...............................................................................................

    @Override
    public final Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public final RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return Length.class.getSimpleName();
    }
}
