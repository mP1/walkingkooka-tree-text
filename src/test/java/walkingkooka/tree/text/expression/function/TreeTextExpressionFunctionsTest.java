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
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.net.Url;
import walkingkooka.net.convert.NetConverters;
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
import walkingkooka.tree.text.Hyperlink;
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
    public void testGetTextWithString() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        this.evaluateAndCheck(
            "getStyle",
            Lists.of(
                textStyle.text()
            ),
            textStyle
        );
    }

    @Test
    public void testGetTextWithStringBuilder() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        this.evaluateAndCheck(
            "getStyle",
            Lists.of(
                new StringBuilder(
                    textStyle.text()
                )
            ),
            textStyle
        );
    }

    @Test
    public void testGetTextWithTextNode() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        final Hyperlink hyperlink = TextNode.hyperlink(
            Url.parseAbsolute("https://example.com")
        );
        this.evaluateAndCheck(
            "getStyle",
            Lists.of(
                hyperlink.setTextStyle(textStyle)
            ),
            textStyle
        );
    }

    @Test
    public void testGetTextWithTextStyle() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        this.evaluateAndCheck(
            "getStyle",
            Lists.of(
                textStyle
            ),
            textStyle
        );
    }

    @Test
    public void testHyperlinkWithString() {
        final String url = "https://www.example.com";

        this.evaluateAndCheck(
            "hyperlink",
            Lists.of(
                url
            ),
            TextNode.hyperlink(
                Url.parseAbsolute(url)
            )
        );
    }

    @Test
    public void testImageWithString() {
        final String url = "https://www.example.com";

        this.evaluateAndCheck(
            "image",
            Lists.of(
                url
            ),
            TextNode.image(
                Url.parseAbsolute(url)
            )
        );
    }

    @Test
    public void testMergeStyleWithTextAndTextNodeWithStyleAndTextStyle() {
        final TextNode textNode = TextNode.text("HelloText123")
            .setTextStyle(
                TextStyle.parse("background-color: #111;")
            );
        final TextStyle mergeWithTextStyle = TextStyle.parse("color: #222;");

        this.evaluateAndCheck(
            "mergeStyle",
            Lists.of(
                textNode,
                mergeWithTextStyle
            ),
            textNode.merge(mergeWithTextStyle)
        );
    }

    @Test
    public void testMergeStyleWithStringAndString() {
        final String style1 = "background-color: #111;";
        final String style2 = "color: #222;";

        this.evaluateAndCheck(
            "mergeStyle",
            Lists.of(
                style1,
                style2
            ),
            TextStyle.parse(style1 + style2)
        );
    }

    @Test
    public void testMergeStyleWithTextAndTextStyleAndTextStyle() {
        final TextStyle textStyle = TextStyle.parse("background-color: #111;");
        final TextStyle mergeWithTextStyle = TextStyle.parse("color: #222;");

        this.evaluateAndCheck(
            "mergeStyle",
            Lists.of(
                textStyle,
                mergeWithTextStyle
            ),
            textStyle.merge(mergeWithTextStyle)
        );
    }

    @Test
    public void testMergeStyleWithTextNodeAndString() {
        final TextNode text = TextNode.text("HelloText123");
        final String style = "text-align: left; color: #999;";

        this.evaluateAndCheck(
            "mergeStyle",
            Lists.of(
                text,
                style
            ),
            text.setTextStyle(
                TextStyle.parse(style)
            )
        );
    }

    @Test
    public void testSetTextWithTextAndString() {
        final TextNode textNode = TextNode.text("Hello");
        final String text = "HelloText123";

        this.evaluateAndCheck(
            "setText",
            Lists.of(
                textNode,
                text
            ),
            textNode.setText(text)
        );
    }

    @Test
    public void testSetTextWithStringAndString() {
        final TextNode textNode = TextNode.text("Hello");
        final String text = "HelloText123";

        this.evaluateAndCheck(
            "setText",
            Lists.of(
                textNode.text(),
                text
            ),
            textNode.setText(text)
        );
    }

    @Test
    public void testSetTextWithTextNodeAndStringBuilder() {
        final Hyperlink hyperlink = TextNode.hyperlink(
            Url.parseAbsolute("https://example.com")
        );
        final String text = "HelloText123";

        this.evaluateAndCheck(
            "setText",
            Lists.of(
                hyperlink,
                new StringBuilder(text)
            ),
            hyperlink.setText(text)
        );
    }

    @Test
    public void testSetStyleWithTextNodeAndStringBuilder() {
        final Hyperlink hyperlink = TextNode.hyperlink(
            Url.parseAbsolute("https://example.com")
        );
        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.COLOR,
            Color.parse("#123456")
            ).set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER
        );

        this.evaluateAndCheck(
            "setStyle",
            Lists.of(
                hyperlink,
                new StringBuilder(style.text())
            ),
            hyperlink.setTextStyle(style)
        );
    }

    @Test
    public void testSetStyleWithTextNodeAndTextNode() {
        final Hyperlink hyperlink = TextNode.hyperlink(
            Url.parseAbsolute("https://example.com")
        );
        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.COLOR,
            Color.parse("#123456")
        ).set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.evaluateAndCheck(
            "setStyle",
            Lists.of(
                hyperlink,
                TextNode.text("Text ignored")
                    .setTextStyle(style)
            ),
            hyperlink.setTextStyle(style)
        );
    }

    @Test
    public void testStyleWithString() {
        final TextStyle textStyle = TextStyle.parse("color: #111; text-align: left");

        this.evaluateAndCheck(
            "style",
            Lists.of(
                textStyle.text()
            ),
            textStyle
        );
    }

    @Test
    public void testStyleGetWithTextNodeAndTextStylePropertyName() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        this.evaluateAndCheck(
            "styleGet",
            Lists.of(
                TextNode.text("Text that is ignored")
                    .setTextStyle(
                        TextStyle.EMPTY.set(
                            property,
                            color
                        )
                    ),
                property
            ),
            color
        );
    }

    @Test
    public void testStyleGetWithTextStyleAndTextStylePropertyName() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        this.evaluateAndCheck(
            "styleGet",
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
    public void testStyleGetWithStringAndString() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle style = TextStyle.EMPTY.set(
            property,
            color
        );

        this.evaluateAndCheck(
            "styleGet",
            Lists.of(
                style.text(),
                property.text()
            ),
            color
        );
    }

    @Test
    public void testStyleRemoveWithTextNodeAndTextStylePropertyName() {
        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.COLOR,
            Color.WHITE
        );

        final TextNode textNode = TextNode.text("Text123")
            .setTextStyle(style);

        final TextStylePropertyName<TextAlign> property = TextStylePropertyName.TEXT_ALIGN;

        this.evaluateAndCheck(
            "styleRemove",
            Lists.of(
                textNode.set(
                    property,
                    TextAlign.CENTER
                ),
                property
            ),
            textNode.remove(property)
        );
    }

    @Test
    public void testStyleRemoveWithTextStyleAndTextStylePropertyName() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;

        final TextStyle style = TextStyle.EMPTY.set(
            property,
            Color.BLACK
        ).set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        this.evaluateAndCheck(
            "styleRemove",
            Lists.of(
                style,
                property
            ),
            style.remove(property)
        );
    }

    @Test
    public void testStyleRemoveWithStringAndString() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;

        final TextStyle style = TextStyle.EMPTY.set(
            property,
            Color.BLACK
        ).set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        this.evaluateAndCheck(
            "styleRemove",
            Lists.of(
                style.text(),
                property.text()
            ),
            style.remove(property)
        );
    }

    @Test
    public void testStyleSetWithTextStyleAndTextStylePropertyNameAndColor() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.evaluateAndCheck(
            "styleSet",
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
    public void testStyleSetWithTextNodeAndTextStylePropertyNameAndColor() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        final TextNode textNode = TextNode.text("Text123");

        this.evaluateAndCheck(
            "styleSet",
            Lists.of(
                textNode.setTextStyle(style),
                property,
                color
            ),
            textNode.setTextStyle(
                style.set(
                    property,
                    color
                )
            )
        );
    }

    @Test
    public void testStyleSetWithStringAndStringAndString() {
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final Color color = Color.BLACK;

        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.evaluateAndCheck(
            "styleSet",
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
    public void testStyleSetWithStringAndStringAndString2() throws Exception {
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
                "styleSet",
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
    public void testStyledTextWithTextNodeAndTextNodeWithTextStyle() {
        final TextNode text = TextNode.text("Hello");
        final TextStyle style = TextStyle.EMPTY.set(
            TextStylePropertyName.COLOR,
            Color.BLACK
        );

        this.evaluateAndCheck(
            "styledText",
            Lists.of(
                text,
                TextNode.text("Ignored text")
                    .setTextStyle(style)
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
                            case "getStyle":
                                return TreeTextExpressionFunctions.getStyle();
                            case "hyperlink":
                                return TreeTextExpressionFunctions.hyperlink();
                            case "image":
                                return TreeTextExpressionFunctions.image();
                            case "mergeStyle":
                                return TreeTextExpressionFunctions.mergeStyle();
                            case "setText":
                                return TreeTextExpressionFunctions.setText();
                            case "setStyle":
                                return TreeTextExpressionFunctions.setStyle();
                            case "style":
                                return TreeTextExpressionFunctions.style();
                            case "styleGet":
                                return TreeTextExpressionFunctions.styleGet();
                            case "styleRemove":
                                return TreeTextExpressionFunctions.styleRemove();
                            case "styleSet":
                                return TreeTextExpressionFunctions.styleSet();
                            case "styledText":
                                return TreeTextExpressionFunctions.styledText();
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
                        false, // canNumbersHaveGroupSeparator
                        Converters.EXCEL_1900_DATE_SYSTEM_OFFSET, // dateTimeOffset
                        ',', // valueSeparator
                        Converters.collection(
                            Lists.of(
                                Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString(),
                                Converters.simple(), // handles Text -> TextNode
                                TreeTextConverters.hasTextStyle(),
                                TreeTextConverters.textToTextNode(),
                                TreeTextConverters.textToTextStyle(),
                                TreeTextConverters.textToTextStylePropertyName(),
                                NetConverters.textToUrl(),
                                TreeTextConverters.toStyleable(),
                                TreeTextConverters.urlToHyperlink(),
                                TreeTextConverters.urlToImage()
                            )
                        ),
                        DateTimeContexts.fake(),
                        DecimalNumberContexts.fake()
                    ),
                    LocaleContexts.fake()
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
