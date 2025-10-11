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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.util.Map;
import java.util.Optional;

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
    Map<TextStylePropertyName<?>, Object> valuesMutableCopy() {
        return Maps.sorted();
    }

    @Override
    TextStyle setValuesWithCopy(final Map<TextStylePropertyName<?>, Object> values) {
        return TextStyleNonEmpty.with(
            TextStylePropertiesMap.with(values)
        );
    }

    // merge............................................................................................................

    @Override
    TextStyle merge0(final TextStyle other) {
        return other;
    }

    @Override
    TextStyle merge1(final TextStyleNonEmpty textStyle) {
        return textStyle; // EMPTY merge NOTEMPTY -> NOTEMPTY
    }

    // replace..........................................................................................................

    @Override
    TextNode replaceNonNull(final TextNode textNode) {
        return textNode.setAttributesEmpty();
    }

    // setChildren......................................................................................................

    @Override
    TextStylePropertiesMap textStyleMap() {
        return TextStylePropertiesMap.EMPTY;
    }

    // get/set/remove...................................................................................................

    @Override <V> Optional<V> getNonNull(final TextStylePropertyName<V> propertyName) {
        return Optional.empty();
    }

    @Override
    <T> TextStyleNonEmpty setTopLeftRightBottom(final TextStylePropertyName<T> top,
                                                final TextStylePropertyName<T> left,
                                                final TextStylePropertyName<T> right,
                                                final TextStylePropertyName<T> bottom,
                                                final T value) {
        return TextStyleNonEmpty.with(
            TextStylePropertiesMap.withTextStyleMapEntrySet(
                TextStylePropertiesMapEntrySet.withList(
                    Lists.of(
                        Maps.entry(
                            top,
                            value
                        ),
                        Maps.entry(
                            left,
                            value
                        ),
                        Maps.entry(
                            right,
                            value
                        ),
                        Maps.entry(
                            bottom,
                            value
                        )
                    )
                )
            )
        );
    }

    @Override
    <T> TextStyleNonEmpty setValue(final TextStylePropertyName<T> propertyName,
                                   final T value) {
        return TextStyleNonEmpty.with(
            TextStylePropertiesMap.withTextStyleMapEntrySet(
                TextStylePropertiesMapEntrySet.withList(
                    Lists.of(
                        Map.entry(
                            propertyName,
                            value
                        )
                    )
                )
            )
        );
    }

    @Override
    TextStyle removeNonNull(final TextStylePropertyName<?> propertyName) {
        return this;
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
    Border border(final BoxEdge edge) {
        return edge.emptyBorder;
    }

    @Override
    Margin margin(final BoxEdge edge) {
        return edge.emptyMargin;
    }

    @Override
    Padding padding(final BoxEdge edge) {
        return edge.emptyPadding;
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
}
