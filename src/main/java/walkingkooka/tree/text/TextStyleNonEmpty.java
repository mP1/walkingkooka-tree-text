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
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printer;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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
    TextStylePropertiesMap copy() {
        return this.value.copy();
    }

    final TextStylePropertiesMap value;

    // merge............................................................................................................

    @Override
    TextStyle mergeNonNull(final TextStyle textStyle) {
        return textStyle.mergeNonEmpty(this);
    }

    @Override
    TextStyle mergeNonEmpty(final TextStyleNonEmpty other) {
        final TextStylePropertiesMap merged = other.value.copy();

        boolean equals = true;

        for (int i = 0; i < merged.values.length; i++) {
            final Object value = this.value.values[i];

            if (null != value) {
                equals = equals &
                    Objects.equals(
                        value,
                        merged.setTextStyleProperty(
                            TextStylePropertyName.NAMES[i],
                            value
                        )
                    );
            }
        }

        return equals ?
            other :
            TextStyleNonEmpty.with(merged);
    }

    // replace..........................................................................................................

    @Override
    TextNode replaceNonNull(final TextNode textNode) {
        return textNode.setAttributesNonEmpty(this.value);
    }

    // setChildren......................................................................................................

    @Override
    TextStylePropertiesMap textStylePropertiesMap() {
        return this.value;
    }

    // get..............................................................................................................

    @Override //
    <V> Optional<V> getNonNull(final TextStylePropertyName<V> propertyName) {
        Object get = null;

        final TextStylePropertiesMap value = this.value;

        switch (propertyName.name) {
            case BORDER:
                get = this.getBorder(BoxEdge.ALL);
                break;
            case BORDER_BOTTOM:
                get = this.getBorder(BoxEdge.BOTTOM);
                break;
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
            case BORDER_LEFT:
                get = this.getBorder(BoxEdge.LEFT);
                break;
            case BORDER_RIGHT:
                get = this.getBorder(BoxEdge.RIGHT);
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
                final Margin margin = BoxEdge.ALL.margin(this);
                if (margin.isNotEmpty()) {
                    get = margin;
                }
                break;
            case PADDING:
                final Padding padding = BoxEdge.ALL.padding(this);
                if (padding.isNotEmpty()) {
                    get = padding;
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

    private Border getBorder(final BoxEdge edge) {
        final Border border = edge.border(this);
        return border.isNotEmpty() ?
            border :
            null;
    }

    // set..............................................................................................................

    @Override
    <V> TextStyleNonEmpty setValue(final TextStylePropertyName<V> propertyName,
                                   final V value) {
        final TextStyleNonEmpty textStyle;

        final TextStylePropertiesMap map = this.value;
        final Object previousValue = map.get(propertyName);

        if (value.equals(previousValue)) {
            textStyle = this;
        } else {
            final TextStylePropertiesMap copy = map.copy();
            copy.setTextStyleProperty(
                propertyName,
                value
            );

            textStyle = with(copy);
        }

        return textStyle;
    }

    // remove...........................................................................................................

    @Override
    TextStyle removeNonNull(final TextStylePropertyName<?> propertyName) {
        final TextStyle removed;

        switch (propertyName.name) {
            case BORDER:
                removed = this.removeBorder(BoxEdge.ALL);
                break;
            case BORDER_BOTTOM:
                removed = this.removeBorder(BoxEdge.BOTTOM);
                break;
            case BORDER_COLOR:
                removed = this.removeValues(
                    TextStylePropertyName.BORDER_TOP_COLOR,
                    TextStylePropertyName.BORDER_LEFT_COLOR,
                    TextStylePropertyName.BORDER_RIGHT_COLOR,
                    TextStylePropertyName.BORDER_BOTTOM_COLOR
                );
                break;
            case BORDER_LEFT:
                removed = this.removeBorder(BoxEdge.LEFT);
                break;
            case BORDER_RIGHT:
                removed = this.removeBorder(BoxEdge.RIGHT);
                break;
            case BORDER_STYLE:
                removed = this.removeValues(
                    TextStylePropertyName.BORDER_TOP_STYLE,
                    TextStylePropertyName.BORDER_LEFT_STYLE,
                    TextStylePropertyName.BORDER_RIGHT_STYLE,
                    TextStylePropertyName.BORDER_BOTTOM_STYLE
                );
                break;
            case BORDER_WIDTH:
                removed = this.removeValues(
                    TextStylePropertyName.BORDER_TOP_WIDTH,
                    TextStylePropertyName.BORDER_LEFT_WIDTH,
                    TextStylePropertyName.BORDER_RIGHT_WIDTH,
                    TextStylePropertyName.BORDER_BOTTOM_WIDTH
                );
                break;
            case MARGIN:
                final TextStylePropertiesMap textStylePropertiesMap = this.copy();
                textStylePropertiesMap.removeMargin();
                removed = this.setValues(textStylePropertiesMap);
                break;
            case PADDING:
                final TextStylePropertiesMap textStylePropertiesMap2 = this.copy();
                textStylePropertiesMap2.removePadding();
                removed = this.setValues(textStylePropertiesMap2);
                break;
            default:
                final int index = propertyName.index();
                final TextStylePropertiesMap values = this.value;
                if (null != values.values[index]) {
                    if (1 == values.size()) {
                        removed = EMPTY;
                    } else {
                        final TextStylePropertiesMap mapRemoved = values.copy();
                        mapRemoved.removeTextStyleProperty(propertyName);
                        removed = TextStyleNonEmpty.with(mapRemoved);
                    }
                } else {
                    removed = this;
                }
                break;
        }

        return removed;
    }

    private TextStyle removeBorder(final BoxEdge boxEdge) {
        final TextStylePropertiesMap borderBottomTextStylePropertiesMap = this.copy();
        borderBottomTextStylePropertiesMap.removeBorder(boxEdge);
        return this.setValues(borderBottomTextStylePropertiesMap);
    }

    private <T> TextStyle removeValues(final TextStylePropertyName<T> top,
                                       final TextStylePropertyName<T> left,
                                       final TextStylePropertyName<T> right,
                                       final TextStylePropertyName<T> bottom) {
        return this.remove(top)
            .remove(left)
            .remove(right)
            .remove(bottom); // replace with more efficient later
    }

    // TextStyleVisitor.................................................................................................

    @Override
    void accept(final TextStyleVisitor visitor) {
        this.value.accept(visitor);
    }

    // text.............................................................................................................

    @Override
    public String text() {
        if (null == this.text) {
            this.text = this.toText(
                TextStylePropertyName::value
            );
        }

        return this.text;
    }

    private String text;

    /**
     * Prints each property and value with spaces after each separator. The provider {@link Function} may be used to
     * modify each {@link TextStylePropertyName}, such as removing a common prefix.
     *
     * <pre>
     * border-top-color: #123456; border-top-style: dotted; border-top-width: 123px;
     * </pre>
     */
    @Override
    String toText(final Function<TextStylePropertyName<?>, String> propertyNameMapper) {
        final StringBuilder b = new StringBuilder();

        try (final Printer text = Printers.stringBuilder(b, LineEnding.SYSTEM)) {
            String separator = "";

            for (final Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.value.entrySet()) {
                text.print(separator);

                final TextStylePropertyName<?> propertyName = propertyAndValue.getKey();
                text.print(
                    propertyNameMapper.apply(propertyName)
                );
                text.print(TextStyle.ASSIGNMENT);
                text.print(" ");

                final Object value = propertyAndValue.getValue();
                final CharSequence valueCss = toTextValue(value);

                text.print(valueCss);
                text.print(TextStyle.SEPARATOR.string());

                separator = " ";
            }
        }

        return b.toString();
    }

    private static CharSequence toTextValue(final Object value){
        return CharSequences.quoteIfNecessary(
            value.toString()
        );
    }

    // BoxEdge........................................................................................................

    @Override
    public Border border(final BoxEdge edge) {
        return Border.with(
            edge,
            this.filter(
                n -> n.isBorder() && edge.isTextStyleProperty(n)
            )
        );
    }

    @Override
    public Margin margin(final BoxEdge edge) {
        return Margin.with(
            edge,
            this.filter(
                n -> n.isMargin() && edge.isTextStyleProperty(n)
            )
        );
    }

    @Override
    public Padding padding(final BoxEdge edge) {
        return Padding.with(
            edge,
            this.filter(
                n -> n.isPadding() && edge.isTextStyleProperty(n)
            )
        );
    }

    @Override
    TextStyle filter(final Predicate<TextStylePropertyName<?>> filter) {
        final TextStylePropertiesMap copy = this.copy();

        for (final Entry<TextStylePropertyName<?>, Object> propertyAndValue : copy.entrySet()) {
            if (false == filter.test(propertyAndValue.getKey())) {
                copy.removeTextStyleProperty(
                    propertyAndValue.getKey()
                );
            }
        }

        return this.setValues(copy);
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
            final JsonNode value = propertyName.handler.marshall(
                Cast.to(
                    propertyAndValue.getValue()
                ),
                context
            );

            json.add(
                value.setName(
                    propertyName.jsonPropertyName
                )
            );
        }

        return JsonNode.object()
            .setChildren(json);
    }
}
