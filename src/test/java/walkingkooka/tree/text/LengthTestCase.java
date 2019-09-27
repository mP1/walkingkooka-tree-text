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
import walkingkooka.predicate.Predicates;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting2;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class LengthTestCase<L extends Length, V> implements ClassTesting2<L>,
        HashCodeEqualsDefinedTesting2<L>,
        IsMethodTesting<L>,
        JsonNodeMarshallingTesting<L>,
        ParseStringTesting<L>,
        ToStringTesting<L>,
        TypeNameTesting<L> {

    LengthTestCase() {
        super();
    }

    @Test
    public final void testUnit() {
        final L length = this.createLength();
        assertEquals(this.unit(), length.unit(), "unit");
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

    // JsonNodeMapTesting...............................................................................................

    @Override
    public final L createJsonNodeMappingValue() {
        return this.createLength();
    }

    // IsMethodTesting..................................................................................................

    @Override
    public final L createIsMethodObject() {
        return this.createLength();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return Length.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return Predicates.never();
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
