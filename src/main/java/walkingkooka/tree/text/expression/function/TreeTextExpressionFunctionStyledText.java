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
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;

import java.util.List;

/**
 * A function that may be used to apply a {@link walkingkooka.tree.text.TextStyle} to a given {@link TextNode}.
 */
final class TreeTextExpressionFunctionStyledText<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<TextNode, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionStyledText<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionStyledText<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionStyledText<>();

    private TreeTextExpressionFunctionStyledText() {
        super("styledText");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        TEXT_NODE,
        TEXT_STYLE
    );

    @Override
    public Class<TextNode> returnType() {
        return TextNode.class;
    }

    @Override
    public TextNode apply(final List<Object> parameters,
                          final C context) {
        final TextNode textNode = TEXT_NODE.getOrFail(parameters, 0);
        final TextStyle textStyle = TEXT_STYLE.getOrFail(parameters, 1);
        return textNode.setTextStyle(textStyle);
    }
}
