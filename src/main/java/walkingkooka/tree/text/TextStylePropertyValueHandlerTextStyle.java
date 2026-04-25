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
 * A {@link TextStylePropertyValueHandler} for {@link TextStyle} parameter values.
 */
final class TextStylePropertyValueHandlerTextStyle extends TextStylePropertyValueHandler<TextStyle> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerTextStyle INSTANCE = new TextStylePropertyValueHandlerTextStyle();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerTextStyle() {
        super();
    }

    @Override
    Class<TextStyle> valueType() {
        return TextStyle.class;
    }

    @Override
    boolean testValue(final Object value) {
        return value instanceof TextStyle;
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    @Override
    InvalidTextStylePropertyValueException invalidValueMessage(final TextStylePropertyName<?> name,
                                                               final Object value) {
        return name.invalidTextStylePropertyValueException(value)
            .setExpected("values not supported");
    }

    @Override
    TextStyle parseValue(final TextStyleParser parser) {
        throw new UnsupportedOperationException();
    }

    @Override
    TextStyle parseValueText(final String value) {
        return TextStyle.parse(value);
    }

    // json.............................................................................................................

    @Override
    TextStyle unmarshall(final JsonNode node,
                         final TextStylePropertyName<?> name,
                         final JsonNodeUnmarshallContext context) {
        return context.unmarshall(
            node,
            TextStyle.class
        );
    }

    @Override
    JsonNode marshall(final TextStyle value,
                      final JsonNodeMarshallContext context) {
        return context.marshall(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return TextStyle.class.getSimpleName();
    }
}
