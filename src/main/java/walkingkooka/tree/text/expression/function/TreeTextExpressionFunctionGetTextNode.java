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
import walkingkooka.tree.text.HasTextNode;
import walkingkooka.tree.text.TextNode;

import java.util.List;

/**
 * A function that may be used to get the {@link TextNode} from a source.
 */
final class TreeTextExpressionFunctionGetTextNode<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<TextNode, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionGetTextNode<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionGetTextNode<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionGetTextNode<>();

    private TreeTextExpressionFunctionGetTextNode() {
        super("getTextNode");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        HAS_TEXT_NODE
    );

    @Override
    public Class<TextNode> returnType() {
        return TextNode.class;
    }

    @Override
    public TextNode apply(final List<Object> parameters,
                           final C context) {
        final HasTextNode has = HAS_TEXT_NODE.getOrFail(parameters, 0, context);

        return has.textNode();
    }
}
