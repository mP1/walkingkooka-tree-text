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
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.expression.FakeTreeTextExpressionEvaluationContext;

public final class TreeTextExpressionFunctionGetTextNodeTest extends TreeTextExpressionFunctionTestCase<TreeTextExpressionFunctionGetTextNode<FakeTreeTextExpressionEvaluationContext>, TextNode> {

    @Test
    public void testApplyWithTextNode() {
        final TextNode textNode = TextNode.text("HelloText123");

        this.applyAndCheck(
            Lists.of(
                textNode
            ),
            textNode
        );
    }

    @Override
    public TreeTextExpressionFunctionGetTextNode<FakeTreeTextExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionGetTextNode.instance();
    }

    @Override
    public FakeTreeTextExpressionEvaluationContext createContext() {
        return new FakeTreeTextExpressionEvaluationContext();
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "getTextNode"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionGetTextNode<FakeTreeTextExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionGetTextNode.class);
    }
}
