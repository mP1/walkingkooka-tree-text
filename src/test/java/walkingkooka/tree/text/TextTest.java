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

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextTest extends TextLeafNodeTestCase<Text, String> {

    // with.............................................................................................................

    @Test
    public void testWithEmpty() {
        assertSame(
            TextNode.EMPTY_TEXT,
            Text.with("")
        );
    }

    @Test
    public void testWith2() {
        final String value = "abc123";
        this.checkText(
            Text.with(value),
            value
        );
    }

    @Test
    public void testSetTextSame() {
        final String value = "abc123";
        final Text text = Text.with(value);

        assertSame(
            text,
            text.setText(value)
        );
    }

    @Test
    public void testSetTextDifferent() {
        final String before = "abc123";
        final Text text = Text.with(before);

        final String value = "different";
        final Text different = text.setText(value);
        assertNotSame(text, different);

        this.checkText(
            different,
            value
        );
        this.checkText(
            text,
            before
        );
    }

    @Test
    public void testSetTextDifferentWithParent() {
        final TextNode parent1 = Text.style(Lists.of(Text.with("text-abc-123"), Text.with("text-def456")));
        final Text text1 = parent1.children().get(0).cast();
        final Text text2 = parent1.children().get(1).cast();

        final String value = "different-text-789";
        final Text different = text1.setText(value);

        this.checkText(
            different,
            value
        );
        this.childCountCheck(
            different.parentOrFail(),
            different,
            text2
        );
    }

    private void checkText(final Text text,
                           final String value) {
        this.textAndCheck(
            text,
            value
        );
        this.textLengthAndCheck(
            text,
            value
        );
        this.checkEquals(
            value,
            text.value(),
            "value"
        );
    }

    // IsXXXMethod .....................................................................................................

    @Test
    public void testIsMethods() {
        final Text text = Text.with("abc");

        this.checkEquals(
            true,
            text.isText(),
            "isText"
        );
        this.checkEquals(
            false,
            text.isPlaceholder(),
            "isPlaceholder"
        );
        this.checkEquals(
            false,
            text.isStyle(),
            "isStyle"
        );
        this.checkEquals(
            false,
            text.isStyleName(),
            "isStyleName"
        );
    }

    // HasTextOffset ...................................................................................................

    @Test
    public void testTextOffsetWithParent() {
        this.textOffsetAndCheck(
            TextNode.style(
                    Lists.of(
                        Text.with("a1"),
                        Text.with("b22")
                    )
                ).children()
                .get(1),
            2
        );
    }

    // toHtml...........................................................................................................

    @Test
    public void testToHtml() {
        final String text = "abc123";

        this.toHtmlAndCheck(
            Text.with(text),
            text
        );
    }

    @Test
    public void testToHtmlAmpersand() {
        this.toHtmlAndCheck(
            Text.with("&"),
            "&amp;"
        );
    }

    @Test
    public void testToHtmlSpace() {
        this.toHtmlAndCheck(
            Text.with(" "),
            "&nbsp;"
        );
    }

    @Test
    public void testToHtmlEscaped() {
        final String text = "abc 123<>&\"'";

        this.toHtmlAndCheck(
            Text.with(text),
            "abc&nbsp;123&lt;&gt;&amp;&amp;quot;'"
        );
    }

    // Visitor .........................................................................................................

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
            protected void visit(final Text n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        this.checkEquals("132", b.toString());
    }

    @Override
    Text createTextNode(final String value) {
        return Text.with(value);
    }

    @Override
    String value() {
        return "abc123";
    }

    @Override
    Class<Text> textNodeType() {
        return Text.class;
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(Text.with("ABC123"));
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            Text.with("abc\t123\n"),
            "Text \"abc\\t123\\n\"\n"
        );
    }

    // ToString ........................................................................................................

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(Text.with(""), "\"\"");
    }

    @Test
    public void testToStringNotEmpty() {
        this.toStringAndCheck(Text.with("abc"), "\"abc\"");
    }

    @Test
    public void testToStringNotEmptyEscaping() {
        this.toStringAndCheck(Text.with("abc\tdef"), "\"abc\\tdef\"");
    }

    // JsonNodeMarshallingTesting.......................................................................................

    @Test
    public void testMarshall() {
        final String text = "abc123!\t";
        this.marshallAndCheck(Text.with(text), JsonNode.string(text));
    }

    @Test
    public void testUnmarshall() {
        final String text = "abc123!\t";
        this.unmarshallAndCheck(JsonNode.string(text), Text.with(text));
    }

    @Override
    public Text unmarshall(final JsonNode from,
                           final JsonNodeUnmarshallContext context) {
        return Text.unmarshallText(from, context);
    }

    // class............................................................................................................

    @Override
    public void testTypeNaming() {
    }
}
