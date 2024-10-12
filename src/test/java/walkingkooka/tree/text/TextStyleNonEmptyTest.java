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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextStyleNonEmptyTest extends TextStyleTestCase<TextStyleNonEmpty> {

    @Test
    public void testValue() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final TextStyleNonEmpty textStyle = (TextStyleNonEmpty)TextStyle.EMPTY.setValues(map);
        this.checkEquals(
                TextNodeMap.class,
                textStyle.value()
                        .getClass(),
                () -> "" + textStyle.value
        );
    }

    // merge............................................................................................................

    @Test
    public void testMergeNotEmptySubset() {
        this.mergeAndCheck(
                TextStyle.EMPTY.setValues(
                        Maps.of(
                                this.property1(), this.value1(),
                                this.property2(), this.value2()
                        )
                ),
                TextStyle.EMPTY.setValues(
                        Maps.of(
                                this.property1(), this.value1()
                        )
                )
        );
    }

    @Test
    public void testMergeNotEmptyCombined() {
        this.mergeAndCheck(
                TextStyle.EMPTY.setValues(
                        Maps.of(
                                this.property1(), this.value1(),
                                this.property2(), this.value2()
                        )
                ),
                TextStyle.EMPTY.setValues(
                        Maps.of(
                                this.property1(), this.value1(),
                                this.property3(), this.value3()
                        )
                )
        );
    }

    // replace...........................................................................................................

    @Test
    public void testReplaceTextStyleWithoutAttributes() {
        final List<TextNode> children = this.children();
        final TextStyle textStyle = this.createTextStyle();

        this.replaceAndCheck(textStyle,
                TextNode.style(children),
                TextStyleNode.with(children, TextNodeMap.with(textStyle.value())));
    }

    @Test
    public void testReplaceTextStyleWithAttributes() {
        final List<TextNode> children = this.children();
        final TextNode textStyle = TextStyleNode.with(children, this.createTextStyle().textStyleMap());

        this.replaceAndCheck(TextStyle.EMPTY,
                textStyle,
                TextNode.style(children));
    }

    @Test
    public void testReplaceTextStyleWithParent() {
        final TextNode textStyleNode = TextNode.style(this.children());

        final TextStyle textStyle = this.createTextStyle(this.property2(), this.value2());

        this.replaceAndCheck(textStyle,
                this.makeStyleNameParent(textStyleNode),
                this.makeStyleNameParent(textStyleNode.setAttributes(textStyle.value())));
    }

    @Test
    public void testReplaceTextStyleWithParent2() {
        final TextNode textStyleNode = TextNode.style(this.children());

        final TextStyle textStyle = this.createTextStyle(this.property2(), this.value2());

        this.replaceAndCheck(textStyle,
                this.makeStyleNameParent(textStyleNode.setAttributes(Maps.of(this.property1(), this.value1()))),
                this.makeStyleNameParent(textStyleNode.setAttributes(textStyle.value())));
    }

    @Test
    public void testReplaceTextStyleName() {
        this.replaceAndCheck2(this.styleName("style123"));
    }

    @Test
    public void testReplaceTextStyleNameWithParent() {
        final TextStyleNameNode styleName = this.styleName("child-style123");
        final TextStyle textStyle = this.createTextStyle();

        this.replaceAndCheck(
                textStyle,
                this.makeStyleNameParent(styleName),
                this.makeStyleNameParent(
                        styleName.setAttributes(
                                textStyle.value()
                        )
                )
        );
    }

    @Test
    public void testReplaceText() {
        this.replaceAndCheck2(TextNode.text("abc123"));
    }

    @Test
    public void testReplaceTextWithParent() {
        this.replaceAndCheck3(TextNode.text("child-abc123"));
    }

    @Test
    public void testReplaceTextPlaceholder() {
        this.replaceAndCheck2(this.placeholder("placeholder123"));
    }

    @Test
    public void testReplaceTextPlaceholderWithParent() {
        this.replaceAndCheck3(this.placeholder("child-placeholder123"));
    }

    private void replaceAndCheck3(final TextNode textNode) {
        final TextStyle textStyle = this.createTextStyle();

        this.replaceAndCheck(
                textStyle,
                this.makeStyleNameParent(textNode),
                this.makeStyleNameParent(
                        TextStyleNode.with(
                                Lists.of(textNode),
                                textStyle.textStyleMap())
                )
        );
    }

    private void replaceAndCheck2(final TextNode textNode) {
        final TextStyle textStyle = this.createTextStyle();

        this.replaceAndCheck(
                textStyle,
                textNode,
                textNode.setAttributes(textStyle.value())
        );
    }

    // get..............................................................................................................

    @Test
    public void testGet() {
        this.getAndCheck(this.createTextStyle(),
                this.property1(),
                this.value1());
    }

    @Test
    public void testGet2() {
        this.getAndCheck(this.createTextStyle(),
                this.property2(),
                this.value2());
    }

    // set..............................................................................................................

    @Test
    public void testSetExistingPropertyAndValue() {
        this.setAndCheck(this.createTextStyle(),
                this.property1(),
                this.value1());
    }

    @Test
    public void testSetExistingPropertyAndValue2() {
        this.setAndCheck(this.createTextStyle(),
                this.property2(),
                this.value2());
    }

    @Test
    public void testSetReplacePropertyAndValue() {
        final TextStylePropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextStylePropertyName<FontFamily> property2 = this.property2();
        final FontFamily value2 = this.value2();

        final WordWrap different = WordWrap.NORMAL;
        assertNotSame(different, value1);

        this.setAndCheck(this.createTextStyle(property1, value1, property2, value2),
                property1,
                different,
                this.createTextStyle(property1, different, property2, value2));
    }

    @Test
    public void testSetReplacePropertyAndValue2() {
        final TextStylePropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextStylePropertyName<FontFamily> property2 = this.property2();
        final FontFamily value2 = this.value2();

        final FontFamily different = FontFamily.with("different");
        assertNotSame(different, value2);

        this.setAndCheck(this.createTextStyle(property1, value1, property2, value2),
                property2,
                different,
                this.createTextStyle(property1, value1, property2, different));
    }

    @Test
    public void testSetNewPropertyAndValue() {
        final TextStylePropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextStylePropertyName<FontFamily> property2 = this.property2();
        final FontFamily value2 = this.value2();

        final TextStylePropertyName<WritingMode> property3 = TextStylePropertyName.WRITING_MODE;
        final WritingMode value3 = WritingMode.VERTICAL_LR;

        this.setAndCheck(this.createTextStyle(property1, value1, property2, value2),
                property3,
                value3,
                this.createTextStyle(property1, value1, property2, value2, property3, value3));
    }

    @Test
    public void testSetNewPropertyAndValue2() {
        final TextStylePropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextStylePropertyName<FontFamily> property2 = this.property2();
        final FontFamily value2 = this.value2();

        final TextStylePropertyName<Color> property3 = TextStylePropertyName.BACKGROUND_COLOR;
        final Color value3 = Color.fromRgb(0x123456);

        this.setAndCheck(this.createTextStyle(property1, value1, property2, value2),
                property3,
                value3,
                this.createTextStyle(property3, value3, property1, value1, property2, value2));
    }

    private <T> void setAndCheck(final TextStyle textStyle,
                                 final TextStylePropertyName<T> propertyName,
                                 final T value) {
        assertSame(textStyle,
                textStyle.set(propertyName, value),
                () -> textStyle + " set " + propertyName + " and " + CharSequences.quoteIfChars(value));
    }

    // remove...........................................................................................................

    @Test
    public void testRemove() {
        final TextStylePropertyName<WordWrap> property1 = this.property1();

        final TextStylePropertyName<FontFamily> property2 = this.property2();
        final FontFamily value2 = this.value2();

        this.removeAndCheck(this.createTextStyle(property1, this.value1(), property2, value2),
                property1,
                this.createTextStyle(property2, value2));
    }

    @Test
    public void testRemove2() {
        final TextStylePropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        final TextStylePropertyName<FontFamily> property2 = this.property2();

        this.removeAndCheck(this.createTextStyle(property1, value1, property2, this.value2()),
                property2,
                this.createTextStyle(property1, value1));
    }

    @Test
    public void testRemoveBecomesEmpty() {
        final TextStylePropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();

        this.removeAndCheck(this.createTextStyle(property1, value1),
                property1,
                TextStyle.EMPTY);
    }

    // set & remove ...................................................................................................

    @Test
    public void testSetSetRemoveRemove() {
        //set
        final TextStylePropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();
        final TextStyle textStyle1 = this.setAndCheck(TextStyle.EMPTY,
                property1,
                value1,
                this.createTextStyle(property1, value1));

        //set
        final TextStylePropertyName<FontFamily> property2 = this.property2();
        final FontFamily value2 = this.value2();
        final TextStyle textStyle2 = this.setAndCheck(textStyle1,
                property2,
                value2,
                this.createTextStyle(property1, value1, property2, value2));

        // remove1
        final TextStyle textStyle3 = this.removeAndCheck(textStyle2,
                property1,
                this.createTextStyle(property2, value2));

        this.removeAndCheck(textStyle3,
                property2,
                TextStyle.EMPTY);
    }

    @Test
    public void testSetSetRemoveSet() {
        //set
        final TextStylePropertyName<WordWrap> property1 = this.property1();
        final WordWrap value1 = this.value1();
        final TextStyle textStyle1 = this.setAndCheck(TextStyle.EMPTY,
                property1,
                value1,
                this.createTextStyle(property1, value1));

        //set
        final TextStylePropertyName<FontFamily> property2 = this.property2();
        final FontFamily value2 = this.value2();
        final TextStyle textStyle2 = this.setAndCheck(textStyle1,
                property2,
                value2,
                this.createTextStyle(property1, value1, property2, value2));

        // remove1
        final TextStyle textStyle3 = this.removeAndCheck(textStyle2,
                property1,
                this.createTextStyle(property2, value2));


        //set property1 again
        this.setAndCheck(textStyle3,
                property1,
                value1,
                this.createTextStyle(property1, value1, property2, value2));
    }

    // setBorder.......................................................................................................

    @Test
    public void testSetBorder() {
        final String text = "abc123";

        final Color color = Color.parse("#123");
        final BorderStyle style = BorderStyle.DASHED;
        final Length<?> width = Length.pixel(123.5);

        this.checkEquals(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.TEXT, text)
                        .set(TextStylePropertyName.BORDER_TOP_COLOR, color)
                        .set(TextStylePropertyName.BORDER_TOP_STYLE, style)
                        .set(TextStylePropertyName.BORDER_TOP_WIDTH, width)
                        .set(TextStylePropertyName.BORDER_RIGHT_COLOR, color)
                        .set(TextStylePropertyName.BORDER_RIGHT_STYLE, style)
                        .set(TextStylePropertyName.BORDER_RIGHT_WIDTH, width)
                        .set(TextStylePropertyName.BORDER_BOTTOM_COLOR, color)
                        .set(TextStylePropertyName.BORDER_BOTTOM_STYLE, style)
                        .set(TextStylePropertyName.BORDER_BOTTOM_WIDTH, width)
                        .set(TextStylePropertyName.BORDER_LEFT_COLOR, color)
                        .set(TextStylePropertyName.BORDER_LEFT_STYLE, style)
                        .set(TextStylePropertyName.BORDER_LEFT_WIDTH, width),
                TextStyle.EMPTY.set(TextStylePropertyName.TEXT, text)
                        .setBorder(
                                color,
                                style,
                                width
                        )
        );
    }

    @Test
    public void testSetBorderSame() {
        final String text = "abc123";

        final Color color = Color.parse("#123");
        final BorderStyle style = BorderStyle.DASHED;
        final Length<?> width = Length.pixel(123.5);

        final TextStyle textStyle = TextStyle.EMPTY.set(TextStylePropertyName.TEXT, text)
                .setBorder(
                        color,
                        style,
                        width
                );

        assertSame(
                textStyle,
                textStyle.setBorder(
                        color,
                        style,
                        width
                )
        );
    }

    // getBorderColor...................................................................................................

    @Test
    public void testBorderColorGetMissing() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ),
                TextStylePropertyName.BORDER_COLOR,
                null
        );
    }

    @Test
    public void testBorderColorGetDifferent() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.BORDER_TOP_COLOR,
                        Color.parse("#111")
                ).set(
                        TextStylePropertyName.BORDER_LEFT_COLOR,
                        Color.parse("#222")
                ).set(
                        TextStylePropertyName.BORDER_RIGHT_COLOR,
                        Color.parse("#333")
                ).set(
                        TextStylePropertyName.BORDER_BOTTOM_COLOR,
                        Color.parse("#444")
                ),
                TextStylePropertyName.BORDER_COLOR,
                null
        );
    }

    @Test
    public void testBorderColorGetSame() {
        final Color color = Color.BLACK;

        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.BORDER_TOP_COLOR,
                        color
                ).set(
                        TextStylePropertyName.BORDER_LEFT_COLOR,
                        color
                ).set(
                        TextStylePropertyName.BORDER_RIGHT_COLOR,
                        color
                ).set(
                        TextStylePropertyName.BORDER_BOTTOM_COLOR,
                        color
                ),
                TextStylePropertyName.BORDER_COLOR,
                color
        );
    }

    // setBorderColor...................................................................................................

    @Test
    public void testBorderColorSet() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final Color color = Color.WHITE;

        this.setAndCheck(
                notEmpty,
                TextStylePropertyName.BORDER_COLOR,
                color,
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_TOP_COLOR,
                                color,
                                TextStylePropertyName.BORDER_LEFT_COLOR,
                                color,
                                TextStylePropertyName.BORDER_RIGHT_COLOR,
                                color,
                                TextStylePropertyName.BORDER_BOTTOM_COLOR,
                                color
                        )
                )
        );
    }

    // removeBorderColor................................................................................................

    @Test
    public void testBorderColorRemoveMissing() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty,
                TextStylePropertyName.BORDER_COLOR,
                notEmpty
        );
    }

    @Test
    public void testBorderColorRemoveSome() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.BORDER_TOP_COLOR,
                        Color.WHITE
                ),
                TextStylePropertyName.BORDER_COLOR,
                notEmpty
        );
    }

    @Test
    public void testBorderColorRemoveAll() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.BORDER_TOP_COLOR,
                        Color.parse("#111")
                ).set(
                        TextStylePropertyName.BORDER_LEFT_COLOR,
                        Color.parse("#222")
                ).set(
                        TextStylePropertyName.BORDER_RIGHT_COLOR,
                        Color.parse("#333")
                ).set(
                        TextStylePropertyName.BORDER_BOTTOM_COLOR,
                        Color.parse("#444")
                ),
                TextStylePropertyName.BORDER_COLOR,
                notEmpty
        );
    }

    // getBorderStyle...................................................................................................

    @Test
    public void testBorderStyleGetMissing() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ),
                TextStylePropertyName.BORDER_STYLE,
                null
        );
    }

    @Test
    public void testBorderStyleGetDifferent() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.BORDER_TOP_STYLE,
                        BorderStyle.DOUBLE
                ).set(
                        TextStylePropertyName.BORDER_LEFT_STYLE,
                        BorderStyle.DASHED
                ).set(
                        TextStylePropertyName.BORDER_RIGHT_STYLE,
                        BorderStyle.SOLID
                ).set(
                        TextStylePropertyName.BORDER_BOTTOM_STYLE,
                        BorderStyle.DOTTED
                ),
                TextStylePropertyName.BORDER_STYLE,
                null
        );
    }

    @Test
    public void testBorderStyleGetSame() {
        final BorderStyle borderStyle = BorderStyle.DASHED;

        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.BORDER_TOP_STYLE,
                        borderStyle
                ).set(
                        TextStylePropertyName.BORDER_LEFT_STYLE,
                        borderStyle
                ).set(
                        TextStylePropertyName.BORDER_RIGHT_STYLE,
                        borderStyle
                ).set(
                        TextStylePropertyName.BORDER_BOTTOM_STYLE,
                        borderStyle
                ),
                TextStylePropertyName.BORDER_STYLE,
                borderStyle
        );
    }

    // setBorderStyle...................................................................................................

    @Test
    public void testBorderStyleSet() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final BorderStyle borderStyle = BorderStyle.DASHED;

        this.setAndCheck(
                notEmpty,
                TextStylePropertyName.BORDER_STYLE,
                borderStyle,
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_TOP_STYLE,
                                borderStyle,
                                TextStylePropertyName.BORDER_LEFT_STYLE,
                                borderStyle,
                                TextStylePropertyName.BORDER_RIGHT_STYLE,
                                borderStyle,
                                TextStylePropertyName.BORDER_BOTTOM_STYLE,
                                borderStyle
                        )
                )
        );
    }

    // removeBorderStyle................................................................................................

    @Test
    public void testBorderStyleRemoveMissing() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty,
                TextStylePropertyName.BORDER_STYLE,
                notEmpty
        );
    }

    @Test
    public void testBorderStyleRemoveSome() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.BORDER_TOP_STYLE,
                        BorderStyle.DOUBLE
                ),
                TextStylePropertyName.BORDER_STYLE,
                notEmpty
        );
    }

    @Test
    public void testBorderStyleRemoveAll() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.BORDER_TOP_STYLE,
                        BorderStyle.DOUBLE
                ).set(
                        TextStylePropertyName.BORDER_LEFT_STYLE,
                        BorderStyle.DASHED
                ).set(
                        TextStylePropertyName.BORDER_RIGHT_STYLE,
                        BorderStyle.DOTTED
                ).set(
                        TextStylePropertyName.BORDER_BOTTOM_STYLE,
                        BorderStyle.SOLID
                ),
                TextStylePropertyName.BORDER_STYLE,
                notEmpty
        );
    }

    // getBorderWidth...................................................................................................

    @Test
    public void testBorderWidthGetMissing() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ),
                TextStylePropertyName.BORDER_WIDTH,
                null
        );
    }

    @Test
    public void testBorderWidthGetDifferent() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.BORDER_TOP_WIDTH,
                        Length.pixel(111.0)
                ).set(
                        TextStylePropertyName.BORDER_LEFT_WIDTH,
                        Length.pixel(222.0)
                ).set(
                        TextStylePropertyName.BORDER_RIGHT_WIDTH,
                                Length.pixel(333.0)
                ).set(
                        TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                        Length.pixel(444.0)
                ),
                TextStylePropertyName.BORDER_WIDTH,
                null
        );
    }

    @Test
    public void testBorderWidthGetSame() {
        final Length<?> width = Length.pixel(888.0);

        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.BORDER_TOP_WIDTH,
                        width
                ).set(
                        TextStylePropertyName.BORDER_LEFT_WIDTH,
                        width
                ).set(
                        TextStylePropertyName.BORDER_RIGHT_WIDTH,
                        width
                ).set(
                        TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                        width
                ),
                TextStylePropertyName.BORDER_WIDTH,
                width
        );
    }

    // setBorderWidth...................................................................................................

    @Test
    public void testBorderWidthSet() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final Length<?> width = Length.pixel(888.0);

        this.setAndCheck(
                notEmpty,
                TextStylePropertyName.BORDER_WIDTH,
                width,
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_TOP_WIDTH,
                                width,
                                TextStylePropertyName.BORDER_LEFT_WIDTH,
                                width,
                                TextStylePropertyName.BORDER_RIGHT_WIDTH,
                                width,
                                TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                                width
                        )
                )
        );
    }

    // removeBorderWidth................................................................................................

    @Test
    public void testBorderWidthRemoveMissing() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty,
                TextStylePropertyName.BORDER_WIDTH,
                notEmpty
        );
    }

    @Test
    public void testBorderWidthRemoveSome() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.BORDER_TOP_WIDTH,
                        Length.pixel(888.0)
                ),
                TextStylePropertyName.BORDER_WIDTH,
                notEmpty
        );
    }

    @Test
    public void testBorderWidthRemoveAll() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.BORDER_TOP_WIDTH,
                        Length.pixel(111.0)
                ).set(
                        TextStylePropertyName.BORDER_LEFT_WIDTH,
                        Length.pixel(222.0)
                ).set(
                        TextStylePropertyName.BORDER_RIGHT_WIDTH,
                        Length.pixel(333.0)
                ).set(
                        TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                        Length.pixel(444.0)
                ),
                TextStylePropertyName.BORDER_WIDTH,
                notEmpty
        );
    }

    // getMargin...................................................................................................

    @Test
    public void testMarginGetMissing() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ),
                TextStylePropertyName.MARGIN,
                null
        );
    }

    @Test
    public void testMarginGetDifferent() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.MARGIN_TOP,
                        Length.pixel(111.0)
                ).set(
                        TextStylePropertyName.MARGIN_LEFT,
                        Length.pixel(222.0)
                ).set(
                        TextStylePropertyName.MARGIN_RIGHT,
                        Length.pixel(333.0)
                ).set(
                        TextStylePropertyName.MARGIN_BOTTOM,
                        Length.pixel(444.0)
                ),
                TextStylePropertyName.MARGIN,
                null
        );
    }

    @Test
    public void testMarginGetSame() {
        final Length<?> width = Length.pixel(888.0);

        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.MARGIN_TOP,
                        width
                ).set(
                        TextStylePropertyName.MARGIN_LEFT,
                        width
                ).set(
                        TextStylePropertyName.MARGIN_RIGHT,
                        width
                ).set(
                        TextStylePropertyName.MARGIN_BOTTOM,
                        width
                ),
                TextStylePropertyName.MARGIN,
                width
        );
    }

    // setMargin........................................................................................................

    @Test
    public void testMarginSet() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final Length<?> width = Length.pixel(888.0);

        this.setAndCheck(
                notEmpty,
                TextStylePropertyName.MARGIN,
                width,
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.MARGIN_TOP,
                                width,
                                TextStylePropertyName.MARGIN_LEFT,
                                width,
                                TextStylePropertyName.MARGIN_RIGHT,
                                width,
                                TextStylePropertyName.MARGIN_BOTTOM,
                                width
                        )
                )
        );
    }

    // removeMargin.....................................................................................................

    @Test
    public void testMarginRemoveMissing() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty,
                TextStylePropertyName.MARGIN,
                notEmpty
        );
    }

    @Test
    public void testMarginRemoveSome() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.MARGIN_TOP,
                        Length.pixel(888.0)
                ),
                TextStylePropertyName.MARGIN,
                notEmpty
        );
    }

    @Test
    public void testMarginRemoveAll() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.MARGIN_TOP,
                        Length.pixel(111.0)
                ).set(
                        TextStylePropertyName.MARGIN_LEFT,
                        Length.pixel(222.0)
                ).set(
                        TextStylePropertyName.MARGIN_RIGHT,
                        Length.pixel(333.0)
                ).set(
                        TextStylePropertyName.MARGIN_BOTTOM,
                        Length.pixel(444.0)
                ),
                TextStylePropertyName.MARGIN,
                notEmpty
        );
    }

    // getPadding.......................................................................................................

    @Test
    public void testPaddingGetMissing() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ),
                TextStylePropertyName.PADDING,
                null
        );
    }

    @Test
    public void testPaddingGetDifferent() {
        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.PADDING_TOP,
                        Length.pixel(111.0)
                ).set(
                        TextStylePropertyName.PADDING_LEFT,
                        Length.pixel(222.0)
                ).set(
                        TextStylePropertyName.PADDING_RIGHT,
                        Length.pixel(333.0)
                ).set(
                        TextStylePropertyName.PADDING_BOTTOM,
                        Length.pixel(444.0)
                ),
                TextStylePropertyName.PADDING,
                null
        );
    }

    @Test
    public void testPaddingGetSame() {
        final Length<?> width = Length.pixel(888.0);

        this.getAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.PADDING_TOP,
                        width
                ).set(
                        TextStylePropertyName.PADDING_LEFT,
                        width
                ).set(
                        TextStylePropertyName.PADDING_RIGHT,
                        width
                ).set(
                        TextStylePropertyName.PADDING_BOTTOM,
                        width
                ),
                TextStylePropertyName.PADDING,
                width
        );
    }

    // setPadding.......................................................................................................

    @Test
    public void testPaddingSet() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final Length<?> width = Length.pixel(888.0);

        this.setAndCheck(
                notEmpty,
                TextStylePropertyName.PADDING,
                width,
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.PADDING_TOP,
                                width,
                                TextStylePropertyName.PADDING_LEFT,
                                width,
                                TextStylePropertyName.PADDING_RIGHT,
                                width,
                                TextStylePropertyName.PADDING_BOTTOM,
                                width
                        )
                )
        );
    }

    // removePadding.....................................................................................................

    @Test
    public void testPaddingRemoveMissing() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty,
                TextStylePropertyName.PADDING,
                notEmpty
        );
    }

    @Test
    public void testPaddingRemoveSome() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.PADDING_TOP,
                        Length.pixel(888.0)
                ),
                TextStylePropertyName.PADDING,
                notEmpty
        );
    }

    @Test
    public void testPaddingRemoveAll() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        this.removeAndCheck(
                notEmpty.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.PADDING_TOP,
                        Length.pixel(111.0)
                ).set(
                        TextStylePropertyName.PADDING_LEFT,
                        Length.pixel(222.0)
                ).set(
                        TextStylePropertyName.PADDING_RIGHT,
                        Length.pixel(333.0)
                ).set(
                        TextStylePropertyName.PADDING_BOTTOM,
                        Length.pixel(444.0)
                ),
                TextStylePropertyName.PADDING,
                notEmpty
        );
    }

    // setValues with BorderColor.......................................................................................

    @Test
    public void testBorderColorSetValues() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final Color color = Color.WHITE;

        this.checkEquals(
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_TOP_COLOR,
                                color,
                                TextStylePropertyName.BORDER_LEFT_COLOR,
                                color,
                                TextStylePropertyName.BORDER_RIGHT_COLOR,
                                color,
                                TextStylePropertyName.BORDER_BOTTOM_COLOR,
                                color
                        )
                ),
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_COLOR,
                                color
                        )
                )
        );
    }

    // setValues with BorderStyle.......................................................................................

    @Test
    public void testBorderStyleSetValues() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final BorderStyle borderStyle = BorderStyle.DOUBLE;

        this.checkEquals(
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_TOP_STYLE,
                                borderStyle,
                                TextStylePropertyName.BORDER_LEFT_STYLE,
                                borderStyle,
                                TextStylePropertyName.BORDER_RIGHT_STYLE,
                                borderStyle,
                                TextStylePropertyName.BORDER_BOTTOM_STYLE,
                                borderStyle
                        )
                ),
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_STYLE,
                                borderStyle
                        )
                )
        );
    }

    // setValues with BorderWidth.......................................................................................

    @Test
    public void testBorderWidthSetValues() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final Length<?> width = Length.pixel(123.0);

        this.checkEquals(
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_TOP_WIDTH,
                                width,
                                TextStylePropertyName.BORDER_LEFT_WIDTH,
                                width,
                                TextStylePropertyName.BORDER_RIGHT_WIDTH,
                                width,
                                TextStylePropertyName.BORDER_BOTTOM_WIDTH,
                                width
                        )
                ),
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_WIDTH,
                                width
                        )
                )
        );
    }

    // setValues with MarginWidth.......................................................................................

    @Test
    public void testMarginSetValues() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final Length<?> width = Length.pixel(123.0);

        this.checkEquals(
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.MARGIN_TOP,
                                width,
                                TextStylePropertyName.MARGIN_LEFT,
                                width,
                                TextStylePropertyName.MARGIN_RIGHT,
                                width,
                                TextStylePropertyName.MARGIN_BOTTOM,
                                width
                        )
                ),
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.MARGIN,
                                width
                        )
                )
        );
    }

    // setValues with Padding...........................................................................................

    @Test
    public void testPaddingSetValues() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final Length<?> width = Length.pixel(123.0);

        this.checkEquals(
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.PADDING_TOP,
                                width,
                                TextStylePropertyName.PADDING_LEFT,
                                width,
                                TextStylePropertyName.PADDING_RIGHT,
                                width,
                                TextStylePropertyName.PADDING_BOTTOM,
                                width
                        )
                ),
                notEmpty.setValues(
                        Maps.of(
                                TextStylePropertyName.PADDING,
                                width
                        )
                )
        );
    }

    // HasText..........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR, Color.parse("#123456")
                ),
                "color: rgb(18, 52, 86);"
        );
    }

    @Test
    public void testTextSeveralProperties() {
        this.textAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR, Color.parse("#123456")
                ).set(
                        TextStylePropertyName.TEXT_ALIGN, TextAlign.LEFT
                ),
                "color: rgb(18, 52, 86); text-align: left;"
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                createTextStyle(),
                "TextStyle\n" +
                        "  font-family=Times News Roman (walkingkooka.tree.text.FontFamily)\n" +
                        "  word-wrap=BREAK_WORD (walkingkooka.tree.text.WordWrap)\n"
        );
    }

    // json.............................................................................................................

    @Test
    public void testUnmarshallEmptyJsonObject() {
        assertSame(
                TextStyle.EMPTY,
                TextStyle.unmarshall(
                        JsonNode.object(),
                        this.unmarshallContext()
                )
        );
    }

    @Test
    public void testMarshallColor() {
        this.marshallAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.COLOR, Color.parse("#123456")),
                JsonNode.object()
                        .set(JsonPropertyName.with("color"), JsonNode.string("#123456"))
        );
    }

    @Test
    public void testMarshallColorRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.COLOR, Color.parse("#123456"))
        );
    }

    @Test
    public void testMarshallManyRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(
                TextStyle.EMPTY
                        .set(TextStylePropertyName.BACKGROUND_COLOR, Color.parse("#123456"))
                        .set(TextStylePropertyName.COLOR, Color.parse("#abcdef"))
                        .set(TextStylePropertyName.DIRECTION, Direction.LTR)
                        .set(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC)
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        this.toStringAndCheck(
                TextStyle.EMPTY.setValues(map),
                map.toString()
        );
    }

    // helpers.........................................................................................................

    @Override
    public TextStyleNonEmpty createObject() {
        return this.createTextStyle();
    }

    private TextStyleNonEmpty createTextStyle() {
        return this.createTextStyle(this.property1(), this.value1(), this.property2(), this.value2());
    }

    private <X> TextStyleNonEmpty createTextStyle(final TextStylePropertyName<X> property1,
                                                  final X value1) {
        return this.createTextStyle(Maps.of(property1, value1));
    }

    private <X, Y> TextStyleNonEmpty createTextStyle(final TextStylePropertyName<X> property1,
                                                     final X value1,
                                                     final TextStylePropertyName<Y> property2,
                                                     final Y value2) {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(property1, value1);
        map.put(property2, value2);
        return this.createTextStyle(map);
    }

    private <X, Y, Z> TextStyleNonEmpty createTextStyle(final TextStylePropertyName<X> property1,
                                                        final X value1,
                                                        final TextStylePropertyName<Y> property2,
                                                        final Y value2,
                                                        final TextStylePropertyName<Z> property3,
                                                        final Z value3) {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(property1, value1);
        map.put(property2, value2);
        map.put(property3, value3);
        return this.createTextStyle(map);
    }

    private TextStyleNonEmpty createTextStyle(final Map<TextStylePropertyName<?>, Object> map) {
        return  TextStyleNonEmpty.with(
                TextNodeMap.with(map)
        );
    }

    private TextStylePropertyName<WordWrap> property1() {
        return TextStylePropertyName.WORD_WRAP;
    }

    private WordWrap value1() {
        return WordWrap.BREAK_WORD;
    }

    private TextStylePropertyName<FontFamily> property2() {
        return TextStylePropertyName.FONT_FAMILY;
    }

    private FontFamily value2() {
        return FontFamily.with("Times News Roman");
    }

    private TextStylePropertyName<FontSize> property3() {
        return TextStylePropertyName.FONT_SIZE;
    }

    private FontSize value3() {
        return FontSize.with(12);
    }

    @Override
    Class<TextStyleNonEmpty> textStyleType() {
        return TextStyleNonEmpty.class;
    }

}
