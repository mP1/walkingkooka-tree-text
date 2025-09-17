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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BadgeTest extends TextParentNodeTestCase<Badge> {

    private final static String BADGE_TEXT = "Hello Badge Text 123";

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> Badge.with(null)
        );
    }

    // setChildren......................................................................................................

    @Test
    public void testSetChildrenWithMoreThanOneChildFails() {
        final Badge node = this.createTextNode();

        assertThrows(
            IllegalArgumentException.class,
            () -> node.setChildren(
                Lists.of(
                    this.text1(),
                    this.text2()
                )
            )
        );
    }

    @Test
    public void testSetChildrenDifferent() {
        final Badge node = this.createTextNode();
        final List<TextNode> children = Lists.of(
            TextNode.text("Different")
        );

        final Badge different = node.setChildren(children);
        assertNotSame(different, node);

        this.childCountCheck(
            different,
            children.get(0)
        );
        this.childCountCheck(
            node,
            text1()
        );
    }

    @Test
    public void testSetChildrenDifferentWithParent() {
        final TextNode child1 = this.text1();

        final TextNode parent = child1.setTextStyle(
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.BLACK
            )
        );

        final Badge grandParent = Badge.with(BADGE_TEXT)
            .setChildren(
                Lists.of(parent)
            );

        final TextNode differentChild = TextNode.text("different");
        final TextNode different = grandParent.children()
            .get(0)
            .appendChild(differentChild);
        this.parentPresentCheck(different);
        this.childCountCheck(
            different,
            child1, differentChild
        );

        final TextNode grandParent2 = different.parentOrFail();
        this.childCountCheck(
            grandParent2,
            different
        );
        this.parentMissingCheck(grandParent2);
    }

    @Test
    public void testSetChildrenEmpty() {
        final Badge node = Badge.with(BADGE_TEXT)
            .setChildren(
                Lists.of(
                    text1()
                )
            );
        final List<TextNode> children = TextNode.NO_CHILDREN;

        final Badge different = node.setChildren(children);
        assertNotSame(different, node);
        this.childCountCheck(different);

        this.childCountCheck(
            node,
            text1()
        );
    }

    @Test
    @Override
    public void testSetChildrenSame() {
        final Text child = this.text1();
        final List<TextNode> children = Lists.of(child);

        final Badge badge = this.createTextNode()
            .setChildren(children);

        Assertions.assertSame(
            badge,
            badge.setChildren(children)
        );
    }

    @Test
    @Override
    public void testSetDifferentChildren() {
        this.setChildrenAndCheck(
            this.createTextNode(),
            this.text2()
        );
    }

    // replaceChild.....................................................................................................

    @Test
    @Override
    public void testReplaceChild() {
        final Badge badge = Badge.with(BADGE_TEXT)
            .appendChild(this.text1());
        final Text replaced = this.text2();

        this.replaceAndCheck(
            badge.children()
                .get(0),
            replaced,
            Badge.with(BADGE_TEXT)
                .appendChild(replaced)
                .children()
                .get(0)
        );
    }

    // SetStyle.........................................................................................................

    @Test
    public void testSetTextStyleFails() {
        final Badge badge = Badge.with(BADGE_TEXT);
        final TextStyle style = TextStyle.parse("color: red");

        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> badge.setTextStyle(style)
        );

        this.checkEquals(
            "Badges cannot have a parent",
            thrown.getMessage()
        );
    }

    // merge............................................................................................................

    @Test
    public void testMergeWithEmpty() {
        final Badge badge = this.createStyleable();

        assertSame(
            badge,
            badge.merge(TextStyle.EMPTY)
        );
    }

    // SetText..........................................................................................................

    @Test
    public void testSetTextWithEmpty() {
        final Badge badge = Badge.with(BADGE_TEXT)
            .appendChild(
                TextNode.text("lost")
            );
        final String text = "";

        this.setTextAndCheck(
            badge,
            text,
            badge.setChildren(
                TextNode.NO_CHILDREN
            )
        );
    }

    @Test
    public void testSetTextWithNotEmpty() {
        final Badge badge = Badge.with(BADGE_TEXT);
        final String text = "Text123";

        this.setTextAndCheck(
            badge,
            text,
            badge.setChildren(
                Lists.of(
                    TextNode.text(text)
                )
            )
        );
    }

    // HasText..........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(
            Badge.with(BADGE_TEXT)
                .setChildren(
                    Lists.of(
                        Text.with("a1")
                    )
                ),
            "a1"
        );
    }

    // appendChild......................................................................................................

    @Test
    @Override
    public void testAppendChild() {
        final Badge badge = Badge.with(BADGE_TEXT);

        final Text text = TextNode.text("child");

        this.appendChildAndCheck(
            badge,
            text
        );
    }

    @Override
    public void testAppendChild2() {
        throw new UnsupportedOperationException();
    }

    // removeChild......................................................................................................

    @Test
    @Override
    public void testRemoveChildFirst() {
        final Text text = TextNode.text("child");
        final Badge badge = Badge.with(BADGE_TEXT)
            .appendChild(text);

        this.removeChildAndCheck(
            badge,
            badge.children()
                .get(0)
        );
    }

    @Override
    public void testRemoveChildLast() {
        throw new UnsupportedOperationException();
    }

    // toHtml...........................................................................................................

    @Test
    public void testToHtmlWithText() {
        this.toHtmlAndCheck(
            Badge.with(BADGE_TEXT)
                .setChildren(
                    Lists.of(
                        TextNode.text("body-text")
                    )
                ),
            "body-text"
        );
    }

    @Override
    Badge createTextNode() {
        return Badge.with(
            BADGE_TEXT,
            Lists.of(
                text1()
            )
        );
    }

    @Override
    Class<Badge> textNodeType() {
        return Badge.class;
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrintable() {
        this.treePrintAndCheck(
            Badge.with(
                BADGE_TEXT
            ).setChildren(
                Lists.of(
                    TextNode.text("a1")
                )
            ),
            "Badge\n" +
                "  badgeText\n" +
                "    \"Hello Badge Text 123\"\n" +
                "  Text \"a1\"\n"
        );
    }

    // Visitor .........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<TextNode> visited = Lists.array();

        final Badge badge = Badge.with(BADGE_TEXT)
            .setChildren(
                Lists.of(
                    TextNode.text("text-a1")
                )
            );
        final Text text1 = Cast.to(
            badge.children()
                .get(0)
        );

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
            protected Visiting startVisit(final Badge t) {
                assertSame(badge, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Badge t) {
                assertSame(badge, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final Text t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(badge);
        this.checkEquals("1517262", b.toString());
        this.checkEquals(
            Lists.of(
                badge,
                badge,
                text1,
                text1,
                text1,
                badge,
                badge
            ),
            visited,
            "visited"
        );
    }

    // toString........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            Badge.with(BADGE_TEXT),
            "\"Hello Badge Text 123\" []"
        );
    }

    @Test
    public void testToStringWithChildren() {
        this.toStringAndCheck(
            Badge.with(BADGE_TEXT)
                .setChildren(
                    Lists.of(
                        text1()
                    )
                ),
            "\"Hello Badge Text 123\" [\"text-1a\"]"
        );
    }

    // JSON.............................................................................................................

    @Test
    public void testMarshallWithoutChildren() {
        this.marshallAndCheck(
            Badge.with(BADGE_TEXT),
            "{\n" +
                "  \"badgeText\": \"Hello Badge Text 123\"\n" +
                "}"
        );
    }

    @Test
    public void testMarshallWithChildren() {
        this.marshallAndCheck(
            Badge.with(BADGE_TEXT)
                .setChildren(
                    Lists.of(
                        TextNode.text("text-1a")
                    )
                ),
            "{\n" +
                "  \"badgeText\": \"Hello Badge Text 123\",\n" +
                "  \"children\": [\n" +
                "    {\n" +
                "      \"type\": \"text\",\n" +
                "      \"value\": \"text-1a\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            "{\n" +
                "  \"badgeText\": \"Hello Badge Text 123\"\n" +
                "}",
            Badge.with(BADGE_TEXT)
        );
    }

    @Test
    public void testUnmarshallWithChildren() {
        this.unmarshallAndCheck(
            "{\n" +
                "  \"badgeText\": \"Hello Badge Text 123\",\n" +
                "  \"children\": [\n" +
                "    {\n" +
                "      \"type\": \"text\",\n" +
                "      \"value\": \"text-1a\"\n" +
                "    }\n" +
                "  ]\n" +
                "}",
            Badge.with(BADGE_TEXT)
                .setChildren(
                    Lists.of(
                        TextNode.text("text-1a")
                    )
                )
        );
    }

    @Test
    public void testJsonRoundtripNoChildren() {
        this.marshallRoundTripTwiceAndCheck(
            Badge.with(BADGE_TEXT)
        );
    }

    @Test
    public void testJsonRoundtripWithChildren() {
        this.marshallRoundTripTwiceAndCheck(
            Badge.with(BADGE_TEXT)
                .setChildren(
                    Lists.of(
                        TextNode.text("text-1A")
                            .setAttributes(
                                Maps.of(
                                    TextStylePropertyName.BACKGROUND_COLOR,
                                    Color.parse("#111")
                                )
                            )
                    )
                )
        );
    }

    @Override
    public Badge unmarshall(final JsonNode json,
                            final JsonNodeUnmarshallContext context) {
        return Badge.unmarshallBadge(
            json,
            context
        );
    }

    // TypeNaming.......................................................................................................

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
