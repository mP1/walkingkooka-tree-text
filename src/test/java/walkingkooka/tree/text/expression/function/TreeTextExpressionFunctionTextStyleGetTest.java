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
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class TreeTextExpressionFunctionTextStyleGetTest extends TreeTextExpressionFunctionTestCase<TreeTextExpressionFunctionTextStyleGet<FakeExpressionEvaluationContext>, Object> {

    @Test
    public void testApplyWithTextNodeAndTextStylePropertyName() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        this.applyAndCheck(
            Lists.of(
                TextNode.text("Text that is ignored")
                    .setTextStyle(
                        TextStyle.EMPTY.set(
                            propertyName,
                            color
                        )
                    ),
                propertyName
            ),
            color
        );
    }

    @Test
    public void testApplyWithTextStyleAndTextStylePropertyName() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        this.applyAndCheck(
            Lists.of(
                TextStyle.EMPTY.set(
                    propertyName,
                    color
                ),
                propertyName
            ),
            color
        );
    }

    @Test
    public void testApplyWhenPropertyMissing() {
        this.applyAndCheck(
            Lists.of(
                TextStyle.EMPTY.set(
                    TextStylePropertyName.TEXT_ALIGN,
                    TextAlign.CENTER
                ),
                TextStylePropertyName.COLOR
            ),
            null
        );
    }

    @Override
    public TreeTextExpressionFunctionTextStyleGet<FakeExpressionEvaluationContext> createBiFunction() {
        return TreeTextExpressionFunctionTextStyleGet.instance();
    }

    @Override
    public FakeExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext();
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
            "styleGet"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctionTextStyleGet<FakeExpressionEvaluationContext>> type() {
        return Cast.to(TreeTextExpressionFunctionTextStyleGet.class);
    }
}
