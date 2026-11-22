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

import walkingkooka.collect.list.CsvStringList;
import walkingkooka.collect.list.HasCsvStringListTsvStringList;
import walkingkooka.collect.list.ImmutableListDefaults;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.list.StringLikeList;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link List} that holds non-null {@link TextStyle textStyle}.
 */
public final class TextStyleList extends AbstractList<TextStyle>
    implements ImmutableListDefaults<TextStyleList, TextStyle>,
    HasCsvStringListTsvStringList {

    public static TextStyleList fromStringLikeList(final StringLikeList stringLikeList) {
        Objects.requireNonNull(stringLikeList, "stringLikeList");

        return EMPTY.setElements(
            stringLikeList.stream()
                .map(TextStyle::parse)
                .collect(Collectors.toList())
        );
    }

    public final static TextStyleList EMPTY = new TextStyleList(Lists.empty());

    // @VisibleForTesting
    TextStyleList(final List<TextStyle> list) {
        super();
        this.list = list;
    }

    @Override
    public TextStyle get(final int index) {
        return this.list.get(index);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public TextStyleList setElements(final Collection<TextStyle> collection) {
        final TextStyleList currencyList;

        if (collection instanceof TextStyleList) {
            currencyList = (TextStyleList) collection;
        } else {
            Objects.requireNonNull(collection, "collection");

            final List<TextStyle> copy = Lists.array();
            for (final TextStyle textStyle : collection) {
                this.elementCheck(textStyle);

                copy.add(textStyle);
            }

            if (copy.equals(this)) {
                currencyList = this;
            } else {
                currencyList = copy.isEmpty() ?
                    EMPTY :
                    new TextStyleList(copy);
            }
        }

        return currencyList;
    }

    @Override
    public void elementCheck(final TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle");
    }

    private final List<TextStyle> list;

    // HasCsvString.....................................................................................................

    /**
     * Returns a {@link CsvStringList}, with each value a single {@link TextStyle} after converting to CSV.
     */
    @Override
    public CsvStringList csvStringList() {
        return CsvStringList.makeCsvStringList(
            this.list,
            TextStyle::text
        );
    }

    // json.............................................................................................................

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(TextStyleList.class),
            TextStyleList::unmarshall,
            TextStyleList::marshall,
            TextStyleList.class
        );
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return context.marshall(this.csvStringList());
    }

    static TextStyleList unmarshall(final JsonNode node,
                                    final JsonNodeUnmarshallContext context) {
        return TextStyleList.fromStringLikeList(
            context.unmarshall(
                node,
                CsvStringList.class
            )
        );
    }
}
