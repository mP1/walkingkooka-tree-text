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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionTesting;
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class TreeTextExpressionFunctionTextStyleSetTest implements ExpressionFunctionTesting<TreeTextExpressionFunctionTextStyleSet<FakeExpressionEvaluationContext>, Styleable, FakeExpressionEvaluationContext>,
    ToStringTesting<TreeTextExpressionFunctionTextStyleSet<FakeExpressionEvaluationContext>> {

    @Test
    public void testApplyWithTextStyleAndTextStylePropertyNameWhenNewProperty() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.applyAndCheck(
            Lists.of(
                textStyle,
                propertyName,
                color
            ),
            textStyle.set(
                propertyName,
                color
            )
        );
    }

    @Test
    public void testApplyWithTextStyleAndStringWhenNewProperty() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.applyAndCheck(
            Lists.of(
                textStyle,
                propertyName,
                color.toString()
            ),
            textStyle.set(
                propertyName,
                color
            )
        );
    }

    @Test
    public void testApplyWithTextStyleAndTextStylePropertyNameAndNullValueWhichRemovesProperty() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.applyAndCheck(
            Lists.of(
                textStyle.set(
                    propertyName,
                    color
                ),
                propertyName,
                null
            ),
            textStyle.remove(
                propertyName
            )
        );
    }

    @Test
    public void testApplyWithTextNodeAndTextStylePropertyNameAndNullValueWhichRemovesProperty() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        final TextNode textNode = TextNode.text("HelloText")
            .setTextStyle(textStyle);

        this.applyAndCheck(
            Lists.of(
                textNode.setTextStyle(
                    textStyle.set(
                        propertyName,
                        color
                    )
                ),
                propertyName,
                null
            ),
            textNode.setTextStyle(
                textStyle.remove(
                    propertyName
                )
            )
        );
    }

    @Override
    public void testParametersIfConvertTypeNotObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TreeTextExpressionFunctionTextStyleSet<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionTextStyleSet.instance();
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
            "styleSet"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionTextStyleSet<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionTextStyleSet.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
