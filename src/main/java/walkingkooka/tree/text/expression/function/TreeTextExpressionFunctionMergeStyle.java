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
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextStyle;

import java.util.List;

/**
 * A function that merges two {@link TextStyle} into one.
 */
final class TreeTextExpressionFunctionMergeStyle<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<Styleable, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionMergeStyle<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionMergeStyle<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionMergeStyle<>();

    private TreeTextExpressionFunctionMergeStyle() {
        super("mergeStyle");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<Styleable> FIRST_STYLEABLE = STYLEABLE.setName(ExpressionFunctionParameterName.with("styleable"));

    final static ExpressionFunctionParameter<TextStyle> SECOND_TEXT_STYLE = TEXT_STYLE.setName(ExpressionFunctionParameterName.with("textStyle"));

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        FIRST_STYLEABLE,
        SECOND_TEXT_STYLE
    );

    @Override
    public Class<Styleable> returnType() {
        return Styleable.class;
    }

    @Override
    public Styleable apply(final List<Object> parameters,
                           final C context) {
        final Styleable first = STYLEABLE.getOrFail(parameters, 0);
        final TextStyle mergeTextStyle = SECOND_TEXT_STYLE.getOrFail(parameters, 1);

        return first.merge(mergeTextStyle);
    }
}
