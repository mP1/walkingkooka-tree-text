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

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.text.Badge;
import walkingkooka.tree.text.Hyperlink;
import walkingkooka.tree.text.Image;
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;

/**
 * A collection of helpers for getting {@link ExpressionFunction}.
 */
public final class TreeTextExpressionFunctions implements PublicStaticHelper {

    /**
     * {@see TreeTextExpressionFunctionBadge}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<Badge, C> badge() {
        return TreeTextExpressionFunctionBadge.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionGetTextNode}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<TextNode, C> getTextNode() {
        return TreeTextExpressionFunctionGetTextNode.instance();
    }
    
    /**
     * {@see TreeTextExpressionFunctionGetStyle}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<TextStyle, C> getStyle() {
        return TreeTextExpressionFunctionGetStyle.instance();
    }
    
    /**
     * {@see TreeTextExpressionFunctionHyperlink}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<Hyperlink, C> hyperlink() {
        return TreeTextExpressionFunctionHyperlink.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionImage}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<Image, C> image() {
        return TreeTextExpressionFunctionImage.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionMergeStyle}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<Styleable, C> mergeStyle() {
        return TreeTextExpressionFunctionMergeStyle.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionSetText}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<TextNode, C> setText() {
        return TreeTextExpressionFunctionSetText.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionSetStyle}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<TextNode, C> setStyle() {
        return TreeTextExpressionFunctionSetStyle.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionTextStyle}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<TextStyle, C> style() {
        return TreeTextExpressionFunctionTextStyle.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionTextStyleGet}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<Object, C> styleGet() {
        return TreeTextExpressionFunctionTextStyleGet.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionTextStyleRemove}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<Styleable, C> styleRemove() {
        return TreeTextExpressionFunctionTextStyleRemove.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionTextStyleSet}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<Styleable, C> styleSet() {
        return TreeTextExpressionFunctionTextStyleSet.instance();
    }

    /**
     * {@see TreeTextExpressionFunctionStyledText}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunction<TextNode, C> styledText() {
        return TreeTextExpressionFunctionStyledText.instance();
    }
    
    /**
     * Stop creation
     */
    private TreeTextExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}
