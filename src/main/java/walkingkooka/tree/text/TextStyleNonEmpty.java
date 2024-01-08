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
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
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
     * Factory that creates a {@link TextStyleNonEmpty} from a {@link TextNodeMap}.
     */
    static TextStyleNonEmpty withNonEmpty(final TextNodeMap value) {
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
    TextStyle setValues0(final Map<TextStylePropertyName<?>, Object> values) {
        final Map<TextStylePropertyName<?>, Object> newValuesMap = Maps.ordered();
        final Map<TextStylePropertyName<?>, Object> oldValuesMap = this.value();
        newValuesMap.putAll(oldValuesMap);
        newValuesMap.putAll(values);

        return oldValuesMap.equals(newValuesMap) ?
                this:
                TextStyleNonEmpty.with(
                    TextNodeMap.with(newValuesMap)
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

    @Override
    <V> Optional<V> get0(final TextStylePropertyName<V> propertyName) {
        return Optional.ofNullable(Cast.to(this.value.get(propertyName)));
    }

    // set..............................................................................................................

    @Override
    <V> TextStyle set0(final TextStylePropertyName<V> propertyName, final V value) {
        TextNodeMap map = this.value;
        final List<Entry<TextStylePropertyName<?>, Object>> list = Lists.array();

        int mode = 0; // new property added.

        for (Entry<TextStylePropertyName<?>, Object> propertyAndValue : map.entries) {
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
                new TextStyleNonEmpty(TextNodeMap.withTextStyleMapEntrySet(TextNodeMapEntrySet.withList(list)));
    }

    // remove...........................................................................................................

    @Override
    TextStyle remove0(final TextStylePropertyName<?> propertyName) {
        final List<Entry<TextStylePropertyName<?>, Object>> list = Lists.array();
        boolean removed = false;

        for (Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.value.entries) {
            final TextStylePropertyName<?> property = propertyAndValue.getKey();
            if (propertyName.equals(property)) {
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
