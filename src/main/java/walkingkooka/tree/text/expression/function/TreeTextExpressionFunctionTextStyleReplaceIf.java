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
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;

/**
 * A function that may be used to conditionally replace a value if another old value is present, using {@link Styleable#replaceIf(TextStylePropertyName, Object, Object)}.
 */
final class TreeTextExpressionFunctionTextStyleReplaceIf<C extends ExpressionEvaluationContext> extends TreeTextExpressionFunction<Styleable, C> {

    static <C extends ExpressionEvaluationContext> TreeTextExpressionFunctionTextStyleReplaceIf<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeTextExpressionFunctionTextStyleReplaceIf<ExpressionEvaluationContext> INSTANCE = new TreeTextExpressionFunctionTextStyleReplaceIf<>();

    private TreeTextExpressionFunctionTextStyleReplaceIf() {
        super("styleReplaceIf");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<Object> OLD_VALUE = ExpressionFunctionParameterName.with("oldValue")
        .required(Object.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    final static ExpressionFunctionParameter<Object> NEW_VALUE = ExpressionFunctionParameterName.with("newValue")
        .required(Object.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        STYLEABLE,
        TEXT_STYLE_PROPERTY_NAME,
        OLD_VALUE,
        NEW_VALUE
    );

    @Override
    public Class<Styleable> returnType() {
        return Styleable.class;
    }

    @Override
    public Styleable apply(final List<Object> parameters,
                           final C context) {
        final Styleable styleable = STYLEABLE.getOrFail(parameters, 0);
        final TextStylePropertyName<?> propertyName = TEXT_STYLE_PROPERTY_NAME.getOrFail(parameters, 1);

        Object oldValue = OLD_VALUE.getOrFail(parameters, 2);
        if (null != oldValue) {
            // GWT doesnt support Class.instanceof
            if (false == propertyName.testValue(oldValue)) {
                oldValue = propertyName.parseValue(
                    context.convertOrFail(
                        oldValue,
                        String.class
                    )
                );
            }
        }

        Object newValue = NEW_VALUE.getOrFail(parameters, 3);
        if (null != newValue) {
            // GWT doesnt support Class.instanceof
            if (false == propertyName.testValue(newValue)) {
                newValue = propertyName.parseValue(
                    context.convertOrFail(
                        newValue,
                        String.class
                    )
                );
            }
        }

        return styleable.replaceIf(
            propertyName,
            Cast.to(oldValue),
            Cast.to(newValue)
        );
    }
}
