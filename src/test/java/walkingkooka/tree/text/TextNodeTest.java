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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.net.Url;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextNodeTest extends TextNodeTestCase<TextNode> implements ToStringTesting<TextNode> {

    //isTextNodeClass...................................................................................................

    @Test
    public void testIsTextNodeClassWithNull() {
        this.isTextNodeClassAndCheck(
            null,
            false
        );
    }

    @Test
    public void testIsTextNodeClassWithTextNode() {
        this.isTextNodeClassAndCheck(
            TextNode.class,
            true
        );
    }

    @Test
    public void testIsTextNodeClassWithHyperlink() {
        this.isTextNodeClassAndCheck(
            Hyperlink.class,
            true
        );
    }

    @Test
    public void testIsTextNodeClassWithText() {
        this.isTextNodeClassAndCheck(
            Text.class,
            true
        );
    }

    @Test
    public void testIsTextNodeClassWithThis() {
        this.isTextNodeClassAndCheck(
            this.getClass(),
            false
        );
    }

    private void isTextNodeClassAndCheck(final Class<?> klass,
                                         final boolean expected) {
        this.checkEquals(
            expected,
            TextNode.isTextNodeClass(klass),
            () -> null != klass ? klass.getName() : null
        );
    }
    
    //..................................................................................................................
    
    @Test
    public void testBuildAndCheckToString() {
        this.toStringAndCheck(
            TextNode.style(Lists.of(
                TextNode.text("text-1a"),
                TextNode.styleName(TextStyleName.with("style123"))
                    .setChildren(Lists.of(TextNode.text("text-2b"), TextNode.placeholder(TextPlaceholderName.with("place-1")))))),
            "[\"text-1a\", style123[\"text-2b\", place-1]]"
        );
    }

    @Test
    public void testSelectorUsage() {
        final TextNode node = TextNode.style(Lists.of(
            TextNode.styleName(TextStyleName.with("style123"))
                .setChildren(Lists.of(TextNode.text("text-1a"), TextNode.text("text-2b"), TextNode.placeholder(TextPlaceholderName.with("place-1"))))));
        this.checkEquals(
            2L,
            TextNode.absoluteNodeSelector()
                .descendant()
                .named(Text.NAME)
                .stream(node, (c) -> ExpressionEvaluationContexts.fake(), TextNode.class)
                .count());
    }

    // root ............................................................................................................

    @Test
    public void testEmptyTextRoot() {
        final TextNode node = TextNode.EMPTY_TEXT;

        assertSame(
            node,
            node.root()
        );
    }

    // setTextStyle.....................................................................................................

    @Test
    public void testSetTextStyleWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextNode.EMPTY_TEXT.setTextStyle(null)
        );
    }

    @Test
    public void testSetTextStyleWithEmpty() {
        final TextNode node = TextNode.image(Url.parseAbsolute("https://example.com/image.gif"));

        assertSame(
            node,
            node.setTextStyle(TextStyle.EMPTY)
        );
    }

    @Test
    public void testSetTextStyleWithNonEmpty() {
        final TextNode image = TextNode.image(Url.parseAbsolute("https://example.com/image.gif"));
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color color = Color.parse("#123");

        this.setTextStyleAndCheck(
            image,
            TextStyle.EMPTY.set(
                propertyName,
                color
            ),
            TextNode.style(
                Lists.of(image)
            ).setAttributes(
                Maps.of(
                    propertyName,
                    color
                )
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextNode> type() {
        return TextNode.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
