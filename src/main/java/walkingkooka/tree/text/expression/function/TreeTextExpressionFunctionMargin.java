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
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.text.Margin;

import java.util.List;

/**
 * A function that accepts a {@link String} and creates a {@link Margin}.
 */
final class TreeTextExpressionFunctionMargin<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<Margin, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionMargin<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionMargin<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionMargin<>();

    private TreeTextExpressionFunctionMargin() {
        super("margin");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        MARGIN
    );

    @Override
    public Class<Margin> returnType() {
        return Margin.class;
    }

    @Override
    public Margin apply(final List<Object> parameters,
                        final C context) {
        return MARGIN.getOrFail(parameters, 0);
    }
}
