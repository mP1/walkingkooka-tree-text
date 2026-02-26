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

package walkingkooka.tree.text.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;

public final class TreeTextExpressionFunctionSetStyleTest extends TreeTextExpressionFunctionTestCase<TreeTextExpressionFunctionSetStyle<FakeExpressionEvaluationContext>, TextNode> {

    @Test
    public void testApplyTextNodeAndTextStyle() {
        final TextNode textNode = TextNode.text("HelloText123");
        final TextStyle textStyle = TextStyle.parse("color: #123456");

        this.applyAndCheck(
            Lists.of(
                textNode,
                textStyle
            ),
            textNode.setTextStyle(textStyle)
        );
    }

    @Test
    public void testApplyTextNodeAndTextNode() {
        final TextNode textNode = TextNode.text("HelloText123");
        final TextStyle textStyle = TextStyle.parse("color: #123456");

        this.applyAndCheck(
            Lists.of(
                textNode,
                TextNode.text("text is ignored")
                    .setTextStyle(textStyle)
            ),
            textNode.setTextStyle(textStyle)
        );
    }

    @Override
    public TreeTextExpressionFunctionSetStyle<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionSetStyle.instance();
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext();
    }

    @Override
    public int minimumParameterCount() {
        return 2;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "setStyle"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionSetStyle<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionSetStyle.class);
    }
}
