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
 * A {@link TextStylePropertyValueHandler} for {@link FontWeight}
 */
final class TextStylePropertyValueHandlerFontWeight extends TextStylePropertyValueHandler<FontWeight> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerFontWeight INSTANCE = new TextStylePropertyValueHandlerFontWeight();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerFontWeight() {
        super();
    }

    @Override
    Class<FontWeight> valueType() {
        return FontWeight.class;
    }

    @Override
    boolean testValue(final Object value) {
        return value instanceof FontWeight;
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    String invalidValueMessage(final Object value) {
        return "Expected FontWeight but got " + value.getClass().getSimpleName();
    }

    @Override
    FontWeight parseValue(final TextStyleParser parser) {
        return this.parseValueText(
            parser.token()
        );
    }

    @Override
    FontWeight parseValueText(final String value) {
        return FontWeight.BOLD_TEXT.equals(value) ?
            FontWeight.BOLD :
            FontWeight.NORMAL_TEXT.equals(value) ?
                FontWeight.NORMAL :
                FontWeight.with(Integer.parseInt(value));
    }

    // JsonNodeContext..................................................................................................

    @Override
    FontWeight unmarshall(final JsonNode node,
                          final TextStylePropertyName<?> name,
                          final JsonNodeUnmarshallContext context) {
        return context.unmarshall(
            node,
            FontWeight.class
        );
    }

    @Override
    JsonNode marshall(final FontWeight value,
                      final JsonNodeMarshallContext context) {
        return context.marshall(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return FontWeight.class.getSimpleName();
    }
}
