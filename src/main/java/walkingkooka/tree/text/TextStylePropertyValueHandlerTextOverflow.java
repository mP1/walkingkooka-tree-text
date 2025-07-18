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
 * A {@link TextStylePropertyValueHandler} for {@link TextOverflow}
 */
final class TextStylePropertyValueHandlerTextOverflow extends TextStylePropertyValueHandler<TextOverflow> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerTextOverflow INSTANCE = new TextStylePropertyValueHandlerTextOverflow();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerTextOverflow() {
        super();
    }

    @Override
    Class<TextOverflow> valueType() {
        return TextOverflow.class;
    }

    @Override
    boolean testValue(final Object value) {
        return value instanceof TextOverflow;
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    String invalidValueMessage(final Object value) {
        return "Expected TextOverflow got " + value.getClass().getSimpleName();
    }

    @Override
    TextOverflow parseValueText(final String value) {
        return TextOverflow.parse(value);
    }

    @Override
    TextOverflow parseValue(final TextStyleParser parser) {
        return this.parseValueText(
            parser.quotedText()
                .orElseGet(parser::token)
        );
    }

    // JsonNodeContext..................................................................................................

    @Override
    TextOverflow unmarshall(final JsonNode node,
                          final TextStylePropertyName<?> name,
                          final JsonNodeUnmarshallContext context) {
        return context.unmarshall(
            node,
            TextOverflow.class
        );
    }

    @Override
    JsonNode marshall(final TextOverflow value,
                      final JsonNodeMarshallContext context) {
        return context.marshall(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return TextOverflow.class.getSimpleName();
    }
}
