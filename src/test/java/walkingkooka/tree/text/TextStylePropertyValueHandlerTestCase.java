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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TextStylePropertyValueHandlerTestCase<P extends TextStylePropertyValueHandler<T>, T> implements ClassTesting<P>,
        ToStringTesting<P>,
        TypeNameTesting<P> {

    TextStylePropertyValueHandlerTestCase() {
        super();
    }

    @Test
    public final void testCheckNullValueFails() {
        this.checkFails(
                "Property " + this.propertyName().inQuotes() + " missing value"
        );
    }

    final void check(final Object value) {
        final TextStylePropertyName<?> propertyName = this.propertyName();
        this.handler().check(value, propertyName);
        propertyName.check(value);
    }

    final void checkFails(final String message) {
        this.checkFails(
                null,
                NullPointerException.class,
                message
        );
    }

    final void checkFails(final Object value, final String message) {
        this.checkFails(
                value,
                IllegalArgumentException.class,
                message
        );
    }

    private <T extends RuntimeException> void checkFails(final Object value,
                                                         final Class<T> throwType,
                                                         final String message) {
        final T thrown = assertThrows(
                throwType,
                () -> this.check(value)
        );
        this.checkEquals(message, thrown.getMessage(), "message");

        final T thrown2 = assertThrows(
                throwType,
                () -> this.propertyName().check(value)
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

    abstract String propertyValueType();

    final JsonNodeUnmarshallContext unmarshallContext() {
        return JsonNodeUnmarshallContexts.basic(
                ExpressionNumberKind.DEFAULT,
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
