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
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
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
        HasCss,
        CanBeEmpty {

    /**
     * A {@link TextStyle} with no styling.
     */
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public final static TextStyle EMPTY = TextStyleEmpty.instance();

    static final String BORDER_COLOR = "border-color";

    static final String BORDER_STYLE = "border-style";

    static final String BORDER_WIDTH = "border-width";

    static final String MARGIN = "margin";

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
    abstract TextNodeMap textStyleMap();

    // merge............................................................................................................

    /**
     * Merges the two {@link TextStyle}, with the value from this having priority when both have the same
     * {@link TextStylePropertyName}.
     */
    public final TextStyle merge(final TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle");

        return this.merge0(textStyle);
    }

    abstract TextStyle merge0(final TextStyle textStyle);

    abstract TextStyle merge1(final TextStyleNonEmpty textStyle);

    // replace............................................................................................................

    /**
     * If empty returns the given {@link TextNode} otherwise wraps inside a {@link TextStyleNode}
     */
    public final TextNode replace(final TextNode textNode) {
        Objects.requireNonNull(textNode, "textNode");

        return this.replace0(textNode);
    }

    abstract TextNode replace0(final TextNode textNode);

    // get..............................................................................................................

    /**
     * Gets the value for the given {@link TextStylePropertyName}.
     */
    public final <V> Optional<V> get(final TextStylePropertyName<V> propertyName) {
        checkPropertyName(propertyName);

        return this.get0(propertyName);
    }

    abstract <V> Optional<V> get0(final TextStylePropertyName<V> propertyName);

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
    public final <V> TextStyle set(final TextStylePropertyName<V> propertyName, final V value) {
        checkPropertyName(propertyName);

        propertyName.check(value);
        return this.set0(propertyName, value);
    }

    abstract <V> TextStyle set0(final TextStylePropertyName<V> propertyName, final V value);

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
            final TextStylePropertyName<Color> colorPropertyName = boxEdge.borderColorPropertyName();
            colorPropertyName.check(color);

            colorStyleWidth.put(
                    colorPropertyName,
                    color
            );

            final TextStylePropertyName<BorderStyle> stylePropertyName = boxEdge.borderStylePropertyName();
            stylePropertyName.check(style);

            colorStyleWidth.put(
                    stylePropertyName,
                    style
            );

            final TextStylePropertyName<Length<?>> widthPropertyName = boxEdge.borderWidthPropertyName();
            widthPropertyName.check(width);

            colorStyleWidth.put(
                    widthPropertyName,
                    width
            );
        }

        return this.setValuesWithCopy(colorStyleWidth);
    }

    /**
     * Sets all margin to the given {@link Length}
     */
    public final TextStyle setMargin(final Length<?> length) {
        Objects.requireNonNull(length, "length");

        TextStylePropertyName.MARGIN_TOP.check(length);
        TextStylePropertyName.MARGIN_RIGHT.check(length);
        TextStylePropertyName.MARGIN_BOTTOM.check(length);
        TextStylePropertyName.MARGIN_LEFT.check(length);

        final Map<TextStylePropertyName<?>, Object> newValues = this.valuesMutableCopy();

        newValues.put(
                TextStylePropertyName.MARGIN_TOP,
                length
        );
        newValues.put(
                TextStylePropertyName.MARGIN_RIGHT,
                length
        );
        newValues.put(
                TextStylePropertyName.MARGIN_BOTTOM,
                length
        );
        newValues.put(
                TextStylePropertyName.MARGIN_LEFT,
                length
        );

        return this.setValuesWithCopy(newValues);
    }

    /**
     * Sets all padding to the given {@link Length}
     */
    public final TextStyle setPadding(final Length<?> length) {
        Objects.requireNonNull(length, "length");

        TextStylePropertyName.PADDING_TOP.check(length);
        TextStylePropertyName.PADDING_RIGHT.check(length);
        TextStylePropertyName.PADDING_BOTTOM.check(length);
        TextStylePropertyName.PADDING_LEFT.check(length);

        final Map<TextStylePropertyName<?>, Object> newValues = this.valuesMutableCopy();

        newValues.put(
                TextStylePropertyName.PADDING_TOP,
                length
        );
        newValues.put(
                TextStylePropertyName.PADDING_RIGHT,
                length
        );
        newValues.put(
                TextStylePropertyName.PADDING_BOTTOM,
                length
        );
        newValues.put(
                TextStylePropertyName.PADDING_LEFT,
                length
        );

        return this.setValuesWithCopy(newValues);
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

        if (values instanceof TextNodeMap) {
            copy = values;
        } else {
            copy = this.valuesMutableCopy();

            for (final Entry<TextStylePropertyName<?>, Object> propertyNameAndValue : values.entrySet()) {
                final TextStylePropertyName<?> propertyName = propertyNameAndValue.getKey();
                final Object value = propertyNameAndValue.getValue();

                propertyName.check(value);

                switch (propertyName.name) {
                    case "border-color":
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
                    case "border-style":
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
                    case "border-width":
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
                    case "margin":
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
     * {@link TextStylePropertyName#ALL} is a special case and always returns a {@link #EMPTY}.
     */
    public final TextStyle remove(final TextStylePropertyName<?> propertyName) {
        checkPropertyName(propertyName);

        return propertyName == TextStylePropertyName.ALL ?
                TextStyle.EMPTY :
                this.remove0(propertyName);
    }

    abstract TextStyle remove0(final TextStylePropertyName<?> propertyName);

    private static void checkPropertyName(final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");
    }

    // setOrRemove......................................................................................................

    /**
     * Does a set or remove if the value is null.
     */
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
    abstract public int hashCode();

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEquals(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEquals(final Object other);

    abstract boolean equals0(final TextStyle other);

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
        TextStylePropertyName.BACKGROUND_COLOR.toString();

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
}
