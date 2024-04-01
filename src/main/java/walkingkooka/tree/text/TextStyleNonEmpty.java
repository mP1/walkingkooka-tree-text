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
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
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
     * Factory that creates a {@link TextStyleNonEmpty} from a {@link TextNodeMap}.
     */
    static TextStyleNonEmpty with(final TextNodeMap value) {
        return new TextStyleNonEmpty(value);
    }

    private TextStyleNonEmpty(final TextNodeMap value) {
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
        Map<TextStylePropertyName<?>, Object> copy = Maps.sorted();
        copy.putAll(this.value);
        return copy;
    }

    @Override
    TextStyle setValuesWithCopy(final Map<TextStylePropertyName<?>, Object> values) {
        final Map<TextStylePropertyName<?>, Object> oldValuesMap = this.value();

        return oldValuesMap.equals(values) ?
                this:
                TextStyleNonEmpty.with(
                    TextNodeMap.with(values)
                );
    }

    final TextNodeMap value;

    // merge............................................................................................................

    @Override
    TextStyle merge0(final TextStyle textStyle) {
        return textStyle.merge1(this);
    }

    @Override
    TextStyle merge1(final TextStyleNonEmpty textStyle) {
        final Map<TextStylePropertyName<?>, Object> otherBefore = this.value; // because of double dispatch params are reversed.
        final Map<TextStylePropertyName<?>, Object> before = textStyle.value;


        final Map<TextStylePropertyName<?>, Object> merged = Maps.sorted();
        merged.putAll(otherBefore);
        merged.putAll(before);

        return merged.equals(otherBefore) ?
                this :
                merged.equals(before) ?
                        textStyle :
                        new TextStyleNonEmpty(TextNodeMap.with(merged));
    }

    // replace..........................................................................................................

    @Override
    TextNode replace0(final TextNode textNode) {
        return textNode.setAttributesNonEmptyTextStyleMap(this.value);
    }

    // setChildren......................................................................................................

    @Override
    TextNodeMap textStyleMap() {
        return this.value;
    }

    // get..............................................................................................................

    @Override //
    <V> Optional<V> get0(final TextStylePropertyName<V> propertyName) {
        Object get = null;

        final TextNodeMap value = this.value;

        switch (propertyName.name) {
            case "border-color":
                // only return a non null color if all colors are the same.
                final Color top = (Color) value.get(TextStylePropertyName.BORDER_TOP_COLOR);
                if (null != top) {
                    final Color left = (Color) value.get(TextStylePropertyName.BORDER_LEFT_COLOR);
                    if (top.equals(left)) {
                        final Color right = (Color) value.get(TextStylePropertyName.BORDER_RIGHT_COLOR);
                        if (top.equals(right)) {
                            final Color bottom = (Color) value.get(TextStylePropertyName.BORDER_BOTTOM_COLOR);
                            if (top.equals(bottom)) {
                                get = top;
                            }
                        }
                    }
                }
                break;
            case "border-style":
                // only return a non null style if all styles are the same.
                final BorderStyle topBorderStyle = (BorderStyle) value.get(TextStylePropertyName.BORDER_TOP_STYLE);
                if (null != topBorderStyle) {
                    if (topBorderStyle.equals(value.get(TextStylePropertyName.BORDER_LEFT_STYLE))) {
                        if (topBorderStyle.equals(value.get(TextStylePropertyName.BORDER_RIGHT_STYLE))) {
                            if (topBorderStyle.equals(value.get(TextStylePropertyName.BORDER_BOTTOM_STYLE))) {
                                get = topBorderStyle;
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
    <V> TextStyle set0(final TextStylePropertyName<V> propertyName,
                       final V value) {
        TextStyle result;

        switch (propertyName.name) {
            case "border-color":
                final Color color = Cast.to(value);

                result = this.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_TOP_COLOR,
                                color,
                                TextStylePropertyName.BORDER_LEFT_COLOR,
                                color,
                                TextStylePropertyName.BORDER_RIGHT_COLOR,
                                color,
                                TextStylePropertyName.BORDER_BOTTOM_COLOR,
                                color
                        )
                );
                break;
            case "border-style":
                final BorderStyle borderStyle = Cast.to(value);

                result = this.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_TOP_STYLE,
                                borderStyle,
                                TextStylePropertyName.BORDER_LEFT_STYLE,
                                borderStyle,
                                TextStylePropertyName.BORDER_RIGHT_STYLE,
                                borderStyle,
                                TextStylePropertyName.BORDER_BOTTOM_STYLE,
                                borderStyle
                        )
                );
                break;
            default:
                result = this.set1(
                        propertyName,
                        value
                );
                break;
        }

        return result;
    }

    private <V> TextStyleNonEmpty set1(final TextStylePropertyName<V> propertyName,
                                       final V value) {
        TextNodeMap map = this.value;
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
            TextNodeMapEntrySet.sort(list);
        }

        return 1 == mode ?
                this :
                new TextStyleNonEmpty(
                        TextNodeMap.withTextStyleMapEntrySet(
                                TextNodeMapEntrySet.withList(list)
                        )
                );
    }

    // remove...........................................................................................................

    @Override
    TextStyle remove0(final TextStylePropertyName<?> propertyName) {
        final List<Entry<TextStylePropertyName<?>, Object>> list = Lists.array();
        boolean removed = false;

        Predicate<TextStylePropertyName<?>> removeIf;

        switch(propertyName.name) {
            case "border-color":
                removeIf = BORDER_COLOR;
                break;
            case "border-style":
                removeIf = BORDER_STYLE;
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
                this.remove1(list) :
                this;
    }

    /**
     * Used to remove any of the 4 BORDER_XXX_COLOR properties.
     */
    private static final Predicate<TextStylePropertyName<?>> BORDER_COLOR = Predicates.setContains(
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
    private static final Predicate<TextStylePropertyName<?>> BORDER_STYLE = Predicates.setContains(
            Sets.of(
                    TextStylePropertyName.BORDER_TOP_STYLE,
                    TextStylePropertyName.BORDER_LEFT_STYLE,
                    TextStylePropertyName.BORDER_RIGHT_STYLE,
                    TextStylePropertyName.BORDER_BOTTOM_STYLE
            )
    );

    /**
     * Accepts a list after removing a property, special casing if the list is empty.
     */
    private TextStyle remove1(List<Entry<TextStylePropertyName<?>, Object>> list) {
        return list.isEmpty() ?
                TextStyle.EMPTY :
                new TextStyleNonEmpty(TextNodeMap.withTextStyleMapEntrySet(TextNodeMapEntrySet.withList(list))); // no need to sort after a delete
    }

    // TextStyleVisitor.................................................................................................

    @Override
    void accept(final TextStyleVisitor visitor) {
        this.value.accept(visitor);
    }

    // css.............................................................................................................

    @Override
    public String css() {
        return this.value.css();
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
    public int hashCode() {
        return this.value().hashCode();
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof TextStyleNonEmpty;
    }

    @Override
    boolean equals0(final TextStyle other) {
        return this.value.equals(other.value());
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    // JsonNodeContext..................................................................................................

    /**
     * Creates a json-object where the properties are strings, and the value without types.
     */
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
