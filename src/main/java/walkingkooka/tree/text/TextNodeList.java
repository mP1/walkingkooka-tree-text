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
import walkingkooka.collect.list.ImmutableListDefaults;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

/**
 * An immutable list of {@link TextNode}. This exists primarily to support marshalling/unmarshalling JSON which does
 * not support generic types.
 * <pre>
 * [
 *   {
 *     "type": "text",
 *     "value": "Hello"
 *   },
 *   {
 *     "type": "hyperlink",
 *     "value": {
 *       "url": "https://example.com/link123"
 *     }
 *   }
 * ]
 * </pre>
 */
public final class TextNodeList extends AbstractList<TextNode>
    implements ImmutableListDefaults<TextNodeList, TextNode> {

    public static TextNodeList with(final List<TextNode> nodes) {
        Objects.requireNonNull(nodes, "nodes");

        return nodes instanceof TextNodeList ?
            (TextNodeList) nodes :
            new TextNodeList(
                Lists.immutable(nodes)
            );
    }

    private TextNodeList(final List<TextNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public TextNode get(int index) {
        return this.nodes.get(index);
    }

    @Override
    public int size() {
        return this.nodes.size();
    }

    private final List<TextNode> nodes;

    @Override
    public void elementCheck(final TextNode node) {
        Objects.requireNonNull(node, "node");
    }

    @Override
    public TextNodeList setElements(final List<TextNode> nodes) {
        final TextNodeList copy = with(nodes);
        return this.equals(copy) ?
            this :
            copy;
    }

    // json.............................................................................................................

    static TextNodeList unmarshall(final JsonNode node,
                                   final JsonNodeUnmarshallContext context) {
        return with(
            Cast.to(
                context.unmarshallListWithType(node)
            )
        );
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return context.marshallCollectionWithType(this);
    }

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(TextNodeList.class),
            TextNodeList::unmarshall,
            TextNodeList::marshall,
            TextNodeList.class
        );
    }
}
