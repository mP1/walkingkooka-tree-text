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
import walkingkooka.Cast;
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
    public void testWithTextStyleMap() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        final TextNodeMap textStyleMap = TextNodeMap.with(map);

        final TextStyleNonEmpty textStyle = this.createTextStyle(textStyleMap);
        assertSame(textStyleMap, textStyle.value(), "value");
    }

    @Test
    public void testWithMapCopied() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final Map<TextStylePropertyName<?>, Object> copy = Maps.sorted();
        copy.putAll(map);

        final TextStyleNonEmpty textStyle = this.createTextStyle(map);

        map.clear();
        this.checkEquals(copy, textStyle.value(), "value");
    }

    @Test
    public void testEmpty() {
        assertSame(TextNodeMap.EMPTY, TextNodeMap.with(Maps.empty()));
    }

    @Test
    public void testValue() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final TextStyleNonEmpty textStyle = this.createTextStyle(map);
        this.checkEquals(TextNodeMap.class, textStyle.value().getClass(), () -> "" + textStyle.value);
    }

    // merge............................................................................................................

    @Test
    public void testMergeNotEmptySubset() {
        this.mergeAndCheck(
                TextStyle.with(Maps.of(this.property1(), this.value1(), this.property2(), this.value2())),
                TextStyle.with(Maps.of(this.property1(), this.value1())));
    }

    @Test
    public void testMergeNotEmptyCombined() {
        this.mergeAndCheck(
                TextStyle.with(Maps.of(this.property1(), this.value1(), this.property2(), this.value2())),
                TextStyle.with(Maps.of(this.property1(), this.value1(), this.property3(), this.value3())));
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

        this.replaceAndCheck(textStyle,
                this.makeStyleNameParent(styleName),
                this.makeStyleNameParent(styleName.setAttributes(textStyle.value()).parentOrFail()).children().get(0));
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

        this.replaceAndCheck(textStyle,
                this.makeStyleNameParent(textNode),
                this.makeStyleNameParent(TextStyleNode.with(Lists.of(textNode), textStyle.textStyleMap())).children().get(0));
    }

    private void replaceAndCheck2(final TextNode textNode) {
        final TextStyle textStyle = this.createTextStyle();

        this.replaceAndCheck(textStyle,
                textNode,
                textNode.setAttributes(textStyle.value()).parentOrFail().children().get(0));
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

    // setMargin.......................................................................................................

    @Test
    public void testSetMargin() {
        final Color color = Color.parse("#123");
        final Length<?> length = Length.pixel(123.5);

        this.checkEquals(
                TextStyle.withTextStyleMap(
                        TextNodeMap.with(
                                Maps.of(
                                        TextStylePropertyName.COLOR, color,
                                        TextStylePropertyName.MARGIN_TOP, length,
                                        TextStylePropertyName.MARGIN_RIGHT, length,
                                        TextStylePropertyName.MARGIN_BOTTOM, length,
                                        TextStylePropertyName.MARGIN_LEFT, length
                                )
                        )
                ),
                TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR, color
                ).setMargin(length)
        );
    }

    @Test
    public void testSetMarginSame() {
        final Color color = Color.parse("#123");
        final Length<?> length = Length.pixel(123.5);
        final TextStyle style = TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR, color
        ).setMargin(length);

        assertSame(
                style,
                style.setMargin(length)
        );
    }
    
    // setPadding.......................................................................................................

    @Test
    public void testSetPadding() {
        final Color color = Color.parse("#123");
        final Length<?> length = Length.pixel(123.5);

        this.checkEquals(
                TextStyle.withTextStyleMap(
                        TextNodeMap.with(
                                Maps.of(
                                        TextStylePropertyName.COLOR, color,
                                        TextStylePropertyName.PADDING_TOP, length,
                                        TextStylePropertyName.PADDING_RIGHT, length,
                                        TextStylePropertyName.PADDING_BOTTOM, length,
                                        TextStylePropertyName.PADDING_LEFT, length
                                )
                        )
                ),
                TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR, color
                ).setPadding(length)
        );
    }

    @Test
    public void testSetPaddingSame() {
        final Color color = Color.parse("#123");
        final Length<?> length = Length.pixel(123.5);
        final TextStyle style = TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR, color
        ).setPadding(length);

        assertSame(
                style,
                style.setPadding(length)
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

        this.toStringAndCheck(TextStyleNonEmpty.with(map), map.toString());
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
        return Cast.to(TextStyle.with(map));
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
