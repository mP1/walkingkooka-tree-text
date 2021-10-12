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
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.patch.Patchable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link TextStyle} holds a {@link Map} of {@link TextStylePropertyName} and values.
 */
public abstract class TextStyle implements Value<Map<TextStylePropertyName<?>, Object>>,
        Patchable<TextStyle>,
        TreePrintable {

    /**
     * A {@link TextStyle} with no styling.
     */
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public final static TextStyle EMPTY = TextStyleEmpty.instance();

    /**
     * Factory that creates a {@link TextStyle} from a {@link Map}.
     */
    public static TextStyle with(final Map<TextStylePropertyName<?>, Object> value) {
        return withTextStyleMap(TextNodeMap.with(value));
    }

    static TextStyle withTextStyleMap(final TextNodeMap map) {
        return map.isEmpty() ?
                EMPTY :
                TextStyleNonEmpty.withNonEmpty(map);
    }

    /**
     * Private ctor to limit sub classes.
     */
    TextStyle() {
        super();
    }

    /**
     * Returns true if the {@link TextStyle} is empty.
     */
    public abstract boolean isEmpty();

    // setChildren......................................................................................................

    /**
     * Factory that returns a {@link TextStyleNode} with the given {@link TextNode}
     * and these styles.
     */
    public final TextNode setChildren(final List<TextNode> textNodes) {
        return TextStyleNode.with(textNodes, this.textStyleMap());
    }

    abstract TextNodeMap textStyleMap();

    // merge............................................................................................................

    /**
     * Merges the two {@link TextStyle}, with the value from this having priority when both have the same
     * {@link TextStylePropertyName}.
     */
    public final TextStyle merge(final TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle");

        return this.merge0(textStyle);
    }

    abstract TextStyle merge0(final TextStyle textStyle);

    abstract TextStyle merge1(final TextStyleNonEmpty textStyle);

    // replace............................................................................................................

    /**
     * If empty returns the given {@link TextNode} otherwise wraps inside a {@link TextStyleNode}
     */
    public final TextNode replace(final TextNode textNode) {
        Objects.requireNonNull(textNode, "textNode");

        return this.replace0(textNode);
    }

    abstract TextNode replace0(final TextNode textNode);

    // get..............................................................................................................

    /**
     * Gets the value for the given {@link TextStylePropertyName}.
     */
    public final <V> Optional<V> get(final TextStylePropertyName<V> propertyName) {
        checkPropertyName(propertyName);

        return this.get0(propertyName);
    }

    abstract <V> Optional<V> get0(final TextStylePropertyName<V> propertyName);

    /**
     * Gets the given property or throws an {@link TextStylePropertyValueException}
     */
    public final <V> V getOrFail(final TextStylePropertyName<V> propertyName) {
        return this.get(propertyName).orElseThrow(() -> new TextStylePropertyValueException("Missing " + propertyName));
    }

    // set..............................................................................................................

    /**
     * Sets a possibly new property returning a {@link TextStyle} with the new definition which may or may not
     * require creating a new {@link TextStyle}.
     */
    public final <V> TextStyle set(final TextStylePropertyName<V> propertyName, final V value) {
        checkPropertyName(propertyName);

        propertyName.check(value);
        return this.set0(propertyName, value);
    }

    abstract <V> TextStyle set0(final TextStylePropertyName<V> propertyName, final V value);

    // remove...........................................................................................................

    /**
     * Removes a possibly existing property returning a {@link TextStyle} without.
     */
    public final TextStyle remove(final TextStylePropertyName<?> propertyName) {
        checkPropertyName(propertyName);

        return this.remove0(propertyName);
    }

    abstract TextStyle remove0(final TextStylePropertyName<?> propertyName);

    private static void checkPropertyName(final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");
    }

    // setOrRemove......................................................................................................

    /**
     * Does a set or remove if the value is null.
     */
    public final <V> TextStyle setOrRemove(final TextStylePropertyName<V> propertyName, final V value) {
        return null != value ?
                this.set(propertyName, value) :
                this.remove(propertyName);
    }
    // TextStyleVisitor.................................................................................................

    abstract void accept(final TextStyleVisitor visitor);

    // BoxEdge........................................................................................................

    abstract Border border(final BoxEdge edge);

    abstract Margin margin(final BoxEdge edge);

    abstract Padding padding(final BoxEdge edge);

    // Object...........................................................................................................

    @Override
    abstract public int hashCode();

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEquals(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEquals(final Object other);

    abstract boolean equals0(final TextStyle other);

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

        return with(properties);
    }

    abstract JsonNode marshall(final JsonNodeMarshallContext context);

    static {
        TextStylePropertyName.BACKGROUND_COLOR.toString();

        JsonNodeContext.register("text-style",
                TextStyle::unmarshall,
                TextStyle::marshall,
                TextStyle.class,
                TextStyleNonEmpty.class,
                TextStyleEmpty.class);
    }

    // Patchable........................................................................................................

    /**
     * Performs a patch on this {@link TextStyle} returning the result. Null values will result in the property being removed.
     */
    public TextStyle patch(final JsonNode patch,
                           final JsonNodeUnmarshallContext context) {
        Objects.requireNonNull(patch, "patch");
        Objects.requireNonNull(context, "context");

        TextStyle result = this;

        for (final JsonNode nameAndValue : patch.objectOrFail().children()) {
            final TextStylePropertyName<?> name = TextStylePropertyName.unmarshall(nameAndValue);
            result = result.setOrRemove(
                    name,
                    Cast.to(
                            name.handler.unmarshall(nameAndValue, name, context)
                    )
            );
        }

        return result;
    }
}
