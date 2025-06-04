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

import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

/**
 * A {@link ExpressionFunction} with an assumed {@link C}.
 */
abstract class TreeTextExpressionFunction<T, C extends ExpressionEvaluationContext> implements ExpressionFunction<T, C> {

    final static ExpressionFunctionParameter<TextStyle> TEXT_STYLE = ExpressionFunctionParameterName.with("textStyle")
        .required(TextStyle.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    final static ExpressionFunctionParameter<TextStylePropertyName> TEXT_STYLE_PROPERTY_NAME = ExpressionFunctionParameterName.with("propertyName")
        .required(TextStylePropertyName.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    final static ExpressionFunctionParameter<TextNode> TEXT_NODE = ExpressionFunctionParameterName.with("textNode")
        .required(TextNode.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);


    TreeTextExpressionFunction(final String name) {
        super();
        this.name = Optional.of(
            ExpressionFunctionName.with(name)
        );
    }

    @Override
    public final Optional<ExpressionFunctionName> name() {
        return this.name;
    }

    private final Optional<ExpressionFunctionName> name;

    /**
     * All functions are pure except for cell.
     */
    @Override
    public final boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public final String toString() {
        return this.name()
            .get()
            .toString();
    }
}
