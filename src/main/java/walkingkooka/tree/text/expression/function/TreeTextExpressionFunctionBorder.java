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
import walkingkooka.tree.text.Border;

import java.util.List;

/**
 * A function that accepts a {@link Url} and creates a {@link Border}.
 */
final class TreeTextExpressionFunctionBorder<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<Border, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionBorder<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionBorder<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionBorder<>();

    private TreeTextExpressionFunctionBorder() {
        super("border");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        BORDER
    );

    @Override
    public Class<Border> returnType() {
        return Border.class;
    }

    @Override
    public Border apply(final List<Object> parameters,
                      final C context) {
        return BORDER.getOrFail(parameters, 0);
    }
}
