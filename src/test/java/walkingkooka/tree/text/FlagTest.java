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

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FlagTest extends TextLeafNodeTestCase<Flag, String> {

    // &#x1F1E6; &#x1F1FA;
    // &#127462; &#127482;
    private final static String AU_FLAG = "AU"; // "\\ud83c\\udde6\\ud83c\\uddfa";

    private final static String AU_FLAG_HTML_ENTITY = "&#x1F1E6;&#x1F1FA;";

    @Test
    public void testWithNullStringFails() {
        assertThrows(
            NullPointerException.class,
            () -> Flag.with(null)
        );
    }

    @Test
    public void testWithEmptyStringFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Flag.with("")
        );
    }

    @Test
    public void testWithInvalidStringLengthFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> Flag.with("ABC")
        );

        this.checkEquals(
            "Invalid flag country code: \"ABC\" not 2 letters",
            thrown.getMessage()
        );
    }

    @Test
    public void testWithUpperCased() {
        final Flag flag = Flag.with(AU_FLAG);
        this.checkEquals(
            AU_FLAG,
            flag.value
        );
    }

    @Test
    public void testWithLowerCased() {
        final Flag flag = Flag.with(
            AU_FLAG.toLowerCase(Locale.ENGLISH)
        );
        this.checkEquals(
            AU_FLAG,
            flag.value
        );
    }

    // SetText..........................................................................................................

    @Test
    public void testSetTextWithEmptyFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createTextNode()
                .setText("")
        );
    }

    @Test
    public void testSetTextWithInvalidCountryCodeFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createTextNode()
                .setText("INV")
        );
    }

    // HasText ........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(
            this.createTextNode(),
            AU_FLAG
        );
    }

    // HasTextOffset ...................................................................................................

    @Test
    public void testTextOffsetWithParent() {
        this.textOffsetAndCheck(
            TextNode.style(
                Lists.of(
                    Text.with("a1"),
                    this.createTextNode()
                )
                ).children()
                .get(1),
            2
        );
    }

    // toHtml...........................................................................................................

    @Test
    public void testToHtml() {
        this.toHtmlAndCheck(
            this.createNode(),
            AU_FLAG_HTML_ENTITY
        );
    }

    @Override
    public void testPropertiesNeverReturnNull() {

    }

    @Override
    Flag createTextNode(final String value) {
        return Flag.with(value);
    }

    // &#x1F1E6; &#x1F1FA;
    // &#127462; &#127482;
    @Override
    String value() {
        return AU_FLAG;
    }

    @Override
    Class<Flag> textNodeType() {
        return Flag.class;
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createTextNode(),
            "Flag \""+ AU_FLAG + "\"\n"
        );
    }

    // equals ..........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(
            Flag.with("NZ")
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
            protected void visit(final Flag n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);

        this.checkEquals(
            "132",
            b.toString()
        );
    }

    // ToString ........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createNode(),
            AU_FLAG
        );
    }

    // JsonNodeMarshallingTesting.......................................................................................

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
            Flag.with(AU_FLAG),
            JsonNode.string(AU_FLAG)
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            JsonNode.string(AU_FLAG),
            Flag.with(AU_FLAG)
        );
    }

    @Override
    public Flag unmarshall(final JsonNode from,
                           final JsonNodeUnmarshallContext context) {
        return Flag.unmarshallFlag(
            from,
            context
        );
    }

    // canBeEmpty.......................................................................................................

    @Test
    public void testIsEmptyWhenEmpty() {
        this.isEmptyAndCheck(
            this.createTextNode(),
            false
        );
    }

    // typeNaming.......................................................................................................

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
