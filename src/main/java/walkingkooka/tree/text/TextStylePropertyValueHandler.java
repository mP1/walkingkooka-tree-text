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

import walkingkooka.Cast;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.function.Function;

/**
 * Base converter that provides support for handling property values.
 */
abstract class TextStylePropertyValueHandler<T> {

    /**
     * {@see TextStylePropertyValueHandlerEnum}
     */
    static <E extends Enum<E>> TextStylePropertyValueHandlerEnum<E> enumTextPropertyValueHandler(final Function<String, E> factory,
                                                                                                 final Class<E> type) {
        return TextStylePropertyValueHandlerEnum.with(factory, type);
    }

    /**
     * {@see TextStylePropertyValueHandlerJsonNode}
     */
    static <V> TextStylePropertyValueHandler<V> jsonNode(final Class<V> type) {
        return TextStylePropertyValueHandlerJsonNode.with(type);
    }

    /**
     * {@see TextStylePropertyValueHandlerJsonNodeWithType}
     */
    static TextStylePropertyValueHandlerJsonNodeWithType jsonNodeWithType() {
        return TextStylePropertyValueHandlerJsonNodeWithType.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerNoneLengthPixelLength}
     */
    static TextStylePropertyValueHandlerNoneLengthPixelLength noneLengthPixelLength() {
        return TextStylePropertyValueHandlerNoneLengthPixelLength.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerNormalLengthPixelLength}
     */
    static TextStylePropertyValueHandlerNormalLengthPixelLength normalLengthPixelLength() {
        return TextStylePropertyValueHandlerNormalLengthPixelLength.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerString}
     */
    static TextStylePropertyValueHandler<String> string() {
        return TextStylePropertyValueHandlerString.INSTANCE;
    }

    /**
     * Package private to limit sub classing.
     */
    TextStylePropertyValueHandler() {
        super();
    }

    // checkValue...........................................................

    final T check(final Object value, final TextStylePropertyName<?> name) {
        if (null == value) {
            throw new TextStylePropertyValueException("Property " + name.inQuotes() + " missing value");
        }

        this.check0(value, name);
        return Cast.to(value);
    }

    abstract void check0(final Object value, final TextStylePropertyName<?> name);

    /**
     * Checks the type of the given value and throws a {@link TextStylePropertyValueException} if this test fails.
     */
    final <U> U checkType(final Object value, final Class<U> type, final TextStylePropertyName<?> name) {
        if (!type.isInstance(value)) {
            throw this.textStylePropertyValueException(value, name);
        }
        return type.cast(value);
    }

    /**
     * Creates a {@link TextStylePropertyValueException} used to report an invalid value.
     */
    final TextStylePropertyValueException textStylePropertyValueException(final Object value,
                                                                          final TextStylePropertyName<?> name) {
        final Class<?> type = value.getClass();

        String typeName = type.getName();
        if (textStylePropertyType(typeName)) {
            typeName = typeName.substring(1 + typeName.lastIndexOf('.'));
        }

        return new TextStylePropertyValueException("Property " + name.inQuotes() + " value " + CharSequences.quoteIfChars(value) + "(" + typeName + ") is not a " + this.expectedTypeName(type));
    }

    abstract String expectedTypeName(final Class<?> type);

    final boolean textStylePropertyType(final String type) {
        return type.startsWith(PACKAGE) && type.indexOf('.', 1 + PACKAGE.length()) == -1;
    }

    private final static String PACKAGE = "walkingkooka.tree.text";

    // unmarshall ....................................................................................................

    /**
     * Transforms a {@link JsonNode} into a value.
     */
    abstract T unmarshall(final JsonNode node,
                          final TextStylePropertyName<?> name,
                          final JsonNodeUnmarshallContext context);

    /**
     * Transforms a value into json, performing the inverse of {@link #unmarshall(JsonNode, TextStylePropertyName, JsonNodeUnmarshallContext)}
     */
    abstract JsonNode marshall(final T value,
                               final JsonNodeMarshallContext context);

    // Object .........................................................................................................

    @Override
    abstract public String toString();
}
