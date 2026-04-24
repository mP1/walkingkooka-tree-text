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
 * A {@link TextStylePropertyValueHandler} that handles {@link Border}
 */
final class TextStylePropertyValueHandlerBorder extends TextStylePropertyValueHandler<Border> {

    /**
     * Singleton
     */
    final static TextStylePropertyValueHandlerBorder INSTANCE = new TextStylePropertyValueHandlerBorder();

    /**
     * Private ctor
     */
    private TextStylePropertyValueHandlerBorder() {
        super();
    }

    @Override
    Class<Border> valueType() {
        return Border.class;
    }

    @Override
    boolean testValue(final Object value) {
        return value instanceof Border;
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
    Border parseValue(final TextStyleParser parser) {
        return this.parseValueText(
            parser.quotedOrMultiTokenText()
                .orElse("")
        );
    }

    @Override
    Border parseValueText(final String text) {
        return Border.parse(text);
    }

    @Override
    String makeString(final Border value) {
        return value.toString();
    }

    // JsonNodeContext..................................................................................................

    @Override
    Border unmarshall(final JsonNode node,
                      final TextStylePropertyName<?> name,
                      final JsonNodeUnmarshallContext context) {
        return context.unmarshall(
            node,
            Border.class
        );
    }

    @Override
    JsonNode marshall(final Border border,
                      final JsonNodeMarshallContext context) {
        return context.marshall(border);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return Border.class.getSimpleName();
    }
}
