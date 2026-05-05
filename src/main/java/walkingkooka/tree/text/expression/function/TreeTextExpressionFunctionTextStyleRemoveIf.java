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
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;

/**
 * A function that may be used to conditionally remove an individual property from a {@link TextStyle} return result.
 */
final class TreeTextExpressionFunctionTextStyleRemoveIf<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<Styleable, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionTextStyleRemoveIf<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionTextStyleRemoveIf<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionTextStyleRemoveIf<>();

    private TreeTextExpressionFunctionTextStyleRemoveIf() {
        super("styleRemoveIf");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<Object> TEXT_STYLE_PROPERTY_VALUE = ExpressionFunctionParameterName.with("value")
        .required(Object.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        STYLEABLE,
        TEXT_STYLE_PROPERTY_NAME,
        TEXT_STYLE_PROPERTY_VALUE
    );

    @Override
    public Class<Styleable> returnType() {
        return Styleable.class;
    }

    @Override
    public Styleable apply(final List<Object> parameters,
                           final C context) {
        this.checkParameterCount(parameters);

        final Styleable styleable = STYLEABLE.getOrFail(parameters, 0);
        final TextStylePropertyName<?> propertyName = TEXT_STYLE_PROPERTY_NAME.getOrFail(parameters, 1);

        Object value = TEXT_STYLE_PROPERTY_VALUE.getOrFail(parameters, 2);

        // GWT doesnt support Class.instanceof
        if (false == propertyName.testValue(value)) {
            value = propertyName.parseValue(
                context.convertOrFail(
                    value,
                    String.class
                )
            );
        }

        return styleable.removeIf(
            propertyName,
            Cast.to(value)
        );
    }
}
