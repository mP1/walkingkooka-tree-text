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
 * A {@link TextStylePropertyValueHandler} for {@link FontSize}
 */
final class TextStylePropertyValueHandlerFontSize extends TextStylePropertyValueHandler<FontSize> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerFontSize INSTANCE = new TextStylePropertyValueHandlerFontSize();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerFontSize() {
        super();
    }

    @Override
    Class<FontSize> valueType() {
        return FontSize.class;
    }

    @Override
    boolean testValue(final Object value) {
        return value instanceof FontSize;
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    String invalidValueMessage(final Object value) {
        return "Expected FontSize but got " + value.getClass().getSimpleName();
    }

    @Override
    FontSize parseValue(final TextStyleParser parser) {
        return this.parseValueText(
            parser.token()
        );
    }

    @Override
    FontSize parseValueText(final String value) {
        return FontSize.with(
            Integer.parseInt(value)
        );
    }

    // JsonNodeContext..................................................................................................

    @Override
    FontSize unmarshall(final JsonNode node,
                        final TextStylePropertyName<?> name,
                        final JsonNodeUnmarshallContext context) {
        return context.unmarshall(
            node,
            FontSize.class
        );
    }

    @Override
    JsonNode marshall(final FontSize value,
                      final JsonNodeMarshallContext context) {
        return JsonNode.number(value.value());
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return FontSize.class.getSimpleName();
    }
}
