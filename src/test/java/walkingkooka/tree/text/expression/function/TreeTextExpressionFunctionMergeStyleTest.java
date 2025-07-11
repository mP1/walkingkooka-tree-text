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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionTesting;
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;

public final class TreeTextExpressionFunctionMergeStyleTest implements ExpressionFunctionTesting<TreeTextExpressionFunctionMergeStyle<FakeExpressionEvaluationContext>, Styleable, FakeExpressionEvaluationContext>,
    ToStringTesting<TreeTextExpressionFunctionMergeStyle<FakeExpressionEvaluationContext>> {

    @Test
    public void testApplyWithTextNodeAndTextNode() {
        final TextStyle textStyle1 = TextStyle.parse("background-color: #111; color: #222;");
        final TextStyle textStyle2 = TextStyle.parse("color: #333; text-align: left;");

        final TextNode text = TextNode.text("Text1");

        this.applyAndCheck(
            Lists.of(
                text.setTextStyle(textStyle1),
                textStyle2
            ),
            text.setTextStyle(
                TextStyle.parse("background-color: #111; color: #333; text-align: left;")
            )
        );
    }

    @Test
    public void testApplyWithTextStyleAndTextStyle() {
        final TextStyle textStyle1 = TextStyle.parse("background-color: #111; color: #222;");
        final TextStyle textStyle2 = TextStyle.parse("color: #333; text-align: left;");

        this.applyAndCheck(
            Lists.of(
                textStyle1,
                textStyle2
            ),
            TextStyle.parse("background-color: #111; color: #333; text-align: left;")
        );
    }

    @Override
    public TreeTextExpressionFunctionMergeStyle<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionMergeStyle.instance();
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
            "mergeStyle"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionMergeStyle<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionMergeStyle.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
