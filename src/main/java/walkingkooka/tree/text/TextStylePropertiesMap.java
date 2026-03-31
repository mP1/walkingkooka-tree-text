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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for both the immutable and mutable Maps used by {@link TextStyleNonEmpty}, backed by an array,
 * using {@link TextStylePropertyName#index()} to locate the slot and set/get/remove the value.
 */
final class TextStylePropertiesMap extends AbstractMap<TextStylePropertyName<?>, Object> implements CanBeEmpty {

    /**
     * An empty immutable {@link TextStylePropertiesMap}.
     */
    final static TextStylePropertiesMap EMPTY = TextStylePropertiesMap.empty();

    static TextStylePropertiesMap empty() {
        return new TextStylePropertiesMap();
    }

    static TextStylePropertiesMap with(final Map<TextStylePropertyName<?>, Object> map) {
        final TextStylePropertiesMap textStylePropertiesMap;

        if (map instanceof TextStylePropertiesMap) {
            textStylePropertiesMap = (TextStylePropertiesMap) map;
        } else {
            textStylePropertiesMap = empty();

            for (final Entry<TextStylePropertyName<?>, Object> entry : map.entrySet()) {
                textStylePropertiesMap.setTextStyleProperty(
                    entry.getKey(),
                    entry.getValue()
                );
            }
        }

        return textStylePropertiesMap;
    }

    TextStylePropertiesMap() {
        super();
        this.values = new Object[TextStylePropertyName.NAMES.length];
        this.size = 0;
    }

    TextStylePropertiesMap copy() {
        final TextStylePropertiesMap copy = empty();
        System.arraycopy(
            this.values,
            0,
            copy.values,
            0,
            this.values.length
        );
        copy.size = this.size;
        return copy;
    }

    void removeBorder() {
        this.removeTextStyleProperty(TextStylePropertyName.BORDER_TOP_COLOR);
        this.removeTextStyleProperty(TextStylePropertyName.BORDER_TOP_STYLE);
        this.removeTextStyleProperty(TextStylePropertyName.BORDER_TOP_WIDTH);

        this.removeTextStyleProperty(TextStylePropertyName.BORDER_RIGHT_COLOR);
        this.removeTextStyleProperty(TextStylePropertyName.BORDER_RIGHT_STYLE);
        this.removeTextStyleProperty(TextStylePropertyName.BORDER_RIGHT_WIDTH);

        this.removeTextStyleProperty(TextStylePropertyName.BORDER_BOTTOM_COLOR);
        this.removeTextStyleProperty(TextStylePropertyName.BORDER_BOTTOM_STYLE);
        this.removeTextStyleProperty(TextStylePropertyName.BORDER_BOTTOM_WIDTH);

        this.removeTextStyleProperty(TextStylePropertyName.BORDER_LEFT_COLOR);
        this.removeTextStyleProperty(TextStylePropertyName.BORDER_LEFT_STYLE);
        this.removeTextStyleProperty(TextStylePropertyName.BORDER_LEFT_WIDTH);
    }

    void removeMargin() {
        this.removeTextStyleProperty(TextStylePropertyName.MARGIN_TOP);
        this.removeTextStyleProperty(TextStylePropertyName.MARGIN_RIGHT);
        this.removeTextStyleProperty(TextStylePropertyName.MARGIN_BOTTOM);
        this.removeTextStyleProperty(TextStylePropertyName.MARGIN_LEFT);
    }

    void removePadding() {
        this.removeTextStyleProperty(TextStylePropertyName.PADDING_TOP);
        this.removeTextStyleProperty(TextStylePropertyName.PADDING_RIGHT);
        this.removeTextStyleProperty(TextStylePropertyName.PADDING_BOTTOM);
        this.removeTextStyleProperty(TextStylePropertyName.PADDING_LEFT);
    }

    <T> T removeTextStyleProperty(final TextStylePropertyName<T> name) {
        return this.setTextStyleProperty(
            name,
            null
        );
    }

    void setBorder(final Border border) {
        final BoxEdge borderBoxEdge = border.edge();

        for (final BoxEdge boxEdge : BoxEdge.values()) {
            if (BoxEdge.ALL == boxEdge) {
                continue;
            }

            if (boxEdge == borderBoxEdge || BoxEdge.ALL == borderBoxEdge) {
                this.setBorderMarginPadding(
                    border,
                    boxEdge.borderColorPropertyName()
                );
                this.setBorderMarginPadding(
                    border,
                    boxEdge.borderStylePropertyName()
                );
                this.setBorderMarginPadding(
                    border,
                    boxEdge.borderWidthPropertyName()
                );
            }
        }
    }

    <T> T setBorderMarginPadding(final BorderMarginPadding borderMarginPadding,
                                 final TextStylePropertyName<T> name) {
        return this.setTextStyleProperty(
            name,
            borderMarginPadding.getProperty(name)
                .orElse(null)
        );
    }

    <T> T setTextStyleProperty(final TextStylePropertyName<T> name,
                               final Object value) {
        final int index = name.index();

        if (null != value) {
            name.checkValue(value);
        }

        Object[] values = this.values;

        final T previous = (T) values[index];
        values[index] = value;

        if (null == previous) {
            if (null != value) {
                this.size++;
            }
        } else {
            if (null == value) {
                this.size--;
            }
        }

        return previous;
    }

    // Map..............................................................................................................

    @Override
    public int size() {
        return this.size;
    }

    private int size;

    @Override
    public Object get(final Object name) {
        return name instanceof TextStylePropertyName ?
            this.getTextStylePropertyName(
                (TextStylePropertyName<?>) name
            ) :
            null;
    }

    private Object getTextStylePropertyName(final TextStylePropertyName<?> name) {
        final int index = name.index();
        return -1 == index ?
            null :
            this.values[index];
    }

    final Object[] values;

    @Override
    public Set<Entry<TextStylePropertyName<?>, Object>> entrySet() {
        if (null == this.entries) {
            this.entries = TextStylePropertiesMapEntrySet.with(this);
        }
        return this.entries;
    }

    private TextStylePropertiesMapEntrySet entries;

    // TextStyleVisitor.................................................................................................

    void accept(final TextStyleVisitor visitor) {
        int index = 0;

        for (Object value : this.values) {
            if (null != value) {
                visitor.acceptPropertyAndValue(
                    TextStylePropertyName.NAMES[index],
                    Cast.to(value)
                );
            }

            index++;
        }
    }

    // json.............................................................................................................

    static {
        Maps.registerImmutableType(TextStylePropertiesMap.class);
    }

    /**
     * Recreates this {@link TextStylePropertiesMapEntrySet} from the json object.
     */
    static TextStylePropertiesMap unmarshall(final JsonNode json,
                                             final JsonNodeUnmarshallContext context) {
        final TextStylePropertiesMap map = TextStylePropertiesMap.empty();

        for (final JsonNode child : json.children()) {
            final TextStylePropertyName<?> name = TextStylePropertyName.unmarshall(child);

            map.setTextStyleProperty(
                name,
                Cast.to(
                    name.handler.unmarshall(
                        child,
                        name,
                        context
                    )
                )
            );
        }

        return map.isEmpty() ?
            EMPTY :
            map;
    }

    /**
     * Creates a json object using the keys and values from the entries in this {@link Map}.
     */
    JsonNode marshall(final JsonNodeMarshallContext context) {
        final List<JsonNode> json = Lists.array();

        for (final Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.entrySet()) {
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

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(TextStylePropertiesMap.class),
            TextStylePropertiesMap::unmarshall,
            TextStylePropertiesMap::marshall,
            TextStylePropertiesMap.class
        );
    }
}
