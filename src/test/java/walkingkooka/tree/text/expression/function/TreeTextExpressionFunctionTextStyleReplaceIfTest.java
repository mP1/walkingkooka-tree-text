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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class TreeTextExpressionFunctionTextStyleReplaceIfTest extends TreeTextExpressionFunctionTestCase<TreeTextExpressionFunctionTextStyleReplaceIf<FakeExpressionEvaluationContext>, Styleable> {

    @Test
    public void testApplyWithTextStyleAndTextStylePropertyNameOldPropertyValueNewPropertyWrongOldValue() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;

        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        ).set(
            propertyName,
            Color.BLACK
        );

        this.applyAndCheck(
            Lists.of(
                textStyle,
                propertyName,
                Color.parse("red"),
                Color.WHITE
            ),
            textStyle
        );
    }

    @Test
    public void testApplyWithTextStyleAndTextStylePropertyNameOldPropertyValueNewPropertyValueDifferentValue() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color oldColor = Color.BLACK;
        final Color newColor = Color.WHITE;

        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.applyAndCheck(
            Lists.of(
                textStyle.set(
                    propertyName,
                    oldColor
                ),
                propertyName,
                oldColor,
                newColor
            ),
            textStyle.set(
                propertyName,
                newColor
            )
        );
    }

    @Override
    public void testParametersIfConvertTypeNotObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TreeTextExpressionFunctionTextStyleReplaceIf<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionTextStyleReplaceIf.instance();
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext() {
            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> type) {
                return this.successfulConversion(
                    type.cast(value),
                    type
                );
            }
        };
    }

    @Override
    public int minimumParameterCount() {
        return 2;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "styleReplaceIf"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionTextStyleReplaceIf<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionTextStyleReplaceIf.class);
    }
}
