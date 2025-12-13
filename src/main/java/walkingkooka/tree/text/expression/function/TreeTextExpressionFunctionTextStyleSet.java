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
 * A function that may be used to set an individual property from a {@link TextStyle}. If the value parameter is not
 * already the correct type it will attempt to parse string values using {@link TextStylePropertyName#parseValue(String)}.
 * If the value is null it will be removed.
 */
final class TreeTextExpressionFunctionTextStyleSet<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<Styleable, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionTextStyleSet<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionTextStyleSet<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionTextStyleSet<>();

    private TreeTextExpressionFunctionTextStyleSet() {
        super("styleSet");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameterName.with("value")
        .required(Object.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        STYLEABLE,
        TEXT_STYLE_PROPERTY_NAME,
        VALUE
    );

    @Override
    public Class<Styleable> returnType() {
        return Styleable.class;
    }

    @Override
    public Styleable apply(final List<Object> parameters,
                           final C context) {
        final Styleable styleable = STYLEABLE.getOrFail(parameters, 0, context);
        final TextStylePropertyName<?> propertyName = TEXT_STYLE_PROPERTY_NAME.getOrFail(parameters, 1, context);
        Object value = VALUE.getOrFail(parameters, 2, context);

        if (null != value) {
            // GWT doesnt support Class.instanceof
            if (false == propertyName.testValue(value)) {
                value = propertyName.parseValue(
                    context.convertOrFail(
                        value,
                        String.class
                    )
                );
            }
        }

        return styleable.setOrRemove(
            propertyName,
            Cast.to(value)
        );
    }
}
