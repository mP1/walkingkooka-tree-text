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
import walkingkooka.color.Color;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.Printer;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A read only sorted view of attributes or text style to values that appear within a {@link TextStyleNode}.
 */
final class TextNodeMap extends AbstractMap<TextStylePropertyName<?>, Object> {

    static {
        Maps.registerImmutableType(TextNodeMap.class);
    }

    /**
     * An empty {@link TextNodeMap}.
     */
    static final TextNodeMap EMPTY = new TextNodeMap(TextNodeMapEntrySet.EMPTY);

    /**
     * Factory that takes a copy if the given {@link Map} is not a {@link TextNodeMap}.
     */
    static TextNodeMap with(final Map<TextStylePropertyName<?>, Object> map) {
        Objects.requireNonNull(map, "map");

        return map instanceof TextNodeMap ?
                (TextNodeMap) map :
                with0(map);
    }

    private static TextNodeMap with0(final Map<TextStylePropertyName<?>, Object> map) {
        return with1(TextNodeMapEntrySet.with(map));
    }

    private static TextNodeMap with1(final TextNodeMapEntrySet entrySet) {
        return entrySet.isEmpty() ?
                EMPTY :
                withTextStyleMapEntrySet(entrySet);
    }

    static TextNodeMap withTextStyleMapEntrySet(final TextNodeMapEntrySet entrySet) {
        return new TextNodeMap(entrySet);
    }

    private TextNodeMap(final TextNodeMapEntrySet entries) {
        super();
        this.entries = entries;
    }

    @Override
    public Set<Entry<TextStylePropertyName<?>, Object>> entrySet() {
        return this.entries;
    }

    final TextNodeMapEntrySet entries;

    // TextStyleVisitor.................................................................................................

    void accept(final TextStyleVisitor visitor) {
        this.entries.accept(visitor);
    }

    // css..............................................................................................................

    String css() {
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

            for (final Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.entrySet()) {
                css.print(separator);

                final TextStylePropertyName<?> propertyName = propertyAndValue.getKey();
                css.print(propertyName.value());
                css.print(": ");

                final Object value = propertyAndValue.getValue();
                final CharSequence valueCss = valueToCss(value);

                css.print(valueCss);
                css.print(";");

                separator = " ";
            }
        }

        return cssStringBuilder.toString();
    }

    private static CharSequence valueToCss(final Object value) {
        final CharSequence css;

        if (value instanceof Color) {
            final Color color = (Color) value;
            css = color.toCss();
        } else {
            if (value instanceof HasCss) {
                final HasCss hasCss = (HasCss) value;
                css = hasCss.css();
            } else {
                if (value instanceof Enum) {
                    css = HasCss.cssFromEnumName(
                            (Enum<?>) value
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
        }

        return css;
    }

    // JsonNodeContext..................................................................................................

    static TextNodeMap unmarshall(final JsonNode json,
                                  final JsonNodeUnmarshallContext context) {
        return TextNodeMap.with1(
                TextNodeMapEntrySet.unmarshall(
                        json,
                        context
                )
        );
    }

    JsonNode toJson(final JsonNodeMarshallContext context) {
        return this.entries.toJson(context);
    }
}
