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

import walkingkooka.color.Color;
import walkingkooka.color.WebColorName;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;
import java.util.Optional;

/**
 * A {@link TextStylePropertyValueHandler} that handles {@link Color} with {@link WebColorName} support added for marshalling.
 */
final class TextStylePropertyValueHandlerColor extends TextStylePropertyValueHandler<Color> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerColor INSTANCE = new TextStylePropertyValueHandlerColor();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerColor() {
        super();
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    void checkNonNullValue(final Object value,
                           final TextStylePropertyName<?> name) {
        this.checkType(
            value,
            (v) -> v instanceof Color,
            name
        );
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return Color.class.getSimpleName();
    }

    @Override
    Color parseValueText(final String value) {
        return this.unmarshall(
            JsonNode.string(value),
            null,
            CONTEXT
        );
    }

    private final static JsonNodeUnmarshallContext CONTEXT = JsonNodeUnmarshallContexts.basic(
        ExpressionNumberKind.DOUBLE,
        MathContext.DECIMAL32
    );

    // JsonNodeContext..................................................................................................

    @Override
    Color unmarshall(final JsonNode node,
                     final TextStylePropertyName<?> name,
                     final JsonNodeUnmarshallContext context) {
        return context.unmarshall(
            node,
            Color.class
        );
    }

    /**
     * Try to marshall the {@link WebColorName#value()} or let {@link Color}.
     */
    @Override
    JsonNode marshall(final Color color,
                      final JsonNodeMarshallContext context) {
        final WebColorName webColorName = color.toWebColorName()
            .orElse(null);
        return null != webColorName ?
            context.marshall(
                webColorName.value()
            ) :
            context.marshall(
                color
            );
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return Color.class.getSimpleName();
    }
}
