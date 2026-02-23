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

package walkingkooka.tree.text.convert;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.tree.text.HasTextNode;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStylePropertyName;

public final class TreeTextConverterHasTextNodeTest extends TreeTextConverterTestCase<TreeTextConverterHasTextNode<FakeConverterContext>, FakeConverterContext>
    implements ToStringTesting<TreeTextConverterHasTextNode<FakeConverterContext>> {

    @Test
    public void testStringToString() {
        this.convertFails(
            "hello",
            String.class
        );
    }

    @Test
    public void testTextToHasTextNode() {
        this.convertAndCheck(
            TextNode.text("hello"),
            HasTextNode.class
        );
    }

    @Test
    public void testTextToTextNode() {
        this.convertAndCheck(
            TextNode.text("hello"),
            TextNode.class
        );
    }

    @Test
    public void testTextStyleNodeToTextNode() {
        this.convertAndCheck(
            TextNode.style(
                Lists.of(
                    TextNode.text("hello")
                )
            ).setAttributes(
                Maps.of(
                    TextStylePropertyName.TEXT_ALIGN,
                    TextAlign.LEFT
                )
            ),
            TextNode.class
        );
    }

    @Test
    public void testNullHasTextNodeToTextNode() {
        this.convertAndCheck(
            (HasTextNode) null,
            TextNode.class,
            null
        );
    }

    @Test
    public void testHasTextNodeToTextNode() {
        final TextNode textNode = TextNode.text("hello");

        this.convertAndCheck(
            new HasTextNode() {
                @Override
                public TextNode textNode() {
                    return textNode;
                }
            },
            TextNode.class,
            textNode
        );
    }

    @Override
    public TreeTextConverterHasTextNode<FakeConverterContext> createConverter() {
        return TreeTextConverterHasTextNode.instance();
    }

    @Override
    public FakeConverterContext createContext() {
        return (FakeConverterContext) ConverterContexts.fake();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createConverter(),
            "HasTextNode"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextConverterHasTextNode<FakeConverterContext>> type() {
        return Cast.to(TreeTextConverterHasTextNode.class);
    }
}
