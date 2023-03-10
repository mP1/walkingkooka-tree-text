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
import walkingkooka.text.CharSequences;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
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
        final StringBuilder cssStringBuilder = new StringBuilder();

        try (final IndentingPrinter css = Printers.stringBuilder(cssStringBuilder, LineEnding.SYSTEM).indenting(Indentation.SPACES2)) {
            css.println("{");
            css.indent();

            for (final Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.entrySet()) {
                final TextStylePropertyName<?> propertyName = propertyAndValue.getKey();
                css.print(propertyName.value());
                css.print(": ");

                final Object value = propertyAndValue.getValue();
                final CharSequence valueCss;
                if (value instanceof Enum) {
                    valueCss = HasCss.cssFromEnumName(
                            (Enum<?>) value
                    );
                } else {
                    final String stringValue = value.toString();
                    if (stringValue.indexOf(' ') >= 0) {
                        valueCss = CharSequences.quoteAndEscape(stringValue);
                    } else {
                        valueCss = stringValue;
                    }
                }

                css.print(valueCss);
                css.println(";");
            }

            css.outdent();
            css.println("}");
        }

        return cssStringBuilder.toString();
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
