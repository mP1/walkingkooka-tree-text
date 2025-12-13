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

import walkingkooka.Cast;
import walkingkooka.net.Url;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.text.Badge;
import walkingkooka.tree.text.TextNode;

import java.util.List;

/**
 * A function that accepts a {@link Url} and creates a {@link Badge}.
 */
final class TreeTextExpressionFunctionBadge<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<Badge, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionBadge<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionBadge<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionBadge<>();

    private TreeTextExpressionFunctionBadge() {
        super("badge");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        TEXT,
        TEXT_NODE
    );

    @Override
    public Class<Badge> returnType() {
        return Badge.class;
    }

    @Override
    public Badge apply(final List<Object> parameters,
                       final C context) {
        final String badgeText = TEXT.getOrFail(parameters, 0);
        final TextNode textNode = TEXT_NODE.getOrFail(parameters, 1);

        return TextNode.badge(badgeText)
            .appendChild(textNode);
    }
}
