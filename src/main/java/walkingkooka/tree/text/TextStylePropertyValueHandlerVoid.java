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
 * A {@link TextStylePropertyValueHandler} for {@link Void} parameter values.
 */
final class TextStylePropertyValueHandlerVoid extends TextStylePropertyValueHandler<Void> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerVoid INSTANCE = new TextStylePropertyValueHandlerVoid();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerVoid() {
        super();
    }

    @Override
    Class<Void> valueType() {
        return Void.class;
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    void checkNonNullValue(final Object value,
                           final TextStylePropertyName<?> name) {
        throw new IllegalArgumentException("Values not supported for " + name.inQuotes());
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return Void.class.getSimpleName();
    }

    @Override
    Void parseValue(final TextStyleParser parser) {
        throw new UnsupportedOperationException();
    }

    @Override
    Void parseValueText(final String value) {
        if (value.length() > 0) {
            throw new IllegalArgumentException("Only empty values allowed: " + CharSequences.quoteAndEscape(value));
        }
        return null;
    }

    // JsonNodeContext..................................................................................................

    @Override
    Void unmarshall(final JsonNode node,
                    final TextStylePropertyName<?> name,
                    final JsonNodeUnmarshallContext context) {
        throw new UnsupportedOperationException("Unmarshalling " + name.inQuotes() + " with " + node + " is not supported");
    }

    @Override
    JsonNode marshall(final Void value,
                      final JsonNodeMarshallContext context) {
        throw new UnsupportedOperationException("Marshalling value not supported");
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return Void.class.getSimpleName();
    }
}
