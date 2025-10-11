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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.naming.Name;
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasText;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printer;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A {@link TextStyleNonEmpty} holds a non empty {@link Map} of {@link TextStylePropertyName} and values.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class TextStyleNonEmpty extends TextStyle {

    /**
     * Factory that creates a {@link TextStyleNonEmpty} from a {@link TextStylePropertiesMap}.
     */
    static TextStyleNonEmpty with(final TextStylePropertiesMap value) {
        return new TextStyleNonEmpty(value);
    }

    private TextStyleNonEmpty(final TextStylePropertiesMap value) {
        super();
        this.value = value;
    }

    /**
     * Always returns false
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    // Value..........................................................................................................

    @Override
    public Map<TextStylePropertyName<?>, Object> value() {
        return this.value;
    }

    @Override
    Map<TextStylePropertyName<?>, Object> valuesMutableCopy() {
        final Map<TextStylePropertyName<?>, Object> copy = Maps.sorted();
        copy.putAll(this.value);
        return copy;
    }

    @Override
    TextStyle setValuesWithCopy(final Map<TextStylePropertyName<?>, Object> values) {
        final Map<TextStylePropertyName<?>, Object> oldValuesMap = this.value();

        return oldValuesMap.equals(values) ?
            this :
            TextStyleNonEmpty.with(
                TextStylePropertiesMap.with(values)
            );
    }

    final TextStylePropertiesMap value;

    // merge............................................................................................................

    @Override
    TextStyle merge0(final TextStyle textStyle) {
        return textStyle.merge1(this);
    }

    @Override
    TextStyle merge1(final TextStyleNonEmpty other) {
        final Map<TextStylePropertyName<?>, Object> otherBefore = other.value; // because of double dispatch params are reversed.
        final Map<TextStylePropertyName<?>, Object> before = this.value;


        final Map<TextStylePropertyName<?>, Object> merged = Maps.sorted();
        merged.putAll(otherBefore);
        merged.putAll(before);

        return merged.equals(otherBefore) ?
            this :
            merged.equals(before) ?
                other :
                new TextStyleNonEmpty(TextStylePropertiesMap.with(merged));
    }

    // replace..........................................................................................................

    @Override
    TextNode replaceNonNull(final TextNode textNode) {
        return textNode.setAttributesNonEmpty(this.value);
    }

    // setChildren......................................................................................................

    @Override
    TextStylePropertiesMap textStyleMap() {
        return this.value;
    }

    // get..............................................................................................................

    @Override //
    <V> Optional<V> getNonNull(final TextStylePropertyName<V> propertyName) {
        Object get = null;

        final TextStylePropertiesMap value = this.value;

        switch (propertyName.name) {
            case BORDER_COLOR:
                // only return a non null color if all colors are the same.
                final Object borderColor = value.get(TextStylePropertyName.BORDER_TOP_COLOR);
                if (null != borderColor) {
                    if (borderColor.equals(value.get(TextStylePropertyName.BORDER_LEFT_COLOR))) {
                        if (borderColor.equals(value.get(TextStylePropertyName.BORDER_RIGHT_COLOR))) {
                            if (borderColor.equals(value.get(TextStylePropertyName.BORDER_BOTTOM_COLOR))) {
                                get = borderColor;
                            }
                        }
                    }
                }
                break;
            case BORDER_STYLE:
                // only return a non null style if all styles are the same.
                final Object borderStyle = value.get(TextStylePropertyName.BORDER_TOP_STYLE);
                if (null != borderStyle) {
                    if (borderStyle.equals(value.get(TextStylePropertyName.BORDER_LEFT_STYLE))) {
                        if (borderStyle.equals(value.get(TextStylePropertyName.BORDER_RIGHT_STYLE))) {
                            if (borderStyle.equals(value.get(TextStylePropertyName.BORDER_BOTTOM_STYLE))) {
                                get = borderStyle;
                            }
                        }
                    }
                }
                break;
            case BORDER_WIDTH:
                // only return a non null width if all widths are the same.
                final Object borderWidth = value.get(TextStylePropertyName.BORDER_TOP_WIDTH);
                if (null != borderWidth) {
                    if (borderWidth.equals(value.get(TextStylePropertyName.BORDER_LEFT_WIDTH))) {
                        if (borderWidth.equals(value.get(TextStylePropertyName.BORDER_RIGHT_WIDTH))) {
                            if (borderWidth.equals(value.get(TextStylePropertyName.BORDER_BOTTOM_WIDTH))) {
                                get = borderWidth;
                            }
                        }
                    }
                }
                break;
            case MARGIN:
                // only return a non null width if all margins are the same.
                final Object marginWidth = value.get(TextStylePropertyName.MARGIN_TOP);
                if (null != marginWidth) {
                    if (marginWidth.equals(value.get(TextStylePropertyName.MARGIN_LEFT))) {
                        if (marginWidth.equals(value.get(TextStylePropertyName.MARGIN_RIGHT))) {
                            if (marginWidth.equals(value.get(TextStylePropertyName.MARGIN_BOTTOM))) {
                                get = marginWidth;
                            }
                        }
                    }
                }
                break;
            case PADDING:
                // only return a non null width if all paddings are the same.
                final Object paddingWidth = value.get(TextStylePropertyName.PADDING_TOP);
                if (null != paddingWidth) {
                    if (paddingWidth.equals(value.get(TextStylePropertyName.PADDING_LEFT))) {
                        if (paddingWidth.equals(value.get(TextStylePropertyName.PADDING_RIGHT))) {
                            if (paddingWidth.equals(value.get(TextStylePropertyName.PADDING_BOTTOM))) {
                                get = paddingWidth;
                            }
                        }
                    }
                }
                break;
            default:
                get = value.get(propertyName);
                break;
        }

        return Optional.ofNullable(
            Cast.to(get)
        );
    }

    // set..............................................................................................................

    @Override //
    <V> TextStyle setNonNull(final TextStylePropertyName<V> propertyName,
                             final V value) {
        TextStyle result;

        switch (propertyName.name) {
            case BORDER_COLOR:
                final Color color = Cast.to(value);

                result = this.setTopLeftRightBottom(
                    TextStylePropertyName.BORDER_TOP_COLOR,
                    color,
                    TextStylePropertyName.BORDER_LEFT_COLOR,
                    color,
                    TextStylePropertyName.BORDER_RIGHT_COLOR,
                    color,
                    TextStylePropertyName.BORDER_BOTTOM_COLOR,
                    color
                );
                break;
            case BORDER_STYLE:
                final BorderStyle borderStyle = Cast.to(value);

                result = this.setTopLeftRightBottom(
                    TextStylePropertyName.BORDER_TOP_STYLE,
                    borderStyle,
                    TextStylePropertyName.BORDER_LEFT_STYLE,
                    borderStyle,
                    TextStylePropertyName.BORDER_RIGHT_STYLE,
                    borderStyle,
                    TextStylePropertyName.BORDER_BOTTOM_STYLE,
                    borderStyle
                );
                break;
            case BORDER_WIDTH:
                final Length<?> borderWidth = Cast.to(value);

                result = this.setTopLeftRightBottom(
                    TextStylePropertyName.BORDER_TOP_WIDTH,
                    borderWidth,
                    TextStylePropertyName.BORDER_LEFT_WIDTH,
                    borderWidth,
                    TextStylePropertyName.BORDER_RIGHT_WIDTH,
                    borderWidth,
                    TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                    borderWidth
                );
                break;
            case MARGIN:
                final Length<?> margin = Cast.to(value);

                result = this.setTopLeftRightBottom(
                    TextStylePropertyName.MARGIN_TOP,
                    margin,
                    TextStylePropertyName.MARGIN_LEFT,
                    margin,
                    TextStylePropertyName.MARGIN_RIGHT,
                    margin,
                    TextStylePropertyName.MARGIN_BOTTOM,
                    margin
                );
                break;
            case PADDING:
                final Length<?> padding = Cast.to(value);

                result = this.setTopLeftRightBottom(
                    TextStylePropertyName.PADDING_TOP,
                    padding,
                    TextStylePropertyName.PADDING_LEFT,
                    padding,
                    TextStylePropertyName.PADDING_RIGHT,
                    padding,
                    TextStylePropertyName.PADDING_BOTTOM,
                    padding
                );
                break;
            default:
                result = this.setValue(
                    propertyName,
                    value
                );
                break;
        }

        return result;
    }

    private <T> TextStyle setTopLeftRightBottom(final TextStylePropertyName<T> topName,
                                                final T topValue,
                                                final TextStylePropertyName<T> leftName,
                                                final T leftValue,
                                                final TextStylePropertyName<T> rightName,
                                                final T rightValue,
                                                final TextStylePropertyName<T> bottomName,
                                                final T bottomValue) {
        return this.setValues(
            Maps.of(
                topName,
                topValue,
                leftName,
                leftValue,
                rightName,
                rightValue,
                bottomName,
                bottomValue
            )
        );
    }

    private <V> TextStyleNonEmpty setValue(final TextStylePropertyName<V> propertyName,
                                           final V value) {
        TextStylePropertiesMap map = this.value;
        final List<Entry<TextStylePropertyName<?>, Object>> list = Lists.array();

        int mode = 0; // new property added.

        for (final Entry<TextStylePropertyName<?>, Object> propertyAndValue : map.entries) {
            final TextStylePropertyName<?> property = propertyAndValue.getKey();

            if (propertyName.equals(property)) {
                if (propertyAndValue.getValue().equals(value)) {
                    mode = 1; // no change
                    break;
                } else {
                    list.add(Maps.entry(property, value));
                    mode = 2; // replaced
                }
            } else {
                list.add(propertyAndValue);
            }
        }

        // replace didnt happen
        if (0 == mode) {
            list.add(Maps.entry(propertyName, value));
            TextStylePropertiesMapEntrySet.sort(list);
        }

        return 1 == mode ?
            this :
            new TextStyleNonEmpty(
                TextStylePropertiesMap.withTextStyleMapEntrySet(
                    TextStylePropertiesMapEntrySet.withList(list)
                )
            );
    }

    // remove...........................................................................................................

    @Override
    TextStyle removeNonNull(final TextStylePropertyName<?> propertyName) {
        final List<Entry<TextStylePropertyName<?>, Object>> list = Lists.array();
        boolean removed = false;

        Predicate<TextStylePropertyName<?>> removeIf;

        switch (propertyName.name) {
            case BORDER_COLOR:
                removeIf = BORDER_XXX_COLOR;
                break;
            case BORDER_STYLE:
                removeIf = BORDER_XXX_STYLE;
                break;
            case BORDER_WIDTH:
                removeIf = BORDER_XXX_WIDTH;
                break;
            case MARGIN:
                removeIf = MARGIN_XXX;
                break;
            case PADDING:
                removeIf = PADDING_XXX;
                break;
            default:
                removeIf = propertyName::equals;
                break;
        }

        for (final Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.value.entries) {
            final TextStylePropertyName<?> property = propertyAndValue.getKey();
            if (removeIf.test(property)) {
                removed = true;
            } else {
                list.add(propertyAndValue);
            }
        }

        return removed ?
            this.removeNonNull0(list) :
            this;
    }

    /**
     * Used to remove any of the 4 BORDER_XXX_COLOR properties.
     */
    private static final Predicate<TextStylePropertyName<?>> BORDER_XXX_COLOR = Predicates.setContains(
        Sets.of(
            TextStylePropertyName.BORDER_TOP_COLOR,
            TextStylePropertyName.BORDER_LEFT_COLOR,
            TextStylePropertyName.BORDER_RIGHT_COLOR,
            TextStylePropertyName.BORDER_BOTTOM_COLOR
        )
    );

    /**
     * Used to remove any of the 4 BORDER_XXX_STYLE properties.
     */
    private static final Predicate<TextStylePropertyName<?>> BORDER_XXX_STYLE = Predicates.setContains(
        Sets.of(
            TextStylePropertyName.BORDER_TOP_STYLE,
            TextStylePropertyName.BORDER_LEFT_STYLE,
            TextStylePropertyName.BORDER_RIGHT_STYLE,
            TextStylePropertyName.BORDER_BOTTOM_STYLE
        )
    );

    /**
     * Used to remove any of the 4 BORDER_XXX_WIDTH properties.
     */
    private static final Predicate<TextStylePropertyName<?>> BORDER_XXX_WIDTH = Predicates.setContains(
        Sets.of(
            TextStylePropertyName.BORDER_TOP_WIDTH,
            TextStylePropertyName.BORDER_LEFT_WIDTH,
            TextStylePropertyName.BORDER_RIGHT_WIDTH,
            TextStylePropertyName.BORDER_BOTTOM_WIDTH
        )
    );

    /**
     * Used to remove any of the 4 MARGIN_XXX properties.
     */
    private static final Predicate<TextStylePropertyName<?>> MARGIN_XXX = Predicates.setContains(
        Sets.of(
            TextStylePropertyName.MARGIN_TOP,
            TextStylePropertyName.MARGIN_LEFT,
            TextStylePropertyName.MARGIN_RIGHT,
            TextStylePropertyName.MARGIN_BOTTOM
        )
    );

    /**
     * Used to remove any of the 4 PADDING_XXX properties.
     */
    private static final Predicate<TextStylePropertyName<?>> PADDING_XXX = Predicates.setContains(
        Sets.of(
            TextStylePropertyName.PADDING_TOP,
            TextStylePropertyName.PADDING_LEFT,
            TextStylePropertyName.PADDING_RIGHT,
            TextStylePropertyName.PADDING_BOTTOM
        )
    );

    /**
     * Accepts a list after removing a property, special casing if the list is empty.
     */
    private TextStyle removeNonNull0(final List<Entry<TextStylePropertyName<?>, Object>> list) {
        return list.isEmpty() ?
            TextStyle.EMPTY :
            new TextStyleNonEmpty(
                TextStylePropertiesMap.withTextStyleMapEntrySet(
                    TextStylePropertiesMapEntrySet.withList(list)
                )
            ); // no need to sort after a delete
    }

    // TextStyleVisitor.................................................................................................

    @Override
    void accept(final TextStyleVisitor visitor) {
        this.value.accept(visitor);
    }

    // text.............................................................................................................

    @Override
    public String text() {
        if (null == this.css) {
            this.css = this.buildCss();
        }

        return this.css;
    }

    /**
     * A lazily populated cache of the css
     */
    private String css;

    /**
     * Prints each property and value with spaces after each separator.
     *
     * <pre>
     * border-top-color: #123456; border-top-style: dotted; border-top-width: 123px;
     * </pre>
     */
    private String buildCss() {
        final StringBuilder cssStringBuilder = new StringBuilder();

        try (final Printer css = Printers.stringBuilder(cssStringBuilder, LineEnding.SYSTEM)) {
            String separator = "";

            for (final Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.value.entrySet()) {
                css.print(separator);

                final TextStylePropertyName<?> propertyName = propertyAndValue.getKey();
                css.print(propertyName.value());
                css.print(TextStyle.ASSIGNMENT);
                css.print(" ");

                final Object value = propertyAndValue.getValue();
                final CharSequence valueCss = valueToCss(value);

                css.print(valueCss);
                css.print(TextStyle.SEPARATOR.string());

                separator = " ";
            }
        }

        return cssStringBuilder.toString();
    }

    private static CharSequence valueToCss(final Object value) {
        final CharSequence css;

        // dont want to handle values such as FontFamily (which implements Name) here.
        // Name extends HasText
        if (value instanceof HasText && false == value instanceof Name) {
            final HasText hasText = (HasText) value;
            css = hasText.text();
        } else {
            if (value instanceof Enum) {
                final Enum<?> enumEnum = (Enum<?>) value;
                css = CaseKind.SNAKE.change(
                    enumEnum.name().toLowerCase(),
                    CaseKind.KEBAB
                );
            } else {
                final String stringValue = value.toString();
                if (stringValue.indexOf(' ') >= 0) {
                    css = CharSequences.quoteAndEscape(stringValue);
                } else {
                    css = stringValue;
                }
            }
        }

        return css;
    }

    // BoxEdge........................................................................................................

    @Override
    Border border(final BoxEdge edge) {
        return Border.with(edge, this);
    }

    @Override
    Margin margin(final BoxEdge edge) {
        return Margin.with(edge, this);
    }

    @Override
    Padding padding(final BoxEdge edge) {
        return Padding.with(edge, this);
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println("TextStyle");
        printer.indent();

        for (final Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.value.entrySet()) {
            printer.print(propertyAndValue.getKey().value());
            printer.print("=");

            final Object value = propertyAndValue.getValue();
            printer.print(CharSequences.quoteIfChars(value));
            printer.print(" (");
            printer.print(value.getClass().getName());
            printer.print(")");
            printer.println();
        }

        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.value.toString();
    }

    // JsonNodeContext..................................................................................................

    /**
     * Creates a json-object where the properties are strings, and the value without types.
     */
    @Override
    JsonNode marshall(final JsonNodeMarshallContext context) {
        final List<JsonNode> json = Lists.array();

        for (Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.value.entrySet()) {
            final TextStylePropertyName<?> propertyName = propertyAndValue.getKey();
            final JsonNode value = propertyName.handler.marshall(Cast.to(propertyAndValue.getValue()), context);

            json.add(value.setName(propertyName.marshallName()));
        }

        return JsonNode.object()
            .setChildren(json);
    }
}
