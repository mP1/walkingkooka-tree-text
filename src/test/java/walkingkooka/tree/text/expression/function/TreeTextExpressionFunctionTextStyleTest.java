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
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class TreeTextExpressionFunctionTextStyleTest extends TreeTextExpressionFunctionTestCase<TreeTextExpressionFunctionTextStyle<FakeExpressionEvaluationContext>, TextStyle> {

    @Test
    public void testApply() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.applyAndCheck(
            Lists.of(
                textStyle
            ),
            textStyle
        );
    }

    @Override
    public TreeTextExpressionFunctionTextStyle<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionTextStyle.instance();
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext();
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
            "style"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionTextStyle<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionTextStyle.class);
    }
}
