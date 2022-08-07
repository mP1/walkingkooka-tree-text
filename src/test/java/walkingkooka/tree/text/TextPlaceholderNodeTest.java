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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextPlaceholderNodeTest extends TextLeafNodeTestCase<TextPlaceholderNode, TextPlaceholderName> {

    // HasText ........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(this.createTextNode(), "");
    }

    // HasTextOffset ...................................................................................................

    @Test
    public void testTextOffsetWithParent() {
        this.textOffsetAndCheck(TextNode.style(Lists.of(Text.with("a1"), this.createTextNode()))
                        .children().get(1),
                2);
    }

    // HasJsonNode .....................................................................................................

    @Test
    public void testJsonNodeMarshall() {
        final String value = "abc123";
        this.marshallAndCheck(TextPlaceholderNode.with(TextPlaceholderName.with(value)), JsonNode.string(value));
    }

    @Test
    public void testUnmarshall() {
        final String value = "abc123";
        this.unmarshallAndCheck(JsonNode.string(value), TextPlaceholderNode.with(TextPlaceholderName.with(value)));
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                TextPlaceholderNode.with(TextPlaceholderName.with("placeholder1")),
                "Placeholder \"placeholder1\"\n"
        );
    }

    // equals ..........................................................................................................

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(TextPlaceholderNode.with(TextPlaceholderName.with("different-place")));
    }

    // Visitor ........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final TextNode node = this.createTextNode();

        new FakeTextNodeVisitor() {
            @Override
            protected Visiting startVisit(final TextNode n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextNode n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final TextPlaceholderNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        this.checkEquals("132", b.toString());
    }

    // ToString ........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createNode(), "place1");
    }

    @Override
    TextPlaceholderNode createTextNode(final TextPlaceholderName value) {
        return TextPlaceholderNode.with(value);
    }

    @Override
    TextPlaceholderName value() {
        return TextPlaceholderName.with("place1");
    }

    @Override
    Class<TextPlaceholderNode> textNodeType() {
        return TextPlaceholderNode.class;
    }

    // JsonNodeMarshallingTesting........................................................................................

    @Override
    public TextPlaceholderNode unmarshall(final JsonNode from,
                                          final JsonNodeUnmarshallContext context) {
        return TextPlaceholderNode.unmarshallTextPlaceholderNode(from, context);
    }
}
