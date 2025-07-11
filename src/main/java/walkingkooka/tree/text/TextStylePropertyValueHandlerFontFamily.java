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

import walkingkooka.text.CharSequences;
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
    Class<FontFamily> valueType() {
        return FontFamily.class;
    }

    @Override
    boolean testValue(final Object value) {
        return value instanceof FontFamily;
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    String invalidValueMessage(final Object value) {
        return "Expected FontFamily but got " + value.getClass().getSimpleName();
    }

    @Override
    FontFamily parseValueText(final String value) {
        return FontFamily.with(value);
    }

    @Override
    FontFamily parseValue(final TextStyleParser parser) {
        return this.parseValueText(
            parser.quotedText()
                .map(TextStylePropertyValueHandlerFontFamily::unquote)
                .orElseGet(parser::token)
        );
    }

    private static String unquote(final String text) {
        return CharSequences.subSequence(
            text,
            1,
            -1
        ).toString();
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
