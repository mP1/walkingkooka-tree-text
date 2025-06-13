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
import walkingkooka.tree.text.TextStyle;

import java.util.List;

/**
 * A function that may be used create a {@link TextStyle}. This might be useful to prepare a {@link TextStyle} as a parameter
 * to another function.
 */
final class TreeTextExpressionFunctionTextStyle<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<TextStyle, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionTextStyle<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionTextStyle<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionTextStyle<>();

    private TreeTextExpressionFunctionTextStyle() {
        super("style");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        TEXT_STYLE
    );

    @Override
    public Class<TextStyle> returnType() {
        return TextStyle.class;
    }

    @Override
    public TextStyle apply(final List<Object> parameters,
                           final C context) {
        return TEXT_STYLE.getOrFail(
            parameters,
            0
        );
    }
}
