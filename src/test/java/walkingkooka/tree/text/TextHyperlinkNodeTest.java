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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.net.Url;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextHyperlinkNodeTest extends TextParentNodeTestCase<TextHyperlinkNode> {

    private final static Url URL = Url.parseAbsolute("https://example.com/Hello123");

    @Test
    public void testWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> TextHyperlinkNode.with(null)
        );
    }

    @Test
    public void testWithParent() {
        final TextNode child1 = this.text1();
        final TextNode child2 = this.text2();
        final TextNode child3 = this.text3();

        final TextHyperlinkNode parent = TextHyperlinkNode.with(URL)
                .setChildren(
                        Lists.of(
                                child1,
                                child2
                        )
                );
        final TextHyperlinkNode grandParent = TextHyperlinkNode.with(URL)
                .setChildren(
                        Lists.of(
                                parent,
                                child3
                        )
                );

        final TextNode parent2 = grandParent.children().get(0);
        this.parentPresentCheck(parent2);
        this.childCountCheck(parent2, child1, child2);

        final TextNode grandParent2 = parent2.parentOrFail();
        this.childCountCheck(grandParent2, parent, child3);
        this.parentMissingCheck(grandParent2);
    }

    // setChildren......................................................................................................

    @Test
    public void testSetChildrenDifferent() {
        final TextHyperlinkNode node = this.createTextNode();
        final List<TextNode> children = Lists.of(different());

        final TextHyperlinkNode different = node.setChildren(children);
        assertNotSame(different, node);

        this.childCountCheck(different, different());
        this.childCountCheck(node, text1(), text2());
    }

    @Test
    public void testSetChildrenDifferentWithParent() {
        final TextNode child1 = this.text1();
        final TextNode child2 = this.text2();

        final TextHyperlinkNode parent = TextHyperlinkNode.with(URL)
                .setChildren(
                        Lists.of(
                                child1,
                                child2
                        )
                );

        final TextNode parent2 = this.text3();
        final TextHyperlinkNode grandParent = TextHyperlinkNode.with(URL)
                .setChildren(
                        Lists.of(
                                parent,
                                parent2
                        )
                );

        final TextNode differentChild = TextNode.text("different");
        final TextNode different = grandParent.children()
                .get(0)
                .appendChild(differentChild);
        this.parentPresentCheck(different);
        this.childCountCheck(
                different,
                child1, child2, differentChild
        );

        final TextNode grandParent2 = different.parentOrFail();
        this.childCountCheck(grandParent2, different, parent2);
        this.parentMissingCheck(grandParent2);
    }

    @Test
    public void testSetChildrenEmpty() {
        final TextHyperlinkNode node = TextHyperlinkNode.with(URL)
                .setChildren(
                        Lists.of(
                                text1(),
                                text2()
                        )
                );
        final List<TextNode> children = TextNode.NO_CHILDREN;

        final TextHyperlinkNode different = node.setChildren(children);
        assertNotSame(different, node);
        this.childCountCheck(different);

        this.childCountCheck(node, text1(), text2());
    }

    // HasText..........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(
                TextNode.hyperlink(URL)
                        .setChildren(
                                Lists.of(
                                        Text.with("a1"),
                                        Text.with("b2")
                                )
                        ),
                "a1b2"
        );
    }

    // HasTextOffset .....................................................................................................

    @Test
    public void testTextOffsetWithParent() {
        this.textOffsetAndCheck(
                TextNode.hyperlink(URL)
                        .setChildren(
                                Lists.of(
                                        Text.with("a1"),
                                        Text.with("b2")
                                )
                        ).children()
                        .get(1),
                2
        );
    }

    // toHtml...........................................................................................................

    @Test
    public void testToHtmlWithText() {
        this.toHtmlAndCheck(
                TextHyperlinkNode.with(URL)
                        .setChildren(
                                Lists.of(
                                        TextNode.text("hyper-link-text")
                                )
                        ),
                "<A href=\"https://example.com/Hello123\">hyper-link-text</A>"
        );
    }

    @Override
    TextHyperlinkNode createTextNode() {
        return TextHyperlinkNode.with(
                URL,
                Lists.of(
                    text1(),
                    text2()
                )
        );
    }

    @Override
    Class<TextHyperlinkNode> textNodeType() {
        return TextHyperlinkNode.class;
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrintable() {
        this.treePrintAndCheck(
                TextHyperlinkNode.with(
                        URL
                ).setChildren(
                        Lists.of(
                                TextNode.text("a1"),
                                TextNode.text("b2")
                        )
                ),
                "TextHyperlinkNode\n" +
                        "  https://example.com/Hello123\n" +
                        "  Text \"a1\"\n" +
                        "  Text \"b2\"\n"
        );
    }

    // Visitor .........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<TextNode> visited = Lists.array();

        final TextHyperlinkNode hyperLink = TextHyperlinkNode.with(URL)
                .setChildren(
                        Lists.of(
                                TextNode.text("text-a1")
                        )
                );
        final Text text1 = Cast.to(hyperLink.children().get(0));

        new FakeTextNodeVisitor() {
            @Override
            protected Visiting startVisit(final TextNode n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextNode n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final TextHyperlinkNode t) {
                assertSame(hyperLink, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextHyperlinkNode t) {
                assertSame(hyperLink, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final Text t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(hyperLink);
        this.checkEquals("1517262", b.toString());
        this.checkEquals(
                Lists.of(
                        hyperLink,
                        hyperLink,
                        text1,
                        text1,
                        text1,
                        hyperLink,
                        hyperLink
                ),
                visited,
                "visited"
        );
    }

    // toString........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                TextHyperlinkNode.with(URL),
                "https://example.com/Hello123 []"
        );
    }

    @Test
    public void testToStringWithChildren() {
        this.toStringAndCheck(
                TextHyperlinkNode.with(URL)
                        .setChildren(
                                Lists.of(
                                        text1(),
                                        text2()
                                )
                        ),
                "https://example.com/Hello123 [\"text-1a\", \"text-2b\"]"
        );
    }

    // JSON.............................................................................................................

    @Test
    public void testMarshallWithoutChildren() {
        this.marshallAndCheck(
                TextHyperlinkNode.with(URL),
                "{\n" +
                        "  \"url\": {\n" +
                        "    \"type\": \"url\",\n" +
                        "    \"value\": \"https://example.com/Hello123\"\n" +
                        "  }\n" +
                        "}"
        );
    }

    @Test
    public void testMarshallWithChildren() {
        this.marshallAndCheck(
                TextHyperlinkNode.with(URL)
                        .setChildren(
                                Lists.of(
                                        TextNode.text("text-1a"),
                                        TextNode.text("text-2b")
                                )
                        ),
                "{\n" +
                        "  \"url\": {\n" +
                        "    \"type\": \"url\",\n" +
                        "    \"value\": \"https://example.com/Hello123\"\n" +
                        "  },\n" +
                        "  \"children\": [\n" +
                        "    {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"value\": \"text-1a\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"value\": \"text-2b\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
                "{\n" +
                        "  \"url\": {\n" +
                        "    \"type\": \"url\",\n" +
                        "    \"value\": \"https://example.com/Hello123\"\n" +
                        "  }\n" +
                        "}",
                TextHyperlinkNode.with(URL)
        );
    }

    @Test
    public void testUnmarshallWithChildren() {
        this.unmarshallAndCheck(
                "{\n" +
                        "  \"url\": {\n" +
                        "    \"type\": \"url\",\n" +
                        "    \"value\": \"https://example.com/Hello123\"\n" +
                        "  },\n" +
                        "  \"children\": [\n" +
                        "    {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"value\": \"text-1a\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"value\": \"text-2b\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}",
                TextHyperlinkNode.with(URL)
                        .setChildren(
                                Lists.of(
                                        TextNode.text("text-1a"),
                                        TextNode.text("text-2b")
                                )
                        )
        );
    }

    @Test
    public void testJsonRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(
                TextHyperlinkNode.with(URL)
        );
    }

    @Test
    public void testJsonRoundtripWithChildren() {
        this.marshallRoundTripTwiceAndCheck(
                TextHyperlinkNode.with(URL)
                        .setChildren(
                                Lists.of(
                                        TextNode.text("text-1a"),
                                        TextNode.text("text-2b")
                                                .setAttributes(
                                                        Maps.of(
                                                                TextStylePropertyName.BACKGROUND_COLOR,
                                                                Color.parse("#234")
                                                        )
                                                )
                                )
                        )
        );
    }

    @Override
    public TextHyperlinkNode unmarshall(final JsonNode from,
                                        final JsonNodeUnmarshallContext context) {
        return TextHyperlinkNode.unmarshallHyperLink(
                from,
                context
        );
    }
}
