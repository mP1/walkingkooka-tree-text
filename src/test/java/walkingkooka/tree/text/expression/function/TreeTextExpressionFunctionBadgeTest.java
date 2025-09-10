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
import walkingkooka.tree.text.Badge;
import walkingkooka.tree.text.TextNode;

public final class TreeTextExpressionFunctionBadgeTest implements ExpressionFunctionTesting<TreeTextExpressionFunctionBadge<FakeExpressionEvaluationContext>, Badge, FakeExpressionEvaluationContext>,
    ToStringTesting<TreeTextExpressionFunctionBadge<FakeExpressionEvaluationContext>> {

    @Test
    public void testApplyWithTextNode() {
        final String badgeText = "BadgeText111";
        final TextNode textNode = TextNode.text("ChildTextNode222");

        this.applyAndCheck(
            Lists.of(
                badgeText,
                textNode
            ),
            TextNode.badge(badgeText)
                .appendChild(textNode)
        );
    }

    @Override
    public TreeTextExpressionFunctionBadge<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionBadge.instance();
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
            "badge"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionBadge<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionBadge.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
