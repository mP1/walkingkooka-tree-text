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
import walkingkooka.currency.CurrencyLocaleContexts;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TextStylePropertyValueHandlerTestCase<P extends TextStylePropertyValueHandler<T>, T> implements ParseStringTesting<T>,
    ClassTesting<P>,
    ToStringTesting<P>,
    TypeNameTesting<P> {

    TextStylePropertyValueHandlerTestCase() {
        super();
    }

    // parseValue.......................................................................................................

    @Test
    public final void testParseValueTextWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.handler()
                .parseValueText(null)
        );
    }

    @Test
    public final void testParseValueTextInvalidFails() {
        if(false == this instanceof TextStylePropertyValueHandlerStringTest) {
            this.parseStringFails(
                "!!!!\n",
                IllegalArgumentException.class
            );
        }
    }

    @Override
    public final T parseString(final String text) {
        return this.handler()
            .parseValueText(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
    }

    // checkValue.......................................................................................................

    @Test
    public final void testCheckValueWithNullFails() {
        this.checkValueFails(
            "Property " + this.propertyName().inQuotes() + " missing value"
        );
    }

    final void checkValue(final Object value) {
        final TextStylePropertyName<?> propertyName = this.propertyName();
        this.handler().checkValue(value, propertyName);
        propertyName.checkValue(value);
    }

    final void checkValueFails(final String message) {
        this.checkValueFails(
            null,
            NullPointerException.class,
            message
        );
    }

    final void checkValueFails(final Object value,
                               final String message) {
        this.checkValueFails(
            value,
            IllegalArgumentException.class,
            message
        );
    }

    private <TT extends RuntimeException> void checkValueFails(final Object value,
                                                               final Class<TT> throwType,
                                                               final String message) {
        final TT thrown = assertThrows(
            throwType,
            () -> this.checkValue(value)
        );
        this.checkEquals(
            message,
            thrown.getMessage(),
            "message"
        );

        final TT thrown2 = assertThrows(
            throwType,
            () -> this.propertyName().checkValue(value)
        );
        this.checkEquals(
            message,
            thrown2.getMessage(),
            "message"
        );
    }

    // helper...........................................................................................................

    abstract P handler();

    abstract TextStylePropertyName<T> propertyName();

    final JsonNodeUnmarshallContext unmarshallContext() {
        return JsonNodeUnmarshallContexts.basic(
            ExpressionNumberKind.DEFAULT,
            CurrencyLocaleContexts.fake(), // CurrencyCodeLanguageTagContext
            MathContext.DECIMAL32
        );
    }

    final JsonNodeMarshallContext marshallContext() {
        return JsonNodeMarshallContexts.basic();
    }

    final JsonNode marshall(final Object value) {
        return this.marshallContext().marshall(value);
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting...................................................................................................

    @Override
    public final String typeNamePrefix() {
        return TextStylePropertyValueHandler.class.getSimpleName();
    }
}
