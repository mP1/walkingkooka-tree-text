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
import walkingkooka.net.Url;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ImageTest extends TextLeafNodeTestCase<Image, Url> {

    @Test
    public void testWithNullUrlFails() {
        assertThrows(
            NullPointerException.class,
            () -> Image.with(null)
        );
    }

    // SetText..........................................................................................................

    @Test
    public void testSetTextWithEmpty() {
        this.setTextAndCheck(
            this.createTextNode(),
            ""
        );
    }

    @Test
    public void testSetTextWithNotEmptyFails() {
        assertThrows(
            UnsupportedOperationException.class,
            () -> this.createTextNode()
                .setText("NotEmptyFails")
        );
    }

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

    // toHtml...........................................................................................................

    @Test
    public void testToHtml() {
        this.toHtmlAndCheck(
            Image.with(Url.parse("https://example.com/image.png")),
            "<IMG src=\"https://example.com/image.png\"/>"
        );
    }

    @Override
    public void testPropertiesNeverReturnNull() {

    }

    @Override
    Image createTextNode(final Url value) {
        return Image.with(value);
    }

    @Override
    Url value() {
        return Url.parseAbsolute("https://example.com");
    }

    @Override
    Class<Image> textNodeType() {
        return Image.class;
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createTextNode(),
            "Image \"https://example.com\"\n"
        );
    }

    // equals ..........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(Image.with(Url.parse("different-place")));
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
            protected void visit(final Image n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        this.checkEquals("132", b.toString());
    }

    // ToString ........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createNode(),
            "https://example.com"
        );
    }

    // JsonNodeMarshallingTesting.......................................................................................

    @Test
    public void testMarshall() {
        final String value = "abc123";
        this.marshallAndCheck(Image.with(Url.parse(value)), JsonNode.string(value));
    }

    @Test
    public void testUnmarshall() {
        final String value = "abc123";
        this.unmarshallAndCheck(JsonNode.string(value), Image.with(Url.parse(value)));
    }

    @Override
    public Image unmarshall(final JsonNode from,
                            final JsonNodeUnmarshallContext context) {
        return Image.unmarshallImage(from, context);
    }

    // typeNaming.......................................................................................................

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
