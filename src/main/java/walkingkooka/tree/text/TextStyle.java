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

import walkingkooka.CanBeEmpty;
import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.props.HasProperties;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.patch.Patchable;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link TextStyle} holds a {@link Map} of {@link TextStylePropertyName} and values.
 */
public abstract class TextStyle implements Value<Map<TextStylePropertyName<?>, Object>>,
    Patchable<TextStyle>,
    TreePrintable,
    HasText,
    Styleable,
    CanBeEmpty,
    HasProperties {

    /**
     * Tests if the given {@link Class} is for a {@link TextStyle}.
     */
    public static boolean isStyleClass(final Class<?> klass) {
        return TextStyle.class == klass ||
            TextStyleEmpty.class == klass ||
            TextStyleNonEmpty.class == klass;
    }

    /**
     * A {@link TextStyle} with no styling.
     */
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public final static TextStyle EMPTY = TextStyleEmpty.instance();

    static final String BORDER_COLOR = "border-color";

    static final String BORDER_STYLE = "border-style";

    static final String BORDER_WIDTH = "border-width";

    static final String MARGIN = "margin";

    static final String PADDING = "padding";

    public final static CharacterConstant ASSIGNMENT = CharacterConstant.with(':');

    public final static CharacterConstant SEPARATOR = CharacterConstant.with(';');

    // parse............................................................................................................

    /**
     * Parses an almost CSS like declaration into a {@link TextStyle}.
     * Passing the text from {@link #text()} to parse will give an equal {@link TextStyle}.
     */
    public static TextStyle parse(final String text) {
        Objects.requireNonNull(text, "text");

        // WHITESPACE, TextStylePropertyName, WHITESPACE, COLON,
        // WHITESPACE VALUE WHITESPACE
        // COMMA | SEMI-COLON

        final TextStyleParser parser = TextStyleParser.with(text);
        TextStyle style = EMPTY;

        while(parser.isNotEmpty()) {
            parser.skipSpaces();

            final Optional<TextStylePropertyName<?>> maybeName = parser.name();
            if(false == maybeName.isPresent()) {
                if(parser.isNotEmpty()) {
                    throw parser.cursor.lineInfo()
                        .invalidCharacterException()
                        .get();
                }
                break;
            }

            final TextStylePropertyName<?> name = maybeName.get();

            parser.skipSpaces();

            parser.assignment();

            parser.skipSpaces();

            final Object value;

            final int position = parser.cursor.lineInfo()
                .textOffset();
            try {
                value = name.handler.parseValue(parser);
            } catch (final InvalidCharacterException ice) {
                throw ice.setTextAndPosition(
                    text,
                    position + ice.position() - 1
                );
            }

            style = style.set(
                name,
                Cast.to(value)
            );

            parser.skipSpaces();

            if(false == parser.separator()) {
                break;
            }
        }

        return style;
    }

    /**
     * Returns a {@link TextStyle} with the entries within the given {@link Properties}.
     */
    public static TextStyle fromProperties(final Properties properties) {
        Objects.requireNonNull(properties, "properties");

        TextStyle textStyle = EMPTY;

        for (final Entry<PropertiesPath, String> nameAndValue : properties.entries()) {
            final TextStylePropertyName<?> textStylePropertyName = TextStylePropertyName.with(
                nameAndValue.getKey()
                    .value()
            );

            textStyle = textStyle.set(
                textStylePropertyName,
                Cast.to(
                    textStylePropertyName.handler.parseValueText(
                        nameAndValue.getValue()
                    )
                )
            );
        }

        return textStyle;
    }

    /**
     * Private ctor to limit sub classes.
     */
    TextStyle() {
        super();
    }

    // setChildren......................................................................................................

    /**
     * Factory that returns a {@link TextStyleNode} with the given {@link TextNode}
     * and these styles.
     */
    public final TextNode setChildren(final List<TextNode> textNodes) {
        return TextStyleNode.with(textNodes, this.textStyleMap());
    }

    /**
     * A {@link Map} holding the {@link TextStylePropertyName} and values.
     */
    abstract TextStylePropertiesMap textStyleMap();

    // merge............................................................................................................

    /**
     * Merges the two {@link TextStyle}, with the value from the second {@link TextStyle} replacing those in this.
     */
    @Override
    public final TextStyle merge(final TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle");

        return this.mergeNonNull(textStyle);
    }

    abstract TextStyle mergeNonNull(final TextStyle textStyle);

    abstract TextStyle mergeNonEmpty(final TextStyleNonEmpty textStyle);

    // replace............................................................................................................

    /**
     * If empty returns the given {@link TextNode} otherwise wraps inside a {@link TextStyleNode}
     */
    public final TextNode replace(final TextNode textNode) {
        Objects.requireNonNull(textNode, "textNode");

        return this.replaceNonNull(textNode);
    }

    abstract TextNode replaceNonNull(final TextNode textNode);

    // get..............................................................................................................

    /**
     * Gets the value for the given {@link TextStylePropertyName}.
     */
    public final <V> Optional<V> get(final TextStylePropertyName<V> propertyName) {
        Objects.requireNonNull((TextStylePropertyName<?>) propertyName, "propertyName");

        return this.getNonNull(propertyName);
    }

    abstract <V> Optional<V> getNonNull(final TextStylePropertyName<V> propertyName);

    /**
     * Gets the given property or throws an {@link IllegalArgumentException}
     */
    public final <V> V getOrFail(final TextStylePropertyName<V> propertyName) {
        return this.get(propertyName)
            .orElseThrow(() -> new IllegalArgumentException("Missing " + propertyName));
    }

    // set..............................................................................................................

    /**
     * Sets a possibly new property returning a {@link TextStyle} with the new definition which may or may not
     * require creating a new {@link TextStyle}.
     */
    @Override
    public final <V> TextStyle set(final TextStylePropertyName<V> propertyName,
                                   final V value) {
        Objects.requireNonNull((TextStylePropertyName<?>) propertyName, "propertyName");

        propertyName.checkValue(value);
        return this.setNonNull(propertyName, value);
    }

    private <V> TextStyle setNonNull(final TextStylePropertyName<V> propertyName,
                                     final V value) {
        final TextStyle set;

        switch (propertyName.name) {
            case BORDER_COLOR:
                set = this.setTopLeftRightBottom(
                    TextStylePropertyName.BORDER_TOP_COLOR,
                    TextStylePropertyName.BORDER_LEFT_COLOR,
                    TextStylePropertyName.BORDER_RIGHT_COLOR,
                    TextStylePropertyName.BORDER_BOTTOM_COLOR,
                    (Color) value
                );
                break;
            case BORDER_STYLE:
                set = this.setTopLeftRightBottom(
                    TextStylePropertyName.BORDER_TOP_STYLE,
                    TextStylePropertyName.BORDER_LEFT_STYLE,
                    TextStylePropertyName.BORDER_RIGHT_STYLE,
                    TextStylePropertyName.BORDER_BOTTOM_STYLE,
                    (BorderStyle) value
                );
                break;
            case BORDER_WIDTH:
                set = this.setTopLeftRightBottom(
                    TextStylePropertyName.BORDER_TOP_WIDTH,
                    TextStylePropertyName.BORDER_LEFT_WIDTH,
                    TextStylePropertyName.BORDER_RIGHT_WIDTH,
                    TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                    (Length<?>) value
                );
                break;
            case MARGIN:
                set = this.setTopLeftRightBottom(
                    TextStylePropertyName.MARGIN_TOP,
                    TextStylePropertyName.MARGIN_LEFT,
                    TextStylePropertyName.MARGIN_RIGHT,
                    TextStylePropertyName.MARGIN_BOTTOM,
                    (Length<?>) value
                );
                break;
            case PADDING:
                set = this.setTopLeftRightBottom(
                    TextStylePropertyName.PADDING_TOP,
                    TextStylePropertyName.PADDING_LEFT,
                    TextStylePropertyName.PADDING_RIGHT,
                    TextStylePropertyName.PADDING_BOTTOM,
                    (Length<?>) value
                );
                break;
            default:
                set = this.setValue(
                    propertyName,
                    value
                );
                break;
        }

        return set;
    }

    abstract <T> TextStyle setTopLeftRightBottom(final TextStylePropertyName<T> top,
                                                 final TextStylePropertyName<T> left,
                                                 final TextStylePropertyName<T> right,
                                                 final TextStylePropertyName<T> bottom,
                                                 final T value);

    abstract <T> TextStyle setValue(final TextStylePropertyName<T> propertyName,
                                    final T value);

    /**
     * Sets all border properties to the same given value.
     */
    public final TextStyle setBorder(final Color color,
                                     final BorderStyle style,
                                     final Length<?> width) {
        Objects.requireNonNull(color, "color");
        Objects.requireNonNull(style, "style");
        Objects.requireNonNull(width, "width");

        final Map<TextStylePropertyName<?>, Object> colorStyleWidth = this.valuesMutableCopy();

        for (final BoxEdge boxEdge : BoxEdge.values()) {
            if (BoxEdge.ALL == boxEdge) {
                continue;
            }

            final TextStylePropertyName<Color> colorPropertyName = boxEdge.borderColorPropertyName();
            colorPropertyName.checkValue(color);

            colorStyleWidth.put(
                colorPropertyName,
                color
            );

            final TextStylePropertyName<BorderStyle> stylePropertyName = boxEdge.borderStylePropertyName();
            stylePropertyName.checkValue(style);

            colorStyleWidth.put(
                stylePropertyName,
                style
            );

            final TextStylePropertyName<Length<?>> widthPropertyName = boxEdge.borderWidthPropertyName();
            widthPropertyName.checkValue(width);

            colorStyleWidth.put(
                widthPropertyName,
                width
            );
        }

        return this.setValuesWithCopy(colorStyleWidth);
    }

    /**
     * Returns a mutable copy of the current properties for modification.
     */
    abstract Map<TextStylePropertyName<?>, Object> valuesMutableCopy();

    /**
     * Would be setter that returns a {@link TextStyle} that has the given values in addition to what it previously contained,
     * returning a new instance if they are different values.
     */
    public final TextStyle setValues(final Map<TextStylePropertyName<?>, Object> values) {
        Objects.requireNonNull(values, "values");

        final Map<TextStylePropertyName<?>, Object> copy;

        if (values instanceof TextStylePropertiesMap) {
            copy = values;
        } else {
            copy = this.valuesMutableCopy();

            for (final Entry<TextStylePropertyName<?>, Object> propertyNameAndValue : values.entrySet()) {
                final TextStylePropertyName<?> propertyName = propertyNameAndValue.getKey();
                final Object value = propertyNameAndValue.getValue();

                propertyName.checkValue(value);

                switch (propertyName.name) {
                    case BORDER_COLOR:
                        copy.put(
                            TextStylePropertyName.BORDER_TOP_COLOR,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.BORDER_LEFT_COLOR,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.BORDER_RIGHT_COLOR,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.BORDER_BOTTOM_COLOR,
                            value
                        );
                        break;
                    case BORDER_STYLE:
                        copy.put(
                            TextStylePropertyName.BORDER_TOP_STYLE,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.BORDER_LEFT_STYLE,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.BORDER_RIGHT_STYLE,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.BORDER_BOTTOM_STYLE,
                            value
                        );
                        break;
                    case BORDER_WIDTH:
                        copy.put(
                            TextStylePropertyName.BORDER_TOP_WIDTH,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.BORDER_LEFT_WIDTH,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.BORDER_RIGHT_WIDTH,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                            value
                        );
                        break;
                    case MARGIN:
                        copy.put(
                            TextStylePropertyName.MARGIN_TOP,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.MARGIN_LEFT,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.MARGIN_RIGHT,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.MARGIN_BOTTOM,
                            value
                        );
                        break;
                    case PADDING:
                        copy.put(
                            TextStylePropertyName.PADDING_TOP,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.PADDING_LEFT,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.PADDING_RIGHT,
                            value
                        );
                        copy.put(
                            TextStylePropertyName.PADDING_BOTTOM,
                            value
                        );
                        break;
                    default:
                        copy.put(
                            propertyName,
                            value
                        );
                        break;
                }
            }
        }

        return copy.isEmpty() ?
            TextStyle.EMPTY :
            this.setValuesWithCopy(copy);

    }

    /**
     * Assumes the old and new values have been copied and proceeds to return a {@link TextStyle} with the new values if necessary.
     */
    abstract TextStyle setValuesWithCopy(final Map<TextStylePropertyName<?>, Object> values);

    // remove...........................................................................................................

    /**
     * Removes a possibly existing property returning a {@link TextStyle} without.
     * {@link TextStylePropertyName#WILDCARD} is a special case and always returns a {@link #EMPTY}.
     */
    @Override
    public final TextStyle remove(final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        return propertyName == TextStylePropertyName.WILDCARD ?
            TextStyle.EMPTY :
            this.removeNonNull(propertyName);
    }

    abstract TextStyle removeNonNull(final TextStylePropertyName<?> propertyName);

    // setOrRemove......................................................................................................

    /**
     * Does a set or remove if the value is null.
     */
    @Override
    public final <V> TextStyle setOrRemove(final TextStylePropertyName<V> propertyName, final V value) {
        return null != value ?
            this.set(propertyName, value) :
            this.remove(propertyName);
    }

    // TextStyleVisitor.................................................................................................

    abstract void accept(final TextStyleVisitor visitor);

    // BoxEdge........................................................................................................

    abstract Border border(final BoxEdge edge);

    abstract Margin margin(final BoxEdge edge);

    abstract Padding padding(final BoxEdge edge);

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.value()
            .hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
            null != other && this.getClass() == other.getClass() &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextStyle other) {
        return this.value().equals(other.value());
    }

    @Override
    abstract public String toString();

    // JsonNodeContext..................................................................................................

    /**
     * Accepts a json object holding the styles as a map.
     */
    static TextStyle unmarshall(final JsonNode node,
                                final JsonNodeUnmarshallContext context) {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();

        for (JsonNode child : node.objectOrFail().children()) {
            final TextStylePropertyName<?> name = TextStylePropertyName.unmarshall(child);
            properties.put(name,
                name.handler.unmarshall(child, name, context));
        }

        return TextStyle.EMPTY.setValues(properties);
    }

    abstract JsonNode marshall(final JsonNodeMarshallContext context);

    static {
        JsonNodeContext.register("text-style",
            TextStyle::unmarshall,
            TextStyle::marshall,
            TextStyle.class,
            TextStyleNonEmpty.class,
            TextStyleEmpty.class);
    }

    // Patchable........................................................................................................

    /**
     * Performs a patch on this {@link TextStyle} returning the result.<br>
     * A null patch will return a {@link #EMPTY} this is necessary to support something like clear formatting<br>
     * Null values will result in the property being removed.
     */
    @Override
    public final TextStyle patch(final JsonNode patch,
                                 final JsonNodeUnmarshallContext context) {
        Objects.requireNonNull(patch, "patch");
        Objects.requireNonNull(context, "context");

        return patch.isNull() ?
            TextStyle.EMPTY :
            this.patchNonNull(patch, context);
    }

    private TextStyle patchNonNull(final JsonNode patch,
                                   final JsonNodeUnmarshallContext context) {
        TextStyle result = this;

        for (final JsonNode nameAndValue : patch.objectOrFail().children()) {
            final TextStylePropertyName<?> name = TextStylePropertyName.unmarshall(nameAndValue);
            result = result.setOrRemove(
                name,
                nameAndValue.isNull() ?
                    null :
                    Cast.to(
                        name.handler.unmarshall(nameAndValue, name, context)
                    )
            );
        }

        return result;
    }

    // HasTextStyle.....................................................................................................

    /**
     * Always returns this.
     */
    @Override
    public final TextStyle textStyle() {
        return this;
    }

    // HasProperties....................................................................................................

    @Override
    public final Properties properties() {
        Properties properties = Properties.EMPTY;

        for(final Entry<TextStylePropertyName<?>, ?> nameAndValue : this.textStyleMap().entries ) {
            final TextStylePropertyName<?> name = nameAndValue.getKey();
            properties = properties.set(
                name.propertiesPath,
                name.handler.makeString(
                    Cast.to(
                        nameAndValue.getValue()
                    )
                )
            );
        }

        return properties;
    }
}
