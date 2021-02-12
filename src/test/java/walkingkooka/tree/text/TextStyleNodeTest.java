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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleNodeTest extends TextParentNodeTestCase<TextStyleNode> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> TextStyleNode.with(null, TextStyleNode.NO_ATTRIBUTES_MAP));
    }

    @Test
    public void testWithOneChildNoAttributes() {
        final TextNode child = TextNode.text("child1");
        assertSame(child, TextStyleNode.with(Lists.of(child), TextStyleNode.NO_ATTRIBUTES_MAP));
    }

    @Test
    public void testWithOneChildAndAttributes() {
        final TextNode child = TextNode.text("child");
        final TextNode styleNode = TextStyleNode.with(Lists.of(child), TextNodeMap.with(Maps.of(TextStylePropertyName.WIDTH, Length.pixel(1.0))));
        this.childCountCheck(styleNode, child);
    }

    @Test
    public void testWithSeveralChildrenNoAttributes() {
        final TextNode child1 = TextNode.text("child1");
        final TextNode child2 = TextNode.text("child2");
        final TextNode styleNode = TextStyleNode.with(Lists.of(child1, child2), TextStyleNode.NO_ATTRIBUTES_MAP);
        this.childCountCheck(styleNode, child1, child2);
    }

    @Test
    public void testWithChildrenDefensiveCopy() {
        final TextNode child1 = TextNode.text("child1");
        final TextNode child2 = TextNode.text("child2");

        final List<TextNode> children = Lists.array();
        children.add(child1);
        children.add(child2);

        final TextNode styleNode = TextStyleNode.with(children, TextStyleNode.NO_ATTRIBUTES_MAP);
        children.clear();
        this.childCountCheck(styleNode, child1, child2);
    }

    @Test
    public void testWithParent() {
        final TextNode child1 = this.text1();
        final TextNode child2 = this.text2();
        final TextNode child3 = this.text3();

        final TextStyleNode parent = textStyleNode(child1, child2);
        final TextStyleNode grandParent = textStyleNode(parent, child3);

        final TextNode parent2 = grandParent.children().get(0);
        this.parentPresentCheck(parent2);
        this.childCountCheck(parent2, child1, child2);

        final TextNode grandParent2 = parent2.parentOrFail();
        this.childCountCheck(grandParent2, parent, child3);
        this.parentMissingCheck(grandParent2);
    }

    @Test
    public void testSetDifferentChildren() {
        final TextStyleNode node = this.createTextNode();
        final List<TextNode> children = Lists.of(different());

        final TextStyleNode different = node.setChildren(children);
        assertNotSame(different, node);

        this.childCountCheck(different, different());
        this.childCountCheck(node, text1(), text2());
    }

    @Test
    public void testSetDifferentChildrenWithParent() {
        final TextNode child1 = this.text1();
        final TextNode child2 = this.text2();

        final TextStyleNode parent = textStyleNode(child1, child2);

        final TextNode parent2 = this.text3();
        final TextStyleNode grandParent = textStyleNode(parent, parent2);

        final TextNode differentChild = TextNode.text("different");
        final TextNode different = grandParent.children().get(0).appendChild(differentChild);
        this.parentPresentCheck(different);
        this.childCountCheck(different, child1, child2, differentChild);

        final TextNode grandParent2 = different.parentOrFail();
        this.childCountCheck(grandParent2, different, parent2);
        this.parentMissingCheck(grandParent2);
    }

    @Test
    public void testSetNoChildren() {
        final TextStyleNode node = this.createTextNode();
        final List<TextNode> children = TextNode.NO_CHILDREN;

        final TextStyleNode different = node.setChildren(children);
        assertNotSame(different, node);
        this.childCountCheck(different);

        this.childCountCheck(node, text1(), text2());
    }

    @Test
    public void testSetDifferentAttributes() {
        final TextStyleNode node = this.createTextNode();
        final Map<TextStylePropertyName<?>, Object> attributes = Maps.of(TextStylePropertyName.with("abc"), "xyz");

        final TextNode different = node.setAttributes(attributes);
        assertNotSame(different, node);
        checkAttributes(different, attributes);

        checkAttributes(node, TextStyleNode.NO_ATTRIBUTES);
    }

    @Test
    public void testSetDifferentAttributesTwice() {
        final TextStyleNode node = this.createTextNode();
        final Map<TextStylePropertyName<?>, Object> attributes = Maps.of(TextStylePropertyName.with("abc"), "xyz");

        final TextNode different = node.setAttributes(attributes);
        assertNotSame(different, node);

        final Map<TextStylePropertyName<?>, Object> attributes2 = Maps.of(TextStylePropertyName.with("def"), "qrs");
        final TextNode different2 = different.setAttributes(attributes2);
        assertNotSame(different, different2);

        checkAttributes(different, attributes);
        checkAttributes(different2, attributes2);
        checkAttributes(node, TextStyleNode.NO_ATTRIBUTES);
    }

    @Test
    public void testSetDifferentChildrenDifferentAttributes() {
        final TextStyleNode node = this.createTextNode();
        final List<TextNode> children = Lists.of(different());

        final TextStyleNode different = node.setChildren(children);
        assertNotSame(different, node);
        this.childCountCheck(different, different());

        final Map<TextStylePropertyName<?>, Object> attributes = Maps.of(TextStylePropertyName.with("abc"), "xyz");

        final TextNode different2 = node.setAttributes(attributes);
        assertNotSame(different2, different);
        checkAttributes(different2, attributes);

        this.childCountCheck(node, text1(), text2());
        checkAttributes(node, TextStyleNode.NO_ATTRIBUTES);
    }

    private static void checkAttributes(final TextNode node, final Map<TextStylePropertyName<?>, Object> attributes) {
        assertEquals(attributes, node.attributes(), "attributes");
    }

    @Test
    public void testSetAttributesAndTextStyle() {
        final Map<TextStylePropertyName<?>, Object> style = Maps.sorted();
        style.put(TextStylePropertyName.BORDER_RIGHT_COLOR, Color.parseRgb("blue"));
        style.put(TextStylePropertyName.COLOR, Color.parseRgb("lime"));
        style.put(TextStylePropertyName.TEXT_ALIGN, TextAlign.RIGHT);

        final TextNode node = TextNode.style(TextNode.NO_CHILDREN)
                .setAttributes(style);
        assertEquals(TextStyle.with(style), node.textStyle(), "textStyle");
    }

    // HasText..........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(TextNode.style(Lists.of(Text.with("a1"), Text.with("b2"))),
                "a1b2");
    }

    // HasTextOffset .....................................................................................................

    @Test
    public void testTextOffsetWithParent() {
        this.textOffsetAndCheck(TextNode.style(Lists.of(Text.with("a1"),
                TextNode.style(Lists.of(Text.with("b22")))))
                        .children().get(1),
                2);
    }

    // HasJsonNode .....................................................................................................

    @Test
    public void testJsonNodeMarshallWithoutChildren() {
        this.marshallAndCheck(textStyleNode(), "{}");
    }

    @Test
    public void testJsonNodeMarshallWithChildren() {
        this.marshallAndCheck(textStyleNode(TextNode.text("text-1a"), TextNode.text("text-2b")),
                "{\"children\": [{\"type\": \"text\", \"value\": \"text-1a\"}, {\"type\": \"text\", \"value\": \"text-2b\"}]}");
    }

    @Test
    public void testJsonNodeMarshallWithChildren2() {
        this.marshallAndCheck(textStyleNode(TextNode.text("text123"), TextNode.text("text456")),
                "{\"children\": [{\"type\": \"text\", \"value\": \"text123\"}, {\"type\": \"text\", \"value\": \"text456\"}]}");
    }

    @Test
    public void testJsonNodeMarshallWithStyleNode() {
        this.marshallAndCheck(textStyleNode()
                        .setAttributes(Maps.of(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456))),
                "{\"styles\": {\"background-color\": \"#123456\"}}");
    }

    @Test
    public void testJsonNodeMarshallWithStyleNodeAndChildren() {
        this.marshallAndCheck(TextNode.text("text123")
                        .setAttributes(Maps.of(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456)))
                        .parentOrFail(),
                "{\"styles\": {\"background-color\": \"#123456\"}, \"children\": [{\"type\": \"text\", \"value\": \"text123\"}]}");
    }

    @Test
    public void testJsonNodeUnmarshallWithoutChildren() {
        this.unmarshallAndCheck("{}",
                textStyleNode());
    }

    @Test
    public void testJsonNodeUnmarshallWithChildren() {
        this.unmarshallAndCheck("{\"children\": [{\"type\": \"text\", \"value\": \"text-1a\"}, {\"type\": \"text\", \"value\": \"text-2b\"}]}",
                textStyleNode(TextNode.text("text-1a"), TextNode.text("text-2b")));
    }

    @Test
    public void testJsonRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(textStyleNode(TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                textStyleNode(TextNode.text("text3"), TextNode.text("text4"))));
    }

    @Test
    public void testJsonRoundtrip2() {
        this.marshallRoundTripTwiceAndCheck(textStyleNode(
                TextNode.text("text1"),
                textStyleNode(TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                        textStyleNode(TextNode.text("text3"), TextNode.text("text4")))));
    }

    @Test
    public void testJsonRoundtripWithStyleNode() {
        this.marshallRoundTripTwiceAndCheck(textStyleNode(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                textStyleNode(TextNode.text("text3"), TextNode.text("text4")))
                .setAttributes(Maps.of(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456))));
    }

    @Test
    public void testJsonRoundtripWithStyleNode2() {
        this.marshallRoundTripTwiceAndCheck(textStyleNode(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                TextNode.style(Lists.of(TextNode.text("text3"))))
                .setAttributes(Maps.of(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456), TextStylePropertyName.COLOR, Color.fromRgb(0x789abc))));
    }

    @Test
    public void testJsonRoundtripWithStyleNode3() {
        final Map<TextStylePropertyName<?>, Object> styleNode = Maps.ordered();
        styleNode.put(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456));
        styleNode.put(TextStylePropertyName.BORDER_COLLAPSE, BorderCollapse.SEPARATE);
        styleNode.put(TextStylePropertyName.BORDER_SPACING, BorderSpacing.with(Length.pixel(1.0)));
        styleNode.put(TextStylePropertyName.BORDER_BOTTOM_STYLE, BorderStyle.DASHED);
        styleNode.put(TextStylePropertyName.BORDER_LEFT_STYLE, BorderStyle.HIDDEN);
        styleNode.put(TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOTTED);
        styleNode.put(TextStylePropertyName.BORDER_TOP_STYLE, BorderStyle.OUTSET);
        styleNode.put(TextStylePropertyName.BORDER_BOTTOM_WIDTH, Length.pixel(1.0));
        styleNode.put(TextStylePropertyName.BORDER_LEFT_WIDTH, Length.pixel(2.0));
        styleNode.put(TextStylePropertyName.BORDER_RIGHT_WIDTH, Length.pixel(3.0));
        styleNode.put(TextStylePropertyName.BORDER_TOP_WIDTH, Length.pixel(4.0));
        styleNode.put(TextStylePropertyName.FONT_FAMILY, FontFamily.with("Antiqua"));
        styleNode.put(TextStylePropertyName.FONT_KERNING, FontKerning.NORMAL);
        styleNode.put(TextStylePropertyName.FONT_SIZE, FontSize.with(10));
        styleNode.put(TextStylePropertyName.FONT_STRETCH, FontStretch.CONDENSED);
        styleNode.put(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC);
        styleNode.put(TextStylePropertyName.FONT_VARIANT, FontVariant.SMALL_CAPS);
        styleNode.put(TextStylePropertyName.FONT_WEIGHT, FontWeight.with(1000));
        styleNode.put(TextStylePropertyName.HANGING_PUNCTUATION, HangingPunctuation.LAST);
        styleNode.put(TextStylePropertyName.HEIGHT, Length.pixel(99.5));
        styleNode.put(TextStylePropertyName.HYPHENS, Hyphens.AUTO);
        styleNode.put(TextStylePropertyName.LETTER_SPACING, LetterSpacing.with(Length.normal()));
        styleNode.put(TextStylePropertyName.LINE_HEIGHT, Length.pixel(100.0));
        styleNode.put(TextStylePropertyName.LIST_STYLE_POSITION, ListStylePosition.INSIDE);
        styleNode.put(TextStylePropertyName.LIST_STYLE_TYPE, ListStyleType.DECIMAL_LEADING_ZERO);
        styleNode.put(TextStylePropertyName.MARGIN_BOTTOM, Length.pixel(1.0));
        styleNode.put(TextStylePropertyName.MARGIN_LEFT, Length.pixel(2.0));
        styleNode.put(TextStylePropertyName.MARGIN_RIGHT, Length.pixel(3.0));
        styleNode.put(TextStylePropertyName.MARGIN_TOP, Length.pixel(4.0));
        styleNode.put(TextStylePropertyName.MAX_HEIGHT, Length.pixel(1024.0));
        styleNode.put(TextStylePropertyName.MAX_WIDTH, Length.none());
        styleNode.put(TextStylePropertyName.MIN_HEIGHT, Length.pixel(10.0));
        styleNode.put(TextStylePropertyName.MIN_WIDTH, Length.pixel(11.0));
        styleNode.put(TextStylePropertyName.OPACITY, Opacity.with(0.5));
        styleNode.put(TextStylePropertyName.OUTLINE_COLOR, Color.parseRgb("red"));
        styleNode.put(TextStylePropertyName.OUTLINE_OFFSET, Length.pixel(0.25));
        styleNode.put(TextStylePropertyName.OUTLINE_STYLE, OutlineStyle.HIDDEN);
        styleNode.put(TextStylePropertyName.OUTLINE_WIDTH, Length.pixel(0.5));
        styleNode.put(TextStylePropertyName.OVERFLOW_X, Overflow.AUTO);
        styleNode.put(TextStylePropertyName.OVERFLOW_Y, Overflow.AUTO);
        styleNode.put(TextStylePropertyName.PADDING_BOTTOM, Length.pixel(5.0));
        styleNode.put(TextStylePropertyName.PADDING_LEFT, Length.pixel(6.0));
        styleNode.put(TextStylePropertyName.PADDING_RIGHT, Length.pixel(7.0));
        styleNode.put(TextStylePropertyName.PADDING_TOP, Length.pixel(8.0));
        styleNode.put(TextStylePropertyName.TAB_SIZE, Length.number(12));
        styleNode.put(TextStylePropertyName.TEXT, "abc123");
        styleNode.put(TextStylePropertyName.TEXT_ALIGN, TextAlign.LEFT);
        styleNode.put(TextStylePropertyName.COLOR, Color.fromRgb(0x789abc));
        styleNode.put(TextStylePropertyName.TEXT_DECORATION, TextDecoration.UNDERLINE);
        styleNode.put(TextStylePropertyName.TEXT_DECORATION_COLOR, Color.fromRgb(0xabcdef));
        styleNode.put(TextStylePropertyName.TEXT_DECORATION_STYLE, TextDecorationStyle.DASHED);
        styleNode.put(TextStylePropertyName.TEXT_DIRECTION, TextDirection.LTR);
        styleNode.put(TextStylePropertyName.TEXT_INDENT, Length.pixel(40.0));
        styleNode.put(TextStylePropertyName.TEXT_JUSTIFY, TextJustify.INTER_CHARACTER);
        styleNode.put(TextStylePropertyName.TEXT_OVERFLOW, TextOverflow.string("abc123"));
        styleNode.put(TextStylePropertyName.TEXT_TRANSFORM, TextTransform.CAPITALIZE);
        styleNode.put(TextStylePropertyName.TEXT_WRAPPING, TextWrapping.OVERFLOW);
        styleNode.put(TextStylePropertyName.VERTICAL_ALIGN, VerticalAlign.BOTTOM);
        styleNode.put(TextStylePropertyName.VISIBILITY, Visibility.COLLAPSE);
        styleNode.put(TextStylePropertyName.WHITE_SPACE, TextWhitespace.PRE);
        styleNode.put(TextStylePropertyName.WIDTH, Length.pixel(320.0));
        styleNode.put(TextStylePropertyName.WORD_BREAK, WordBreak.BREAK_WORD);
        styleNode.put(TextStylePropertyName.WORD_SPACING, WordSpacing.with(Length.normal()));
        styleNode.put(TextStylePropertyName.WORD_WRAP, WordWrap.BREAK_WORD);
        styleNode.put(TextStylePropertyName.WRITING_MODE, WritingMode.VERTICAL_LR);

        this.marshallRoundTripTwiceAndCheck(textStyleNode(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                TextNode.style(Lists.of(TextNode.text("text3"))))
                .setAttributes(styleNode));
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrintable() {
        this.treePrintAndCheck(
                TextStyleNode.with(
                        Lists.of(
                                TextNode.text("a1"),
                                TextNode.text("b2")
                        ),
                        TextNodeMap.with(
                                Maps.of(
                                        TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456),
                                        TextStylePropertyName.BORDER_COLLAPSE, BorderCollapse.SEPARATE,
                                        TextStylePropertyName.BORDER_SPACING, BorderSpacing.with(Length.pixel(1.0)),
                                        TextStylePropertyName.BORDER_BOTTOM_STYLE, BorderStyle.DASHED
                                )
                        )
                ),
                "Style\n" +
                        "  TextStyle\n" +
                        "    background-color=#123456 (walkingkooka.color.OpaqueRgbColor)\n" +
                        "    border-bottom-style=DASHED (walkingkooka.tree.text.BorderStyle)\n" +
                        "    border-collapse=SEPARATE (walkingkooka.tree.text.BorderCollapse)\n" +
                        "    border-spacing=1px (walkingkooka.tree.text.BorderSpacing)\n" +
                        "  Text \"a1\"\n" +
                        "  Text \"b2\"\n"
        );
    }

    // Visitor .........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<TextNode> visited = Lists.array();

        final TextStyleNode styleNode = textStyleNode(TextNode.text("a1"), TextNode.text("b2"));
        final Text text1 = Cast.to(styleNode.children().get(0));
        final Text text2 = Cast.to(styleNode.children().get(1));

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
            protected Visiting startVisit(final TextStyleNode t) {
                assertSame(styleNode, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextStyleNode t) {
                assertSame(styleNode, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final Text t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(styleNode);
        assertEquals("1517217262", b.toString());
        assertEquals(Lists.of(styleNode, styleNode,
                text1, text1, text1,
                text2, text2, text2,
                styleNode, styleNode),
                visited,
                "visited");
    }

    // toString........................................................................................................

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(textStyleNode(), "[]");
    }

    @Test
    public void testToStringWithChildren() {
        this.toStringAndCheck(textStyleNode(text1(), text2()), "[\"text-1a\", \"text-2b\"]");
    }

    @Test
    public void testToStringWithAttributesWithoutChildren() {
        this.toStringAndCheck(textStyleNode().setAttributes(Maps.of(TextStylePropertyName.with("abc"), "123")), "{abc: \"123\"}[]");
    }

    @Test
    public void testToStringWithAttributes2() {
        this.toStringAndCheck(textStyleNode().setAttributes(Maps.of(TextStylePropertyName.with("abc"), "123", TextStylePropertyName.with("def"), "456")),
                "{abc: \"123\", def: \"456\"}[]");
    }

    @Test
    public void testToStringWithChildrenAndAttributes() {
        this.toStringAndCheck(text1()
                        .setAttributes(Maps.of(TextStylePropertyName.with("style-1"), "value-1"))
                        .parentOrFail(),
                "{style-1: \"value-1\"}[\"text-1a\"]");
    }

    @Test
    public void testToStringWithStyleNodeWithChildren() {
        this.toStringAndCheck(textStyleNode(text1(), textStyleNode(text2(), text3())), "[\"text-1a\", [\"text-2b\", \"text-3c\"]]");
    }

    @Override
    TextStyleNode createTextNode() {
        return textStyleNode(text1(), text2());
    }

    private static TextStyleNode textStyleNode(final TextNode... children) {
        return textStyleNode(Lists.of(children));
    }

    private static TextStyleNode textStyleNode(final List<TextNode> children) {
        assertNotEquals(1, children.size(), () -> "children must not be 1=" + children);
        return Cast.to(TextStyleNode.with(children, TextStyleNode.NO_ATTRIBUTES_MAP));
    }

    @Override
    Class<TextStyleNode> textNodeType() {
        return TextStyleNode.class;
    }

    // JsonNodeMarshallingTesting........................................................................................

    @Override
    public final TextStyleNode unmarshall(final JsonNode from,
                                          final JsonNodeUnmarshallContext context) {
        return TextStyleNode.unmarshallTextStyleNode(from, context);
    }
}
