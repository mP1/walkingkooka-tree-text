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
import walkingkooka.tree.text.Flag;
import walkingkooka.tree.text.TextNode;

public final class TreeTextExpressionFunctionFlagTest implements ExpressionFunctionTesting<TreeTextExpressionFunctionFlag<FakeExpressionEvaluationContext>, Flag, FakeExpressionEvaluationContext>,
    ToStringTesting<TreeTextExpressionFunctionFlag<FakeExpressionEvaluationContext>> {

    @Test
    public void testApplyWithString() {
        final String countryCode = "AU";

        this.applyAndCheck(
            Lists.of(countryCode),
            TextNode.flag(countryCode)
        );
    }

    @Override
    public TreeTextExpressionFunctionFlag<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionFlag.instance();
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
            "flag"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionFlag<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionFlag.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
