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
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Opacity;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextOverflow;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.convert.TreeTextConverters;

import java.lang.reflect.Method;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

public final class TreeTextExpressionFunctionsTest implements PublicStaticHelperTesting<TreeTextExpressionFunctions>,
        TreePrintableTesting {

    @Test
    public void testStyledTextWithStringAndString() {
        final TextNode text = TextNode.text("Hello");
        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.COLOR,
            Color.BLACK
        );

        this.evaluateAndCheck(
            "styledText",
            Lists.of(
                text.text(),
                style.text()
            ),
            text.setTextStyle(style)
        );
    }

    @Test
    public void testStyledTextWithTextNodeAndTextStyle() {
        final TextNode text = TextNode.text("Hello");
        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.COLOR,
            Color.BLACK
        );

        this.evaluateAndCheck(
            "styledText",
            Lists.of(
                text,
                style
            ),
            text.setTextStyle(style)
        );
    }

    @Test
    public void testTextStyleGetWithTextStyleAndTextStylePropertyName() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        this.evaluateAndCheck(
            "textStyleGet",
            Lists.of(
                TextStyle.EMPTY.set(
                    property,
                    color
                ),
                property
            ),
            color
        );
    }

    @Test
    public void testTextStyleGetWithStringAndString() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle style = TextStyle.EMPTY.set(
            property,
            color
        );

        this.evaluateAndCheck(
            "textStyleGet",
            Lists.of(
                style.text(),
                property.text()
            ),
            color
        );
    }

    @Test
    public void testTextStyleRemoveWithTextStyleAndTextStylePropertyName() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;

        final TextStyle style = TextStyle.EMPTY.set(
            property,
            Color.BLACK
        ).set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        this.evaluateAndCheck(
            "textStyleRemove",
            Lists.of(
                style,
                property
            ),
            style.remove(property)
        );
    }

    @Test
    public void testTextStyleRemoveWithStringAndString() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;

        final TextStyle style = TextStyle.EMPTY.set(
            property,
            Color.BLACK
        ).set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        this.evaluateAndCheck(
            "textStyleRemove",
            Lists.of(
                style.text(),
                property.text()
            ),
            style.remove(property)
        );
    }

    @Test
    public void testTextStyleSetWithTextStyleAndTextStylePropertyNameAndColor() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.evaluateAndCheck(
            "textStyleSet",
            Lists.of(
                style,
                property,
                color
            ),
            style.set(
                property,
                color
            )
        );
    }

    @Test
    public void testTextStyleSetWithStringAndStringAndString() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.evaluateAndCheck(
            "textStyleSet",
            Lists.of(
                style.text(),
                property.text(),
                color.toString()
            ),
            style.set(
                property,
                color
            )
        );
    }

    @Test
    public void testTextStyleSetWithStringAndStringAndString2() throws Exception {
        for (final TextStylePropertyName<?> propertyName : TextStylePropertyName.VALUES) {
            final TextStyle style = TextStyle.EMPTY;

            final Object propertyValue;

            final Class<?> valueType = propertyName.valueType();
            switch (valueType.getSimpleName()) {
                case "Color":
                    propertyValue = Color.BLACK;
                    break;
                case "FontFamily":
                    propertyValue = FontFamily.with("Times New Roman");
                    break;
                case "FontSize":
                    propertyValue = FontSize.with(10);
                    break;
                case "FontWeight":
                    propertyValue = FontWeight.with(10);
                    break;
                case "Length":
                    propertyValue = Length.pixel(1.0);
                    break;
                case "Opacity":
                    propertyValue = Opacity.with(0.5);
                    break;
                case "String":
                    propertyValue = "Hello";
                    break;
                case "TextOverflow":
                    propertyValue = TextOverflow.string("TextOverflowString");
                    break;

                default:
                    if (valueType.isEnum()) {
                        final Enum[] enums = (Enum[])
                            valueType.getMethod("values")
                                .invoke(null);
                        propertyValue = enums[0];
                        break;
                    }

                    throw new UnsupportedOperationException(propertyName + " " + propertyName.valueType().getSimpleName());
            }

            String propertyValueText = propertyValue.toString();
            if (valueType == Opacity.class) {
                propertyValueText = Opacity.with(0.5).text();
            }

            this.evaluateAndCheck(
                "textStyleSet",
                Lists.of(
                    style.text(),
                    propertyName.text(),
                    propertyValueText
                ),
                TextStyle.EMPTY.set(
                    propertyName,
                    Cast.to(propertyValue)
                )
            );
        }
    }

    private void evaluateAndCheck(final String functionName,
                                  final List<Object> parameters,
                                  final Object expected) {
        this.checkEquals(
            expected,
            Expression.call(
                    Expression.namedFunction(
                        ExpressionFunctionName.with(functionName)
                    ),
                    parameters.stream()
                        .map(Expression::value)
                        .collect(Collectors.toList())
            ).toValue(
                ExpressionEvaluationContexts.basic(
                    ExpressionNumberKind.BIG_DECIMAL,
                    (name) -> {
                        switch(name.value()) {
                            case "styledText":
                                return TreeTextExpressionFunctions.styledText();
                            case "textStyleGet":
                                return TreeTextExpressionFunctions.textStyleGet();
                            case "textStyleRemove":
                                return TreeTextExpressionFunctions.textStyleRemove();
                            case "textStyleSet":
                                return TreeTextExpressionFunctions.textStyleSet();
                            default:
                                throw new UnknownExpressionFunctionException(name);
                        }
                    }, // name -> function
                    (final RuntimeException cause) -> {
                        throw cause;
                    },
                    (ExpressionReference reference) -> {
                        throw new UnsupportedOperationException();
                    },
                    (ExpressionReference reference) -> {
                        throw new UnsupportedOperationException();
                    },
                    CaseSensitivity.SENSITIVE,
                    ConverterContexts.basic(
                        Converters.EXCEL_1900_DATE_SYSTEM_OFFSET, // dateTimeOffset
                        Converters.collection(
                            Lists.of(
                                Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString(),
                                Converters.simple(), // handles Text -> TextNode
                                TreeTextConverters.textToTextNode(),
                                TreeTextConverters.textToTextStyle(),
                                TreeTextConverters.textToTextStylePropertyName(),
                                TreeTextConverters.urlToHyperlink(),
                                TreeTextConverters.urlToImage()
                            )
                        ),
                        DateTimeContexts.fake(),
                        DecimalNumberContexts.fake()
                    )
                )
            )
        );
    }

    // PublicStaticHelperTesting........................................................................................

    @Test
    public void testPublicStaticMethodsWithoutMathContextParameter() {
        this.publicStaticMethodParametersTypeCheck(MathContext.class);
    }

    // class............................................................................................................

    @Override
    public Class<TreeTextExpressionFunctions> type() {
        return TreeTextExpressionFunctions.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
