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
import walkingkooka.color.WebColorName;
import walkingkooka.props.Properties;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextStyleNonEmptyTest extends TextStyleTestCase<TextStyleNonEmpty> {

    private final static TextStylePropertyName<WordWrap> PROPERTY1 = TextStylePropertyName.WORD_WRAP;

    private final static WordWrap VALUE1 = WordWrap.BREAK_WORD;

    private final static TextStylePropertyName<FontFamily> PROPERTY2 = TextStylePropertyName.FONT_FAMILY;

    private final static FontFamily VALUE2 = FontFamily.with("Times News Roman");

    @Test
    public void testValue() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(PROPERTY1, VALUE1);
        map.put(PROPERTY2, VALUE2);

        final TextStyleNonEmpty textStyle = (TextStyleNonEmpty) TextStyle.EMPTY.setValues(map);
        this.checkEquals(
            TextStylePropertiesMap.class,
            textStyle.value()
                .getClass(),
            () -> "" + textStyle.value
        );
    }

    // merge............................................................................................................

    @Test
    public void testMergeWithEmpty() {
        final TextStyle textStyle = TextStyle.parse("background-color: #111; color: #222");

        assertSame(
            textStyle,
            textStyle.merge(
                TextStyle.EMPTY
            )
        );
    }

    @Test
    public void testMergeWithNotEmptyReplaces() {
        this.mergeAndCheck(
            TextStyle.parse("background-color: #111;"),
            TextStyle.parse("background-color: #999;"),
            TextStyle.parse("background-color: #999;")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent() {
        this.mergeAndCheck(
            TextStyle.parse("background-color: #999;"),
            TextStyle.parse("background-color: #111; color: #222; text-align: left"),
            TextStyle.parse("background-color: #111; color: #222; text-align: left")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent2() {
        this.mergeAndCheck(
            TextStyle.parse("color: #999;"),
            TextStyle.parse("background-color: #111; color: #222; text-align: left"),
            TextStyle.parse("background-color: #111; color: #222; text-align: left")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent3() {
        this.mergeAndCheck(
            TextStyle.parse("text-align: right;"),
            TextStyle.parse("background-color: #111; color: #222; text-align: left"),
            TextStyle.parse("background-color: #111; color: #222; text-align: left")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent4() {
        this.mergeAndCheck(
            TextStyle.parse("background-color: #111; color: #222; text-align: left"),
            TextStyle.parse("background-color: #999;"),
            TextStyle.parse("background-color: #999; color: #222; text-align: left")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent5() {
        this.mergeAndCheck(
            TextStyle.parse("background-color: #111; color: #222; text-align: left"),
            TextStyle.parse("color: #999;"),
            TextStyle.parse("background-color: #111; color: #999; text-align: left")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent6() {
        this.mergeAndCheck(
            TextStyle.parse("background-color: #111; color: #222; text-align: left"),
            TextStyle.parse("text-align: right;"),
            TextStyle.parse("background-color: #111; color: #222; text-align: right")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent7() {
        this.mergeAndCheck(
            TextStyle.parse("background-color: #111; color: #222; text-align: left"),
            TextStyle.parse("font-weight: bold;"),
            TextStyle.parse("background-color: #111; color: #222; font-weight: bold; text-align: left")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent8() {
        this.mergeAndCheck(
            TextStyle.parse("font-weight: bold;"),
            TextStyle.parse("background-color: #111; color: #222; text-align: left"),
            TextStyle.parse("background-color: #111; color: #222; font-weight: bold; text-align: left")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent9() {
        this.mergeAndCheck(
            TextStyle.parse("font-weight: bold;"),
            TextStyle.parse("background-color: #111; color: #222; font-weight: bold; text-align: left"),
            TextStyle.parse("background-color: #111; color: #222; font-weight: bold; text-align: left")
        );
    }

    @Test
    public void testMergeWithNotEmptyWithDifferent10() {
        this.mergeAndCheck(
            TextStyle.parse("background-color: #111; color: #222; font-weight: bold; text-align: left"),
            TextStyle.parse("font-weight: bold;"),
            TextStyle.parse("background-color: #111; color: #222; font-weight: bold; text-align: left")
        );
    }

    // replace...........................................................................................................

    @Test
    public void testReplaceTextStyleWithoutAttributes() {
        final List<TextNode> children = this.children();
        final TextStyle textStyle = this.createTextStyle();

        this.replaceAndCheck(
            textStyle,
            TextNode.style(children),
            TextStyleNode.with(
                children,
                TextStylePropertiesMap.with(textStyle.value())
            )
        );
    }

    @Test
    public void testReplaceTextStyleWithAttributes() {
        final List<TextNode> children = this.children();
        final TextNode textStyle = TextStyleNode.with(
            children,
            this.createTextStyle()
                .textStylePropertiesMap()
        );

        this.replaceAndCheck(
            TextStyle.EMPTY,
            textStyle,
            TextNode.style(children)
        );
    }

    @Test
    public void testReplaceTextStyleWithParent() {
        final TextNode textStyleNode = TextNode.style(this.children());

        final TextStyle textStyle = this.createTextStyle(
            PROPERTY2,
            VALUE2
        );

        this.replaceAndCheck(
            textStyle,
            this.makeStyleNameParent(textStyleNode),
            this.makeStyleNameParent(
                textStyleNode.setAttributes(textStyle.value())
            )
        );
    }

    @Test
    public void testReplaceTextStyleWithParent2() {
        final TextNode textStyleNode = TextNode.style(this.children());

        final TextStyle textStyle = this.createTextStyle(
            PROPERTY2,
            VALUE2
        );

        this.replaceAndCheck(
            textStyle,
            this.makeStyleNameParent(
                textStyleNode.setAttributes(
                    Maps.of(
                        PROPERTY1,
                        VALUE1
                    )
                )
            ),
            this.makeStyleNameParent(
                textStyleNode.setAttributes(
                    textStyle.value()
                )
            )
        );
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
                    textStyle.textStylePropertiesMap())
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
        this.getAndCheck(
            this.createTextStyle(),
            PROPERTY1,
            VALUE1
        );
    }

    @Test
    public void testGet2() {
        this.getAndCheck(
            this.createTextStyle(),
            PROPERTY2,
            VALUE2
        );
    }

    @Test
    public void testGetAll() {
        final TextStyleNonEmpty textStyle = this.createTextStyle();

        this.getAndCheck(
            textStyle,
            TextStylePropertyName.ALL,
            textStyle
        );
    }

    // set..............................................................................................................

    @Test
    public void testSetExistingPropertyAndValue() {
        this.setAndCheck(
            this.createTextStyle(),
            PROPERTY1,
            VALUE1
        );
    }

    @Test
    public void testSetExistingPropertyAndValue2() {
        this.setAndCheck(
            this.createTextStyle(),
            PROPERTY2,
            VALUE2
        );
    }

    @Test
    public void testSetAllWithEmpty() {
        this.setAndCheck(
            this.createTextStyle(),
            TextStylePropertyName.ALL,
            TextStyle.EMPTY,
            TextStyle.EMPTY
        );
    }

    @Test
    public void testSetAllWithNotEmpty() {
        final TextStyle textStyle = TextStyle.parse("text-align: left;");

        this.setAndCheck(
            this.createTextStyle(),
            TextStylePropertyName.ALL,
            textStyle,
            textStyle
        );
    }

    @Test
    public void testSetReplacePropertyAndValue() {
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;
        final WordWrap value1 = VALUE1;

        final TextStylePropertyName<FontFamily> property2 = PROPERTY2;
        final FontFamily value2 = VALUE2;

        final WordWrap different = WordWrap.NORMAL;
        assertNotSame(different, value1);

        this.setAndCheck(
            this.createTextStyle(
                property1,
                value1,
                property2,
                value2
            ),
            property1,
            different,
            this.createTextStyle(
                property1,
                different,
                property2,
                value2
            )
        );
    }

    @Test
    public void testSetReplacePropertyAndValue2() {
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;
        final WordWrap value1 = VALUE1;

        final TextStylePropertyName<FontFamily> property2 = PROPERTY2;
        final FontFamily value2 = VALUE2;

        final FontFamily different = FontFamily.with("different");
        assertNotSame(different, value2);

        this.setAndCheck(
            this.createTextStyle(
                property1,
                value1,
                property2,
                value2
            ),
            property2,
            different,
            this.createTextStyle(
                property1,
                value1,
                property2,
                different
            )
        );
    }

    @Test
    public void testSetNewPropertyAndValue1() {
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;
        final WordWrap value1 = VALUE1;

        final TextStylePropertyName<FontFamily> property2 = PROPERTY2;
        final FontFamily value2 = VALUE2;

        final TextStyle after = this.setAndCheck(
            this.createTextStyle(
                property1,
                value1
            ),
            property2,
            value2,
            this.createTextStyle(
                property1,
                value1,
                property2,
                value2
            )
        );

        this.checkEquals(
            2,
            after.value().size(),
            after::toString
        );
    }

    @Test
    public void testSetNewPropertyAndValue2() {
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;
        final WordWrap value1 = VALUE1;

        final TextStylePropertyName<FontFamily> property2 = PROPERTY2;
        final FontFamily value2 = VALUE2;

        final TextStylePropertyName<WritingMode> property3 = TextStylePropertyName.WRITING_MODE;
        final WritingMode value3 = WritingMode.VERTICAL_LR;

        this.setAndCheck(
            this.createTextStyle(
                property1,
                value1,
                property2,
                value2
            ),
            property3,
            value3,
            this.createTextStyle(
                property1,
                value1,
                property2,
                value2,
                property3,
                value3
            )
        );
    }

    @Test
    public void testSetNewPropertyAndValue3() {
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;
        final WordWrap value1 = VALUE1;

        final TextStylePropertyName<FontFamily> property2 = PROPERTY2;
        final FontFamily value2 = VALUE2;

        final TextStylePropertyName<Color> property3 = TextStylePropertyName.BACKGROUND_COLOR;
        final Color value3 = Color.fromRgb(0x123456);

        this.setAndCheck(
            this.createTextStyle(
                property1,
                value1,
                property2,
                value2
            ),
            property3,
            value3,
            this.createTextStyle(
                property3,
                value3,
                property1,
                value1,
                property2,
                value2
            )
        );
    }

    private <T> void setAndCheck(final TextStyle textStyle,
                                 final TextStylePropertyName<T> propertyName,
                                 final T value) {
        assertSame(
            textStyle,
            textStyle.set(propertyName, value),
            () -> textStyle + " set " + propertyName + " and " + CharSequences.quoteIfChars(value)
        );
    }

    // remove...........................................................................................................

    @Test
    public void testRemoveOnly() {
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;

        this.removeAndCheck(
            this.createTextStyle(
                property1,
                VALUE1
            ),
            property1,
            TextStyleEmpty.instance()
        );
    }

    @Test
    public void testRemove() {
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;

        final TextStylePropertyName<FontFamily> property2 = PROPERTY2;
        final FontFamily value2 = VALUE2;

        this.removeAndCheck(
            this.createTextStyle(
                property1,
                VALUE1,
                property2,
                value2
            ),
            property1,
            this.createTextStyle(
                property2,
                value2
            )
        );
    }

    @Test
    public void testRemove2() {
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;
        final WordWrap value1 = VALUE1;

        final TextStylePropertyName<FontFamily> property2 = PROPERTY2;

        this.removeAndCheck(this.createTextStyle(
            property1,
                value1,
                property2,
                VALUE2
            ),
            property2,
            this.createTextStyle(
                property1,
                value1
            )
        );
    }

    @Test
    public void testRemoveBecomesEmpty() {
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;
        final WordWrap value1 = VALUE1;

        this.removeAndCheck(
            this.createTextStyle(
                property1,
                value1
            ),
            property1,
            TextStyle.EMPTY
        );
    }

    @Test
    public void testRemoveBorder() {
        final TextStyle textStyle = TextStyle.parse(
            "border-top-color: BLACK; border-top-style: SOLID; border-top-width: 1px;" +
                "border-right-color: WHITE; border-right-style: DOTTED; border-right-width: 2px;" +
                "border-bottom-color: RED; border-bottom-style: DASHED; border-bottom-width: 3px;" +
                "border-left-color: GREEN; border-left-style: NONE; border-left-width: 4px;"
        );

        this.removeAndCheck(
            textStyle.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
            ),
            TextStylePropertyName.BORDER,
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
            )
        );
    }

    // set & remove ...................................................................................................

    @Test
    public void testSetSetRemoveRemove() {
        //set
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;
        final WordWrap value1 = VALUE1;
        final TextStyle textStyle1 = this.setAndCheck(
            TextStyle.EMPTY,
            property1,
            value1,
            this.createTextStyle(
                property1,
                value1
            )
        );

        //set
        final TextStylePropertyName<FontFamily> property2 = PROPERTY2;
        final FontFamily value2 = VALUE2;
        final TextStyle textStyle2 = this.setAndCheck(
            textStyle1,
            property2,
            value2,
            this.createTextStyle(
                property1,
                value1,
                property2,
                value2
            )
        );

        // remove1
        final TextStyle textStyle3 = this.removeAndCheck(
            textStyle2,
            property1,
            this.createTextStyle(
                property2,
                value2
            )
        );

        this.removeAndCheck(
            textStyle3,
            property2,
            TextStyle.EMPTY
        );
    }

    @Test
    public void testSetSetRemoveSet() {
        //set
        final TextStylePropertyName<WordWrap> property1 = PROPERTY1;
        final WordWrap value1 = VALUE1;
        final TextStyle textStyle1 = this.setAndCheck(
            TextStyle.EMPTY,
            property1,
            value1,
            this.createTextStyle(
                property1,
                value1
            )
        );

        //set
        final TextStylePropertyName<FontFamily> property2 = PROPERTY2;
        final FontFamily value2 = VALUE2;
        final TextStyle textStyle2 = this.setAndCheck(
            textStyle1,
            property2,
            value2,
            this.createTextStyle(
                property1,
                value1,
                property2,
                value2
            )
        );

        // remove1
        final TextStyle textStyle3 = this.removeAndCheck(
            textStyle2,
            property1,
            this.createTextStyle(
                property2,
                value2
            )
        );

        //set property1 again
        this.setAndCheck(
            textStyle3,
            property1,
            value1,
            this.createTextStyle(
                property1,
                value1,
                property2,
                value2
            )
        );
    }

    // setValues........................................................................................................

    @Test
    public void testSetValuesRemoves() {
        final Map<TextStylePropertyName<?>, Object> values = Maps.hash();
        values.put(
            TextStylePropertyName.COLOR,
            null
        );

        this.setValuesAndCheck(
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.BLACK
            ),
            values,
            TextStyle.EMPTY
        );
    }

    @Test
    public void testSetValuesRemovesAndReplaces() {
        final Map<TextStylePropertyName<?>, Object> values = Maps.hash();
        values.put(
            TextStylePropertyName.COLOR,
            null
        );
        values.put(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.CENTER
        );

        this.setValuesAndCheck(
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.BLACK
            ).set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
            ),
            values,
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.CENTER
            )
        );
    }

    // setBorder.......................................................................................................

    @Test
    public void testSetBorderAllSame() {
        final Color color = Color.parse("#123");
        final BorderStyle style = BorderStyle.DASHED;
        final Length<?> width = Length.pixel(123.5);

        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        ).set(TextStylePropertyName.BORDER_TOP_COLOR, color)
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
            .set(TextStylePropertyName.BORDER_LEFT_WIDTH, width);

        this.setBorderAndCheck(
            textStyle,
            BoxEdge.ALL.setBorder(
                Optional.of(color),
                Optional.of(style),
                Optional.of(width)
            )
        );
    }

    @Test
    public void testSetBorderAll() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        final Color color = Color.parse("#123");
        final BorderStyle style = BorderStyle.DASHED;
        final Length<?> width = Length.pixel(123.5);

        this.setBorderAndCheck(
            textStyle,
            Optional.of(
                BoxEdge.ALL.setBorder(
                    Optional.of(color),
                    Optional.of(style),
                    Optional.of(width)
                )
            ),
            textStyle.set(TextStylePropertyName.BORDER_TOP_COLOR, color)
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
                .set(TextStylePropertyName.BORDER_LEFT_WIDTH, width)
        );
    }

    @Test
    public void testSetBorderTopSame() {
        final Color color = Color.parse("#123");
        final BorderStyle style = BorderStyle.DASHED;
        final Length<?> width = Length.pixel(123.5);

        final TextStyle textStyle = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
            ).set(TextStylePropertyName.BORDER_TOP_COLOR, color)
            .set(TextStylePropertyName.BORDER_TOP_STYLE, style)
            .set(TextStylePropertyName.BORDER_TOP_WIDTH, width);

        this.setBorderAndCheck(
            textStyle,
            Optional.of(
                BoxEdge.TOP.setBorder(
                    Optional.of(color),
                    Optional.of(style),
                    Optional.of(width)
                )
            )
        );
    }

    @Test
    public void testSetBorderTop() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        final Color color = Color.parse("#123");
        final BorderStyle style = BorderStyle.DASHED;
        final Length<?> width = Length.pixel(123.5);

        this.setBorderAndCheck(
            textStyle,
            Optional.of(
                BoxEdge.TOP.setBorder(
                    Optional.of(color),
                    Optional.of(style),
                    Optional.of(width)
                )
            ),
            textStyle.set(TextStylePropertyName.BORDER_TOP_COLOR, color)
                .set(TextStylePropertyName.BORDER_TOP_STYLE, style)
                .set(TextStylePropertyName.BORDER_TOP_WIDTH, width)
        );
    }

    @Test
    public void testSetBorderAllSomeMissing() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        );

        final Color color = Color.BLACK;
        final BorderStyle style = BorderStyle.SOLID;
        final Length<?> width = Length.pixel(1.0);

        this.setBorderAndCheck(
            textStyle.set(TextStylePropertyName.BORDER_TOP_COLOR, color)
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
            Border.parse("border-top-color: WHITE; border-right-color: WHITE"),
            textStyle.set(
                TextStylePropertyName.BORDER_TOP_COLOR,
                Color.WHITE
            ).set(
                TextStylePropertyName.BORDER_RIGHT_COLOR,
                Color.WHITE
            )
        );
    }

    @Test
    public void testSetBorderTopSomeMissing() {
        final Color color = Color.BLACK;
        final BorderStyle style = BorderStyle.SOLID;
        final Length<?> width = Length.pixel(1.0);

        final TextStyle textStyle = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
            ).set(TextStylePropertyName.BORDER_TOP_COLOR, color)
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
            .set(TextStylePropertyName.BORDER_LEFT_WIDTH, width);

        this.setBorderAndCheck(
            textStyle,
            Border.parse("border-top-color: WHITE;")
                .setEdge(BoxEdge.TOP),
            textStyle.set(
                    TextStylePropertyName.BORDER_TOP_COLOR,
                    Color.WHITE
                ).remove(TextStylePropertyName.BORDER_TOP_STYLE)
                .remove(TextStylePropertyName.BORDER_TOP_WIDTH)
        );
    }

    // getBorder........................................................................................................

    @Test
    public void testBorderMissing() {
        this.getAndCheck(
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
            ),
            TextStylePropertyName.BORDER
        );
    }

    @Test
    public void testBorderAll() {
        final String text = "border-top-color: BLACK; border-top-style: SOLID; border-top-width: 1px;" +
            "border-right-color: WHITE; border-right-style: DOTTED; border-right-width: 2px;" +
            "border-bottom-color: RED; border-bottom-style: DASHED; border-bottom-width: 3px;" +
            "border-left-color: GREEN; border-left-style: NONE; border-left-width: 4px;";

        this.getAndCheck(
            TextStyle.parse(text),
            TextStylePropertyName.BORDER,
            Border.parse(text)
        );
    }

    @Test
    public void testBorderAll2() {
        final String text = "border-top-color: BLACK; border-top-style: SOLID; border-top-width: 1px;" +
            "border-right-color: WHITE; border-right-style: DOTTED; border-right-width: 2px;" +
            "border-bottom-color: RED; border-bottom-style: DASHED; border-bottom-width: 3px;" +
            "border-left-color: GREEN; border-left-style: NONE; border-left-width: 4px;";

        this.getAndCheck(
            TextStyle.parse(text + " color: BLUE;"),
            TextStylePropertyName.BORDER,
            Border.parse(text)
        );
    }

    @Test
    public void testBorderIncomplete() {
        final String text = "border-top-color: BLACK; border-right-style: SOLID; border-bottom-width: 1px;";

        this.getAndCheck(
            TextStyle.parse(text),
            TextStylePropertyName.BORDER,
            Border.parse(text)
        );
    }

    @Test
    public void testBorderIncomplete2() {
        final String text = "border-top-color: BLACK; border-right-style: SOLID; border-bottom-width: 1px;";

        this.getAndCheck(
            TextStyle.parse(text + " color: BLUE;"),
            TextStylePropertyName.BORDER,
            Border.parse(text)
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
            TextStylePropertyName.BORDER_COLOR
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
            TextStylePropertyName.BORDER_COLOR
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
            TextStylePropertyName.BORDER_STYLE
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
            TextStylePropertyName.BORDER_STYLE
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
            TextStylePropertyName.BORDER_WIDTH
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
            TextStylePropertyName.BORDER_WIDTH
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

    // border...........................................................................................................

    @Test
    public void testBorderWithAllNoneFiltered() {
        this.borderAndCheck(
            "border-top-color: black; border-top-style: solid; border-left-width: 1px;",
            BoxEdge.ALL
        );
    }

    @Test
    public void testBorderWithAllSomeFiltered() {
        final String style = "border-top-color: black; border-top-style: solid; border-left-width: 1px;";

        this.borderAndCheck(
            style + "color: white; margin-left: 1px; padding-left: 2px;",
            BoxEdge.ALL,
            style
        );
    }

    @Test
    public void testBorderWithTop() {
        this.borderAndCheck(
            "border-top-color: white; border-right-color: black;",
            BoxEdge.TOP,
            "border-top-color: white;"
        );
    }

    @Test
    public void testBorderWithRight() {
        final String style = "border-right-color: white; border-right-width: 1px; border-right-style: solid;";

        this.borderAndCheck(
            "border-top-color: black; color: black;" + style,
            BoxEdge.RIGHT,
            style
        );
    }

    @Test
    public void testBorderWithBottom() {
        this.borderAndCheck(
            "border-top-color: black; border-bottom-color: white; color: black;",
            BoxEdge.BOTTOM,
            "border-bottom-color: white;"
        );
    }

    @Test
    public void testBorderWithLeft() {
        this.borderAndCheck(
            "border-left-color: white; border-bottom-color: white; color: black; text-align: left",
            BoxEdge.LEFT,
            "border-left-color: white;"
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
            TextStylePropertyName.MARGIN
        );
    }

    @Test
    public void testMarginGet() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
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
        );

        this.getAndCheck(
            textStyle.set(
                TextStylePropertyName.COLOR,
                Color.BLACK
            ),
            TextStylePropertyName.MARGIN,
            BoxEdge.ALL.margin(textStyle)
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
            BoxEdge.ALL.setMargin(
                Optional.of(width)
            ),
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
            TextStylePropertyName.PADDING
        );
    }

    @Test
    public void testPaddingGet() {
        final TextStyle textStyle = TextStyle.EMPTY.set(
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
        );

        this.getAndCheck(
            textStyle.set(
                TextStylePropertyName.COLOR,
                Color.BLACK
            ),
            TextStylePropertyName.PADDING,
            BoxEdge.ALL.padding(textStyle)
        );
    }

    // setPadding.......................................................................................................

    @Test
    public void testPaddingSet() {
        final Padding padding = Padding.parse("1px 2px 3px 4px");

        this.setAndCheck(
            padding.textStyle()
                .set(
                    TextStylePropertyName.TEXT_ALIGN,
                    TextAlign.LEFT
                ),
            TextStylePropertyName.PADDING,
            padding,
            TextStyle.parse("padding-top: 1px; padding-right: 2px; padding-bottom: 3px; padding-left: 4px; text-align: left")
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
                    BoxEdge.ALL.setMargin(
                        Optional.of(width)
                    )
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

        this.checkEquals(
            notEmpty.setValues(
                Maps.of(
                    TextStylePropertyName.PADDING_TOP,
                    Length.pixel(1.0),
                    TextStylePropertyName.PADDING_RIGHT,
                    Length.pixel(2.0),
                    TextStylePropertyName.PADDING_BOTTOM,
                    Length.pixel(3.0),
                    TextStylePropertyName.PADDING_LEFT,
                    Length.pixel(4.0)
                )
            ),
            notEmpty.setValues(
                Maps.of(
                    TextStylePropertyName.PADDING,
                    Padding.parse("1px 2px 3px 4px")
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
            "color: #123456;"
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
            "color: #123456; text-align: LEFT;"
        );
    }

    @Test
    public void testTextIncludesWebColorNameColor() {
        this.textAndCheck(
            TextStyle.EMPTY.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.parse("#123456")
            ).set(
                TextStylePropertyName.COLOR,
                WebColorName.RED.color()
            ).set(
                TextStylePropertyName.TEXT_ALIGN, TextAlign.LEFT
            ),
            "background-color: #123456; color: red; text-align: LEFT;"
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

    @Test
    public void testTreePrintIncludingColorWithWebColorNames() {
        this.treePrintAndCheck(
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.BLACK
            ).set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.parse("#123456")
            ),
            "TextStyle\n" +
                "  background-color=#123456 (walkingkooka.color.OpaqueRgbColor)\n" +
                "  color=black (walkingkooka.color.OpaqueRgbColor)\n"
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

    // HasProperties....................................................................................................

    @Test
    public void testPropertiesWithColor() {
        this.propertiesAndCheck2(
            TextStylePropertyName.COLOR,
            Color.BLACK,
            "color: black\r\n"
        );
    }

    @Test
    public void testPropertiesWithEnum() {
        this.propertiesAndCheck2(
            TextStylePropertyName.BORDER_COLLAPSE,
            BorderCollapse.COLLAPSE,
            "border-collapse: COLLAPSE\r\n"
        );
    }

    @Test
    public void testPropertiesWithFontFamily() {
        this.propertiesAndCheck2(
            TextStylePropertyName.FONT_FAMILY,
            FontFamily.with("Times New Roman"),
            "font-family: Times New Roman\r\n"
        );
    }

    @Test
    public void testPropertiesWithFontSize() {
        this.propertiesAndCheck2(
            TextStylePropertyName.FONT_SIZE,
            FontSize.with(10),
            "font-size: 10\r\n"
        );
    }

    @Test
    public void testPropertiesWithFontWeight() {
        this.propertiesAndCheck2(
            TextStylePropertyName.FONT_WEIGHT,
            FontWeight.with(123),
            "font-weight: 123\r\n"
        );
    }

    @Test
    public void testPropertiesWithFontWeightBold() {
        this.propertiesAndCheck2(
            TextStylePropertyName.FONT_WEIGHT,
            FontWeight.BOLD,
            "font-weight: BOLD\r\n"
        );
    }

    @Test
    public void testPropertiesWithLengthNumberNone() {
        this.propertiesAndCheck2(
            TextStylePropertyName.BORDER_BOTTOM_WIDTH,
            Length.none(),
            "border-bottom-width: none\r\n"
        );
    }

    @Test
    public void testPropertiesWithLengthNumberNormal() {
        this.propertiesAndCheck2(
            TextStylePropertyName.BORDER_SPACING,
            Length.normal(),
            "border-spacing: normal\r\n"
        );
    }

    @Test
    public void testPropertiesWithLengthNumberPixel() {
        this.propertiesAndCheck2(
            TextStylePropertyName.BORDER_BOTTOM_WIDTH,
            Length.pixel(123.5),
            "border-bottom-width: 123.5px\r\n"
        );
    }

    @Test
    public void testPropertiesWithOpacity() {
        this.propertiesAndCheck2(
            TextStylePropertyName.OPACITY,
            Opacity.with(0.5),
            "opacity: 50%\r\n"
        );
    }

    @Test
    public void testPropertiesWithOpacityOpaque() {
        this.propertiesAndCheck2(
            TextStylePropertyName.OPACITY,
            Opacity.OPAQUE,
            "opacity: opaque\r\n"
        );
    }

    @Test
    public void testPropertiesWithTextOverflowClip() {
        this.propertiesAndCheck2(
            TextStylePropertyName.TEXT_OVERFLOW,
            TextOverflow.CLIP,
            "text-overflow: clip\r\n"
        );
    }

    @Test
    public void testPropertiesWithTextOverflowString() {
        this.propertiesAndCheck2(
            TextStylePropertyName.TEXT_OVERFLOW,
            TextOverflow.string("Hello"),
            "text-overflow: \"Hello\"\r\n"
        );
    }

    @Test
    public void testPropertiesWithOpacityTransparent() {
        this.propertiesAndCheck2(
            TextStylePropertyName.OPACITY,
            Opacity.TRANSPARENT,
            "opacity: transparent\r\n"
        );
    }

    private <T> void propertiesAndCheck2(final TextStylePropertyName<T> propertyName,
                                         final T value,
                                         final String expected) {
        this.propertiesAndCheck2(
            propertyName,
            value,
            Properties.parse(expected)
        );
    }

    private <T> void propertiesAndCheck2(final TextStylePropertyName<T> propertyName,
                                         final T value,
                                         final Properties expected) {
        this.propertiesAndCheck(
            TextStyleNonEmpty.EMPTY.set(
                propertyName,
                value
            ),
            expected
        );
    }

    @Test
    public void testPropertiesWithBorder() {
        this.propertiesAndCheck(
            TextStyleNonEmpty.EMPTY.setBorder(
                Optional.of(
                    Border.parse("WHITE DASHED 34.0px")
                )
            ),
            "border-bottom-color=white\n" +
                "border-bottom-style=DASHED\n" +
                "border-bottom-width=34px\n" +
                "border-left-color=white\n" +
                "border-left-style=DASHED\n" +
                "border-left-width=34px\n" +
                "border-right-color=white\n" +
                "border-right-style=DASHED\n" +
                "border-right-width=34px\n" +
                "border-top-color=white\n" +
                "border-top-style=DASHED\n" +
                "border-top-width=34px"
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(PROPERTY1, VALUE1);
        map.put(PROPERTY2, VALUE2);

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
        return this.createTextStyle(
            PROPERTY1,
            VALUE1, 
            PROPERTY2, 
            VALUE2
        );
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
        return TextStyleNonEmpty.with(
            TextStylePropertiesMap.with(map)
        );
    }

    // class............................................................................................................

    @Override
    Class<TextStyleNonEmpty> textStyleType() {
        return TextStyleNonEmpty.class;
    }
}
