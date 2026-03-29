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

import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;

/**
 * A {@link TextStylePropertyValueHandler} that handles {@link Margin}
 */
final class TextStylePropertyValueHandlerMargin extends TextStylePropertyValueHandler<Margin> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerMargin INSTANCE = new TextStylePropertyValueHandlerMargin();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerMargin() {
        super();
    }

    @Override
    Class<Margin> valueType() {
        return Margin.class;
    }

    @Override
    boolean testValue(final Object value) {
        return value instanceof Margin;
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    InvalidTextStylePropertyValueException invalidValueMessage(final TextStylePropertyName<?> name,
                                                               final Object value) {
        return this.expectedValueType(
            name,
            value
        );
    }

    @Override
    Margin parseValue(final TextStyleParser parser) {
        return this.parseValueText(
            parser.quotedText()
                .orElse("")
        );
    }

    @Override
    Margin parseValueText(final String text) {
        return Margin.parse(text);
    }

    @Override
    String makeString(final Margin value) {
        return value.toString();
    }

    // JsonNodeContext..................................................................................................

    @Override
    Margin unmarshall(final JsonNode node,
                      final TextStylePropertyName<?> name,
                      final JsonNodeUnmarshallContext context) {
        return context.unmarshall(
            node,
            Margin.class
        );
    }

    @Override
    JsonNode marshall(final Margin margin,
                      final JsonNodeMarshallContext context) {
        return context.marshall(margin);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return Margin.class.getSimpleName();
    }
}
