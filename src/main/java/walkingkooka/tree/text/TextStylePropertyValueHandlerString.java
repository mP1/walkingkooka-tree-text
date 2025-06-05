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
 * A {@link TextStylePropertyValueHandler} for non empty {@link String} parameter values.
 */
final class TextStylePropertyValueHandlerString extends TextStylePropertyValueHandler<String> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerString INSTANCE = new TextStylePropertyValueHandlerString();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerString() {
        super();
    }

    @Override
    Class<String> valueType() {
        return String.class;
    }

    @Override
    boolean testValue(final Object value) {
        return value instanceof String;
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    void checkNonNullValue(final Object value, final TextStylePropertyName<?> name) {
        final String string = this.checkType(value,
            v -> v instanceof String,
            name);
        if (string.isEmpty()) {
            throw new IllegalArgumentException("Property " + name.inQuotes() + " contains an empty/whitespace value " + CharSequences.quoteAndEscape(string));
        }
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return "String";
    }

    @Override
    String parseValue(final TextStyleParser parser) {
        return this.parseValueText(
            parser.quotedText()
                .orElseThrow(() -> new IllegalArgumentException("Missing quoted text"))
        );
    }

    @Override
    String parseValueText(final String value) {
        return value;
    }

    // JsonNodeContext..................................................................................................

    @Override
    String unmarshall(final JsonNode node,
                      final TextStylePropertyName<?> name,
                      final JsonNodeUnmarshallContext context) {
        return node.stringOrFail();
    }

    @Override
    JsonNode marshall(final String value,
                      final JsonNodeMarshallContext context) {
        return JsonNode.string(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return String.class.getSimpleName();
    }
}
