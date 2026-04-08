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
import java.util.function.Function;
import java.util.function.Predicate;

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

    static final String BORDER = "border";

    static final String MARGIN = "margin";

    static final String PADDING = "padding";

    static final String WILDCARD = "*";

    public final static CharacterConstant ASSIGNMENT = CharacterConstant.with(':');

    public final static CharacterConstant SEPARATOR = CharacterConstant.with(';');

    // parse............................................................................................................

    /**
     * Parses an almost CSS like declaration into a {@link TextStyle}.
     * Note that enclosing the entries with open and close braces will result in an {@link InvalidCharacterException}.
     * <pre>
     * border-bottom-color: white;
     * border-bottom-style: dashed;
     * border-bottom-width: 34px;
     * border-left-color: white;
     * border-left-style: dashed;
     * border-left-width: 34px;
     * border-right-color: white;
     * border-right-style: dashed;
     * border-right-width: 34px;
     * border-top-color: white;
     * border-top-style: dashed;
     * border-top-width: 34px;
     * </pre>
     * Note that whitespace after the semi-colon separator is optional.
     * Passing the text from {@link #text()} to parse will give an equal {@link TextStyle}.
     */
    public static TextStyle parse(final String text) {
        return parse1(
            text,
            TextStylePropertyName::with
        );
    }

    static TextStyle parse0(final String text,
                            final String prefix,
                            final Function<TextStylePropertyName<?>, Boolean> checker,
                            final Function<TextStylePropertyName<?>, InvalidTextStylePropertyNameException> invalid) {
        return parse1(
            text,
            (String propertyName) -> {
                final TextStylePropertyName<?> textStylePropertyName = TextStylePropertyName.withOptionalPrefix(
                    propertyName,
                    prefix
                );

                if (false == checker.apply(textStylePropertyName)) {
                    // Invalid border property "margin-top"
                    throw invalid.apply(textStylePropertyName);
                }

                return textStylePropertyName;
            }
        );
    }

    private static TextStyle parse1(final String text,
                                    final Function<String, TextStylePropertyName<?>> propertyNameFactory) {
        // WHITESPACE, TextStylePropertyName, WHITESPACE, COLON,
        // WHITESPACE VALUE WHITESPACE
        // COMMA | SEMI-COLON

        final TextStyleParser parser = TextStyleParser.with(
            text,
            propertyNameFactory
        );
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
        return TextStyleNode.with(textNodes, this.textStylePropertiesMap());
    }

    /**
     * A {@link TextStylePropertiesMap} holding the {@link TextStylePropertyName} and values.
     */
    abstract TextStylePropertiesMap textStylePropertiesMap();

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
     * Gets the value for the given {@link TextStylePropertyName}. If the property name is {@link TextStylePropertyName#WILDCARD},
     * this {@link TextStyle} will be returned.
     */
    public final <V> Optional<V> get(final TextStylePropertyName<V> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        return TextStylePropertyName.WILDCARD == propertyName ?
            Optional.of(
                propertyName.cast(this)
            ) :
            this.getNonNull(propertyName);
    }

    abstract <V> Optional<V> getNonNull(final TextStylePropertyName<V> propertyName);

    /**
     * Gets the given property or throws an {@link IllegalArgumentException}
     */
    public final <V> V getOrFail(final TextStylePropertyName<V> propertyName) {
        return this.get(propertyName)
            .orElseThrow(propertyName::missingTextStylePropertyNameException);
    }

    // set..............................................................................................................

    /**
     * Sets a possibly new property returning a {@link TextStyle} with the new definition which may or may not
     * require creating a new {@link TextStyle}.
     * A with {@link TextStylePropertyName#WILDCARD} will simply return the {@link TextStyle value}.
     */
    @Override
    public final <V> TextStyle set(final TextStylePropertyName<V> propertyName,
                                   final V value) {
        Objects.requireNonNull(propertyName, "propertyName");

        propertyName.checkValue(value);

        final TextStyle set;

        switch (propertyName.name) {
            case BORDER_COLOR:
                set = this.setTopRightBottomLeft(
                    TextStylePropertyName.BORDER_TOP_COLOR,
                    TextStylePropertyName.BORDER_RIGHT_COLOR,
                    TextStylePropertyName.BORDER_BOTTOM_COLOR,
                    TextStylePropertyName.BORDER_LEFT_COLOR,
                    (Color) value
                );
                break;
            case BORDER_STYLE:
                set = this.setTopRightBottomLeft(
                    TextStylePropertyName.BORDER_TOP_STYLE,
                    TextStylePropertyName.BORDER_RIGHT_STYLE,
                    TextStylePropertyName.BORDER_BOTTOM_STYLE,
                    TextStylePropertyName.BORDER_LEFT_STYLE,
                    (BorderStyle) value
                );
                break;
            case BORDER_WIDTH:
                set = this.setTopRightBottomLeft(
                    TextStylePropertyName.BORDER_TOP_WIDTH,
                    TextStylePropertyName.BORDER_RIGHT_WIDTH,
                    TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                    TextStylePropertyName.BORDER_LEFT_WIDTH,
                    (Length<?>) value
                );
                break;
            case BORDER:
                final TextStylePropertiesMap borderTextStylePropertiesMap = this.copy();
                borderTextStylePropertiesMap.setBorder(
                    (Border)value
                );
                set = this.setValues(borderTextStylePropertiesMap);
                break;
            case MARGIN:
            case PADDING:
                set = this.merge(
                    ((HasTextStyle) value)
                        .textStyle()
                );
                break;
            case WILDCARD:
                set = this.equals(value) ?
                    this :
                    TextStylePropertyName.WILDCARD.cast(value);
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

    final <T> TextStyle setTopRightBottomLeft(final TextStylePropertyName<T> topName,
                                              final TextStylePropertyName<T> leftName,
                                              final TextStylePropertyName<T> rightName,
                                              final TextStylePropertyName<T> bottomName,
                                              final T value) {
        final TextStylePropertiesMap values = this.copy();

        values.setTextStyleProperty(
            topName,
            value
        );
        values.setTextStyleProperty(
            rightName,
            value
        );
        values.setTextStyleProperty(
            bottomName,
            value
        );
        values.setTextStyleProperty(
            leftName,
            value
        );

        return this.setTextStylePropertiesMap(values);
    }

    abstract <T> TextStyle setValue(final TextStylePropertyName<T> propertyName,
                                    final T value);

    public final TextStyle setBorder(final Optional<Border> border) {
        Objects.requireNonNull(border, "border");

        return this.setOrRemove(
            TextStylePropertyName.BORDER,
            border.orElse(null)
        );
    }

    public final TextStyle setMargin(final Optional<Margin> margin) {
        Objects.requireNonNull(margin, "margin");

        return this.setOrRemove(
            TextStylePropertyName.MARGIN,
            margin.orElse(null)
        );
    }

    public final TextStyle setPadding(final Optional<Padding> padding) {
        Objects.requireNonNull(padding, "padding");

        return this.setOrRemove(
            TextStylePropertyName.PADDING,
            padding.orElse(null)
        );
    }

    /**
     * Returns a mutable copy of the current properties for modification.
     */
    abstract TextStylePropertiesMap copy();

    /**
     * Would be setter that returns a {@link TextStyle} that has the given values in addition to what it previously contained,
     * returning a new instance if they are different values.
     */
    public final TextStyle setValues(final Map<TextStylePropertyName<?>, Object> values) {
        Objects.requireNonNull(values, "values");

        final TextStyle textStyle;

        if (values instanceof TextStylePropertiesMap) {
            textStyle = this.setTextStylePropertiesMap(
                (TextStylePropertiesMap)values
            );

        } else {
            final TextStylePropertiesMap copy = this.copy();

            for (final Entry<TextStylePropertyName<?>, Object> propertyNameAndValue : values.entrySet()) {
                final TextStylePropertyName<?> propertyName = propertyNameAndValue.getKey();
                final Object value = propertyNameAndValue.getValue();

                if(null!=value) {
                    propertyName.checkValue(value);
                }

                switch (propertyName.name) {
                    case BORDER:
                        final Border border = (Border) value;
                        if (null != border) {
                            copy.setBorder(border);
                        } else {
                            copy.removeBorder();
                        }
                        break;
                    case BORDER_COLOR:
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_TOP_COLOR,
                            value
                        );
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_RIGHT_COLOR,
                            value
                        );
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_BOTTOM_COLOR,
                            value
                        );
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_LEFT_COLOR,
                            value
                        );
                        break;
                    case BORDER_STYLE:
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_TOP_STYLE,
                            value
                        );
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_RIGHT_STYLE,
                            value
                        );
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_BOTTOM_STYLE,
                            value
                        );
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_LEFT_STYLE,
                            value
                        );
                        break;
                    case BORDER_WIDTH:
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_TOP_WIDTH,
                            value
                        );
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_RIGHT_WIDTH,
                            value
                        );
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                            value
                        );
                        copy.setTextStyleProperty(
                            TextStylePropertyName.BORDER_LEFT_WIDTH,
                            value
                        );
                        break;
                    case MARGIN:
                        final Margin margin = (Margin) value;
                        if(null != margin) {
                            final BoxEdge marginBoxEdge = margin.edge();
                            if (BoxEdge.ALL == marginBoxEdge) {
                                copy.setBorderMarginPadding(
                                    margin,
                                    TextStylePropertyName.MARGIN_TOP
                                );
                                copy.setBorderMarginPadding(
                                    margin,
                                    TextStylePropertyName.MARGIN_RIGHT
                                );
                                copy.setBorderMarginPadding(
                                    margin,
                                    TextStylePropertyName.MARGIN_BOTTOM
                                );
                                copy.setBorderMarginPadding(
                                    margin,
                                    TextStylePropertyName.MARGIN_LEFT
                                );
                            } else {
                                copy.setBorderMarginPadding(
                                    margin,
                                    marginBoxEdge.marginPropertyName()
                                );
                            }
                        } else {
                            copy.removeMargin();
                        }
                        break;
                    case PADDING:
                        final Padding padding = (Padding) value;
                        if (null != padding) {
                            final BoxEdge paddingBoxEdge = padding.edge();
                            if (BoxEdge.ALL == paddingBoxEdge) {
                                copy.setBorderMarginPadding(
                                    padding,
                                    TextStylePropertyName.PADDING_TOP
                                );
                                copy.setBorderMarginPadding(
                                    padding,
                                    TextStylePropertyName.PADDING_RIGHT
                                );
                                copy.setBorderMarginPadding(
                                    padding,
                                    TextStylePropertyName.PADDING_BOTTOM
                                );
                                copy.setBorderMarginPadding(
                                    padding,
                                    TextStylePropertyName.PADDING_LEFT
                                );
                            } else {
                                copy.setBorderMarginPadding(
                                    padding,
                                    paddingBoxEdge.paddingPropertyName()
                                );
                            }
                        } else {
                            copy.removePadding();
                        }
                        break;
                    default:
                        copy.setTextStyleProperty(
                            propertyName,
                            value
                        );
                        break;
                }
            }

            textStyle = this.setTextStylePropertiesMap(copy);
        }

        return textStyle;
    }

    /**
     * Assumes the old and new values have been copied and proceeds to return a {@link TextStyle} with the new values if necessary.
     */
    final TextStyle setTextStylePropertiesMap(final TextStylePropertiesMap values) {
        final TextStyle textStyle;

        if (values.isEmpty()) {
            textStyle = EMPTY;
        } else {
            final Map<TextStylePropertyName<?>, Object> previous = this.value();

            textStyle = previous.equals(values) ?
                this :
                TextStyleNonEmpty.with(values);
        }

        return textStyle;
    }

    // remove...........................................................................................................

    /**
     * Removes a possibly existing property returning a {@link TextStyle} without.
     * {@link TextStylePropertyName#WILDCARD} is a special case and always returns a {@link #EMPTY}.
     */
    @Override
    public final TextStyle remove(final TextStylePropertyName<?> propertyName) {
        return propertyName == TextStylePropertyName.WILDCARD ?
            TextStyle.EMPTY :
            this.removeNonNull(
                Objects.requireNonNull(propertyName, "propertyName")
            );
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

    abstract public Border border(final BoxEdge edge);

    abstract public Margin margin(final BoxEdge edge);

    abstract public Padding padding(final BoxEdge edge);

    abstract TextStyle filter(final Predicate<TextStylePropertyName<?>> filter);

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

        for(final Entry<TextStylePropertyName<?>, ?> nameAndValue : this.textStylePropertiesMap().entrySet() ) {
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

    // toText(Function).................................................................................................

    abstract String toText(final Function<TextStylePropertyName<?>, String> propertyNameMapper);
}
