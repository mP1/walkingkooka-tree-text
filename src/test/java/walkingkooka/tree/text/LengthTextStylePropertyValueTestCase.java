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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class LengthTextStylePropertyValueTestCase<L extends LengthTextStylePropertyValue> implements ClassTesting2<L>,
        HashCodeEqualsDefinedTesting2<L>,
        JsonNodeMarshallingTesting<L>,
        ParseStringTesting<L>,
        ToStringTesting<L> {

    LengthTextStylePropertyValueTestCase() {
        super();
    }

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> this.createPropertyValue(null));
    }

    @Test
    public final void testWith() {
        final L propertyValue = this.createPropertyValue();
        assertEquals(this.length(), propertyValue.value(), "value");
    }

    @Test
    public final void testParse() {
        final L propertyValue = this.createPropertyValue();
        this.parseStringAndCheck(propertyValue.toString(), propertyValue);
    }

    @Test
    public final void testJsonNodeUnmarshall() {
        final L propertyValue = this.createPropertyValue();
        this.unmarshallAndCheck(this.marshallContext().marshall(propertyValue), propertyValue);
    }

    @Test
    public final void testJsonNodeMarshall() {
        final Length<?> length = this.length();
        this.marshallAndCheck(this.createPropertyValue(length), length.marshall(this.marshallContext()));
    }

    @Test
    public final void testDifferentLength() {
        this.checkNotEquals(this.createPropertyValue(this.differentLength()));
    }

    @Test
    public final void testToString() {
        final Length<?> length = this.length();
        this.toStringAndCheck(this.createPropertyValue(length), length.toString());
    }

    final L createPropertyValue() {
        return this.createPropertyValue(this.length());
    }

    abstract L createPropertyValue(final Length<?> length);

    abstract Length<?> length();

    abstract Length<?> differentLength();

    final void withAndCheck(final Length<?> length) {
        assertSame(length, this.createPropertyValue(length).value(), "length");
    }

    final void withFails(final Length<?> length) {
        assertThrows(IllegalArgumentException.class, () -> this.createPropertyValue(length));
    }

    // ClassTesting......................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefinedTesting.....................................................................................

    @Override
    public L createObject() {
        return this.createPropertyValue();
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

    // JsonNodeMarshallTesting..........................................................................................

    @Override
    public final L createJsonNodeMappingValue() {
        return this.createPropertyValue();
    }
}
