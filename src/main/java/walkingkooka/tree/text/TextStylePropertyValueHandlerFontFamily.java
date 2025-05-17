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
 * A {@link TextStylePropertyValueHandler} for {@link FontFamily}
 */
final class TextStylePropertyValueHandlerFontFamily extends TextStylePropertyValueHandler<FontFamily> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerFontFamily INSTANCE = new TextStylePropertyValueHandlerFontFamily();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerFontFamily() {
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
            (v) -> v instanceof FontFamily,
            name
        );
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return FontFamily.class.getSimpleName();
    }

    @Override
    FontFamily parseValueText(final String value) {
        return FontFamily.with(value);
    }

    // JsonNodeContext..................................................................................................

    @Override
    FontFamily unmarshall(final JsonNode node,
                          final TextStylePropertyName<?> name,
                          final JsonNodeUnmarshallContext context) {
        return context.unmarshall(
            node,
            FontFamily.class
        );
    }

    @Override
    JsonNode marshall(final FontFamily value,
                      final JsonNodeMarshallContext context) {
        return context.marshall(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return FontFamily.class.getSimpleName();
    }
}
