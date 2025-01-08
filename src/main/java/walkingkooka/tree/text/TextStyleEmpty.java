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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
            TextNodeMap.with(values)
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
    TextNode replace0(final TextNode textNode) {
        return textNode.setAttributesEmptyTextStyleMap();
    }

    // setChildren......................................................................................................

    @Override
    TextNodeMap textStyleMap() {
        return TextNodeMap.EMPTY;
    }

    // get/set/remove...................................................................................................

    @Override <V> Optional<V> get0(final TextStylePropertyName<V> propertyName) {
        return Optional.empty();
    }

    @Override //
    <V> TextStyle set0(final TextStylePropertyName<V> propertyName,
                       final V value) {
        List<Entry<TextStylePropertyName<?>, Object>> entries;

        switch (propertyName.name) {
            case BORDER_COLOR:
                entries = Lists.of(
                    Maps.entry(
                        TextStylePropertyName.BORDER_TOP_COLOR,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.BORDER_LEFT_COLOR,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.BORDER_RIGHT_COLOR,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.BORDER_BOTTOM_COLOR,
                        value
                    )
                );
                break;
            case BORDER_STYLE:
                entries = Lists.of(
                    Maps.entry(
                        TextStylePropertyName.BORDER_TOP_STYLE,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.BORDER_LEFT_STYLE,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.BORDER_RIGHT_STYLE,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.BORDER_BOTTOM_STYLE,
                        value
                    )
                );
                break;
            case BORDER_WIDTH:
                entries = Lists.of(
                    Maps.entry(
                        TextStylePropertyName.BORDER_TOP_WIDTH,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.BORDER_LEFT_WIDTH,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.BORDER_RIGHT_WIDTH,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                        value
                    )
                );
                break;
            case MARGIN:
                entries = Lists.of(
                    Maps.entry(
                        TextStylePropertyName.MARGIN_TOP,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.MARGIN_LEFT,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.MARGIN_RIGHT,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.MARGIN_BOTTOM,
                        value
                    )
                );
                break;
            case PADDING:
                entries = Lists.of(
                    Maps.entry(
                        TextStylePropertyName.PADDING_TOP,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.PADDING_LEFT,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.PADDING_RIGHT,
                        value
                    ),
                    Maps.entry(
                        TextStylePropertyName.PADDING_BOTTOM,
                        value
                    )
                );
                break;
            default:
                entries = Lists.of(
                    Maps.entry(
                        propertyName,
                        value
                    )
                );
                break;
        }

        return TextStyleNonEmpty.with(
            TextNodeMap.withTextStyleMapEntrySet(
                TextNodeMapEntrySet.withList(entries)
            )
        );
    }

    @Override
    TextStyle remove0(final TextStylePropertyName<?> propertyName) {
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
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override boolean canBeEquals(final Object other) {
        return other instanceof TextStyleEmpty;
    }

    @Override
    boolean equals0(final TextStyle other) {
        return true; // singleton
    }

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
