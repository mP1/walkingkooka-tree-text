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
import walkingkooka.color.Color;
import walkingkooka.props.Properties;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;

/**
 * Base converter that provides support for handling property values.
 */
abstract class TextStylePropertyValueHandler<T> {

    /**
     * {@see TextStylePropertyValueHandlerColor}
     */
    static TextStylePropertyValueHandler<Color> color() {
        return TextStylePropertyValueHandlerColor.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerEnum}
     */
    static <E extends Enum<E>> TextStylePropertyValueHandlerEnum<E> enumTextPropertyValueHandler(final E[] values,
                                                                                                 final Class<E> type) {
        return TextStylePropertyValueHandlerEnum.with(
            values,
            type
        );
    }

    /**
     * {@see TextStylePropertyValueHandlerFontFamily}
     */
    static TextStylePropertyValueHandler<FontFamily> fontFamily() {
        return TextStylePropertyValueHandlerFontFamily.INSTANCE;
    }    

    /**
     * {@see TextStylePropertyValueHandlerFontSize}
     */
    static TextStylePropertyValueHandler<FontSize> fontSize() {
        return TextStylePropertyValueHandlerFontSize.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerFontWeight}
     */
    static TextStylePropertyValueHandler<FontWeight> fontWeight() {
        return TextStylePropertyValueHandlerFontWeight.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength}
     */
    static TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength noneLengthNumberLengthPixelLength() {
        return TextStylePropertyValueHandlerLengthNoneLengthNumberLengthPixelLength.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerLengthNoneLengthPixelLength}
     */
    static TextStylePropertyValueHandlerLengthNoneLengthPixelLength noneLengthPixelLength() {
        return TextStylePropertyValueHandlerLengthNoneLengthPixelLength.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerLengthNormalLengthPixelLength}
     */
    static TextStylePropertyValueHandlerLengthNormalLengthPixelLength normalLengthPixelLength() {
        return TextStylePropertyValueHandlerLengthNormalLengthPixelLength.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerOpacity}
     */
    static TextStylePropertyValueHandler<Opacity> opacity() {
        return TextStylePropertyValueHandlerOpacity.INSTANCE;
    }
    
    /**
     * {@see TextStylePropertyValueHandlerString}
     */
    static TextStylePropertyValueHandler<String> string() {
        return TextStylePropertyValueHandlerString.INSTANCE;
    }

    /**
     * {@see TextStylePropertyValueHandlerTextOverflow}
     */
    static TextStylePropertyValueHandler<TextOverflow> textOverflow() {
        return TextStylePropertyValueHandlerTextOverflow.INSTANCE;
    }

    /**
     * Package private to limit sub classing.
     */
    TextStylePropertyValueHandler() {
        super();
    }

    /**
     * {@link TextStylePropertyName#type()}
     */
    abstract Class<T> valueType();

    /**
     * Sub-classes should do an instanceof check etc.
     */
    abstract boolean testValue(final Object value);

    abstract Optional<Class<Enum<?>>> enumType();

    // checkValue.......................................................................................................

    final T checkValue(final Object value,
                       final TextStylePropertyName<?> name) {
        if (null == value) {
            throw new NullPointerException("Property " + name.inQuotes() + " missing value");
        }

        if (false == this.testValue(value)) {
            // Property "tab-size" value normal(NormalLength) is not a NoneLength|NumberLength|PixelLength
            throw new IllegalArgumentException(
                "Property " +
                name.inQuotes() +
                    ": " +
                    this.invalidValueMessage(
                        value
                    )
            );
        }

        return Cast.to(value);
    }

    /**
     * The message that will appear when {@link #checkValue(Object, TextStylePropertyName)} fails.
     */
    abstract String invalidValueMessage(final Object value);

    // parseValue.......................................................................................................

    /**
     * Parses the value.
     */
    abstract T parseValue(final TextStyleParser parser);

    // parseValueText...................................................................................................

    /**
     * Parses the text form of a value into an actual value instance.
     */
    abstract T parseValueText(final String value);

    // makeString ......................................................................................................

    /**
     * Creates a {@link String} representation of the given value, and is used to individual values into a {@link Properties}.
     * This is the inverse of {@link #parseValueText(String)}.
     */
    abstract String makeString(final T value);

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
