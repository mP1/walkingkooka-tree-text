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
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.expression.function.InvalidExpressionFunctionParameterCountException;
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TreeTextExpressionFunctionTextStyleRemoveIfTest extends TreeTextExpressionFunctionTestCase<TreeTextExpressionFunctionTextStyleRemoveIf<FakeExpressionEvaluationContext>, Styleable> {

    @Test
    public void testApplyWithTextNodeAndTextStylePropertyNameFails() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        final TextNode textNode = TextNode.text("Text123")
            .setTextStyle(textStyle);

        assertThrows(
            InvalidExpressionFunctionParameterCountException.class,
            () -> this.apply2(
                Lists.of(
                    textNode.setTextStyle(
                        textStyle.set(
                            propertyName,
                            color
                        )
                    ),
                    propertyName
                )
            )
        );
    }

    @Test
    public void testApplyWithTextStyleAndTextStylePropertyNameAndPropertyValue() {
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
                color
            ),
            textStyle
        );
    }

    @Test
    public void testApplyWithTextStyleAndTextStylePropertyNameAndDifferentPropertyValue() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        ).set(
            propertyName,
            color
        );

        this.applyAndCheck(
            Lists.of(
                textStyle,
                propertyName,
                Color.WHITE
            ),
            textStyle
        );
    }

    @Override
    public TreeTextExpressionFunctionTextStyleRemoveIf<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionTextStyleRemoveIf.instance();
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext();
    }

    @Override
    public int minimumParameterCount() {
        return 3;
    }

    @Override
    public void testParametersIfConvertTypeNotObject() {
        throw new UnsupportedOperationException();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "styleRemoveIf"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionTextStyleRemoveIf<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionTextStyleRemoveIf.class);
    }
}
