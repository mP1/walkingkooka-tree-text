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
import walkingkooka.naming.Name;
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
        final TextStylePropertiesMapEntrySet before = other.value.entries;
        final TextStylePropertiesMapEntrySet after = this.value.entries;

        final Object[] beforeValues = before.values;
        final Object[] afterValues = after.values;

        final int beforeSize = beforeValues.length;
        final int afterSize = afterValues.length;
        final int newSize = Math.max(
            beforeSize,
            afterSize
        );

        final Object[] newValues = new Object[newSize];

        // copy before
        System.arraycopy(
            beforeValues,
            0,
            newValues,
            0,
            beforeSize
        );

        boolean different = false;
        int newCount = before.count;

        for (int i = 0; i < afterSize; i++) {
            final Object afterValue = afterValues[i];
            if(null == afterValue) {
                continue;
            }

            if(i < beforeSize) {
                final Object beforeValue = newValues[i];
                if (afterValue.equals(beforeValue)) {
                    continue;
                }
                if(null != beforeValue) {
                    newCount--;
                }
            }

            different = true;
            newValues[i] = afterValue;
            newCount++;
        }

        return different ?
            TextStyleNonEmpty.with(
                TextStylePropertiesMap.withTextStyleMapEntrySet(
                    TextStylePropertiesMapEntrySet.with(
                        newValues,
                        newCount
                    )
                )
            ) :
            this;
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

    @Override
    <T> TextStyle setTopLeftRightBottom(final TextStylePropertyName<T> topName,
                                        final TextStylePropertyName<T> leftName,
                                        final TextStylePropertyName<T> rightName,
                                        final TextStylePropertyName<T> bottomName,
                                        final T value) {
        return this.setValue(
            topName,
            value
        ).setValue(
            leftName,
            value
        ).setValue(
            rightName,
            value
        ).setValue(
            bottomName,
            value
        );
    }

    @Override
    <V> TextStyleNonEmpty setValue(final TextStylePropertyName<V> propertyName,
                                   final V value) {
        final TextStyleNonEmpty set;

        final TextStylePropertiesMap map = this.value;
        final Object previousValue = map.getValue(propertyName);

        if (value.equals(previousValue)) {
            set = this;
        } else {
            final TextStylePropertiesMapEntrySet entries = map.entries;

            final int index = propertyName.index();
            final int oldSize = entries.values.length;
            final int newSize = Math.max(
                index + 1,
                oldSize
            );

            final Object[] oldValues = entries.values;

            final Object[] newValues = new Object[newSize];
            System.arraycopy(
                oldValues,
                0,
                newValues,
                0,
                oldSize
            );
            newValues[index] = value;

            set = TextStyleNonEmpty.with(
                TextStylePropertiesMap.withTextStyleMapEntrySet(
                    TextStylePropertiesMapEntrySet.with(
                        newValues,
                        entries.count +
                            (
                                null == previousValue ?
                                1 :
                                0
                            )
                    )
                )
            );
        }

        return set;
    }

    // remove...........................................................................................................

    @Override
    TextStyle removeNonNull(final TextStylePropertyName<?> propertyName) {
        final TextStyle removed;

        switch (propertyName.name) {
            case BORDER_COLOR:
                removed = this.removeValues(
                    TextStylePropertyName.BORDER_TOP_COLOR,
                    TextStylePropertyName.BORDER_LEFT_COLOR,
                    TextStylePropertyName.BORDER_RIGHT_COLOR,
                    TextStylePropertyName.BORDER_BOTTOM_COLOR
                );
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
                removed = this.removeValues(
                    TextStylePropertyName.MARGIN_TOP,
                    TextStylePropertyName.MARGIN_LEFT,
                    TextStylePropertyName.MARGIN_RIGHT,
                    TextStylePropertyName.MARGIN_BOTTOM
                );
                break;
            case PADDING:
                removed = this.removeValues(
                    TextStylePropertyName.PADDING_TOP,
                    TextStylePropertyName.PADDING_LEFT,
                    TextStylePropertyName.PADDING_RIGHT,
                    TextStylePropertyName.PADDING_BOTTOM
                );
                break;
            default:
                final int index = propertyName.index();

                final TextStylePropertiesMapEntrySet entries = this.value.entries;
                final Object[] oldValues = entries.values;
                final int oldArraySize = oldValues.length;

                // remove index outside values, never existed
                if(index >= oldArraySize) {
                    removed = this;
                } else {
                    final Object previousValue = oldValues[index];

                    // value was already null, nothing to remove
                    if(null == previousValue) {
                        removed = this;
                    } else {
                        final int count = entries.count - 1;

                        // removed only value return EMPTY
                        if(0 == count) {
                            removed = TextStyle.EMPTY;
                        } else {
                            int newArraySize = oldArraySize;

                            // value being removed is the last value in array, need to compact
                            if(index == oldArraySize) {
                                // find previous non null value
                                while(null == oldValues[newArraySize]) {
                                    newArraySize--;
                                }
                            }

                            // copy old array into new
                            final Object[] newValues = new Object[newArraySize];
                            for(int i = 0; i < oldArraySize; i++) {
                                newValues[i] = oldValues[i];
                            }

                            // didnt remove last element of array, need to clear value in new array
                            if(index < newArraySize) {
                                newValues[index] = null;
                            }

                            removed = TextStyleNonEmpty.with(
                                TextStylePropertiesMap.withTextStyleMapEntrySet(
                                    TextStylePropertiesMapEntrySet.with(
                                        newValues,
                                        count
                                    )
                                )
                            );
                        }
                    }
                }
                break;
        }

        return removed;
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
            final JsonNode value = propertyName.handler.marshall(
                Cast.to(
                    propertyAndValue.getValue()
                ),
                context
            );

            json.add(value.setName(propertyName.marshallName()));
        }

        return JsonNode.object()
            .setChildren(json);
    }
}
