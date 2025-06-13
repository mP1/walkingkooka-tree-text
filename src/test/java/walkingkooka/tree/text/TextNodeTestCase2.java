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
import walkingkooka.collect.map.Maps;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.HasTextLengthTesting;
import walkingkooka.text.HasTextTesting;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.HasTextOffsetTesting;
import walkingkooka.tree.NodeTesting;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TextNodeTestCase2<N extends TextNode> extends TextNodeTestCase<TextNode>
    implements NodeTesting<TextNode, TextNodeName, TextStylePropertyName<?>, Object>,
    JsonNodeMarshallingTesting<TextNode>,
    HasTextLengthTesting,
    HasTextOffsetTesting,
    HasTextStyleTesting,
    HasTextTesting,
    TreePrintableTesting {

    TextNodeTestCase2() {
        super();
    }

    // setText..........................................................................................................

    @Test
    public void testSetTextWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createTextNode()
                .setText(null)
        );
    }

    final void setTextAndCheck(final N textNode,
                               final String text) {
        assertSame(
            textNode,
            textNode.setText(text)
        );
    }

    final void setTextAndCheck(final N textNode,
                               final String text,
                               final TextNode expected) {
        this.checkEquals(
            expected,
            textNode.setText(text)
        );
    }

    // SetAttributes....................................................................................................

    @Test
    public final void testSetAttributesEmpty() {
        final N textNode = this.createTextNode();
        assertSame(textNode, textNode.setAttributes(TextNode.NO_ATTRIBUTES));
    }

    final void setAttributeNotEmptyAndCheck() {
        final N before = this.createTextNode();
        final Map<TextStylePropertyName<?>, Object> attributes = Maps.of(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC);
        final TextNode after = before.setAttributes(attributes);
        assertNotSame(after, before);

        this.parentMissingCheck(after);
        this.childCountCheck(after, before);
        this.checkEquals(
            attributes,
            after.attributes(),
            "attributes"
        );
    }

    // textStyle........................................................................................................

    @Test
    public final void testTextStyle() {
        final N textNode = this.createTextNode();
        this.checkEquals(
            textNode.attributes(),
            textNode.textStyle().textStyleMap()
        );
        this.checkEquals(
            TextStyle.EMPTY.setValues(
                textNode.attributes()
            ),
            textNode.textStyle()
        );
    }

    // HasTextOffset .....................................................................................................

    @Test
    public final void testTextOffset() {
        this.textOffsetAndCheck(this.createTextNode(), 0);
    }

    // toHtml...........................................................................................................

    final void toHtmlAndCheck(final TextNode node,
                              final String html) {
        this.checkEquals(
            html,
            node.toHtml(),
            () -> node + " toHtml"
        );
    }

    // TextNodeVisitor..................................................................................................

    @Test
    public final void testVisitor() {
        new TextNodeVisitor() {
        }.accept(this.createNode());
    }

    // helpers .........................................................................................................

    @Override
    public final TextNode createNode() {
        return this.createTextNode();
    }

    abstract N createTextNode();

    // ClassTesting.....................................................................................................

    @Override
    public final Class<TextNode> type() {
        return Cast.to(this.textNodeType());
    }

    abstract Class<N> textNodeType();

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // TypeNameTesting...................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Text";
    }

    // JsonNodeMarshallingTesting...........................................................................................

    @Override
    public final TextNode createJsonNodeMarshallingValue() {
        return this.createTextNode();
    }
}
