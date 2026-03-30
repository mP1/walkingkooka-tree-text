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

import walkingkooka.collect.map.Maps;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A {@link TextStyle} with style and values.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class TextStyleEmpty extends TextStyle {

    /**
     * Singleton necessary to avoid race conditions to a init'd static field
     */
    static TextStyleEmpty instance() {
        if (null == instance) {
            instance = new TextStyleEmpty();
        }
        return instance;
    }

    private static TextStyleEmpty instance;

    /**
     * Private ctor
     */
    private TextStyleEmpty() {
        super();
    }

    /**
     * Always returns true
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    // Value............................................................................................................

    @Override
    public Map<TextStylePropertyName<?>, Object> value() {
        return Maps.empty();
    }

    @Override
    TextStylePropertiesMap copy() {
        return TextStylePropertiesMap.empty();
    }

    // merge............................................................................................................

    @Override
    TextStyle mergeNonNull(final TextStyle other) {
        return other; // EMPTY merge other gives other.
    }

    @Override
    TextStyle mergeNonEmpty(final TextStyleNonEmpty textStyle) {
        return textStyle; // EMPTY merge NOTEMPTY -> NOTEMPTY
    }

    // replace..........................................................................................................

    @Override
    TextNode replaceNonNull(final TextNode textNode) {
        return textNode.setAttributesEmpty();
    }

    // setChildren......................................................................................................

    @Override
    TextStylePropertiesMap textStylePropertiesMap() {
        return TextStylePropertiesMap.EMPTY;
    }

    // get/set/remove...................................................................................................

    @Override //
    <V> Optional<V> getNonNull(final TextStylePropertyName<V> propertyName) {
        return Optional.empty();
    }

    @Override
    <T> TextStyleNonEmpty setValue(final TextStylePropertyName<T> propertyName,
                                   final T value) {
        final TextStylePropertiesMap values = this.copy();
        values.setTextStyleProperty(propertyName, value);

        return TextStyleNonEmpty.with(values);
    }

    @Override
    TextStyle removeNonNull(final TextStylePropertyName<?> propertyName) {
        return this; // empty nothing to remove
    }

    // TextStyleVisitor.................................................................................................

    @Override
    void accept(final TextStyleVisitor visitor) {
        // no properties
    }

    // text.............................................................................................................

    /**
     * Returns an empty string, no names and values.
     */
    @Override
    public String text() {
        return "";
    }

    // BoxEdge........................................................................................................

    @Override
    public Border border(final BoxEdge boxEdge) {
        Objects.requireNonNull(boxEdge, "boxEdge");
        return boxEdge.emptyBorder;
    }

    @Override
    public Margin margin(final BoxEdge boxEdge) {
        Objects.requireNonNull(boxEdge, "boxEdge");
        return boxEdge.emptyMargin;
    }

    @Override
    public Padding padding(final BoxEdge boxEdge) {
        Objects.requireNonNull(boxEdge, "boxEdge");
        return boxEdge.emptyPadding;
    }

    @Override
    TextStyle filter(final Predicate<TextStylePropertyName<?>> filter) {
        return this;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        // nop
    }

    // Object..........................................................................................................

    @Override
    public String toString() {
        return "";
    }

    // JsonNodeContext..................................................................................................

    @Override
    JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.object();
    }

    // toText(Function).................................................................................................

    @Override
    String toText(final Function<TextStylePropertyName<?>, String> propertyNameMapper) {
        return "";
    }
}
