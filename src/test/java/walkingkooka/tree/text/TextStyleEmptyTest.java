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
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;
import walkingkooka.visit.Visiting;

import java.math.MathContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextStyleEmptyTest extends TextStyleTestCase<TextStyleEmpty> {

    @Test
    public void testValue() {
        assertSame(TextStyle.EMPTY.value(), TextStyle.EMPTY.value());
    }

    // merge............................................................................................................

    @Test
    public void testMergeNotEmpty() {
        final TextStyle notEmpty = TextStyle.EMPTY.set(
                TextStylePropertyName.FONT_STYLE,
                FontStyle.ITALIC
        );
        assertSame(
                notEmpty,
                TextStyle.EMPTY.merge(notEmpty)
        );
    }

    // replace...........................................................................................................

    @Test
    public void testReplaceTextPlaceholderNode() {
        this.replaceAndCheck2(this.placeholder("place1"));
    }

    @Test
    public void testReplaceTextPlaceholderNodeWithParent() {
        this.replaceAndCheck2(makeStyleNameParent(this.placeholder("child-place1")));
    }

    @Test
    public void testReplaceTextStyle() {
        this.replaceAndCheck2(TextNode.style(this.children()));
    }

    @Test
    public void testReplaceTextStyle2() {
        final TextNode node = TextNode.style(children());

        this.replaceAndCheck(TextStyle.EMPTY,
                node.setAttributes(Maps.of(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC)),
                node);
    }

    @Test
    public void testReplaceTextStyleNodeWithParent() {
        final TextNode textStyleNode = TextNode.style(this.children());

        this.replaceAndCheck(TextStyle.EMPTY,
                makeStyleNameParent(textStyleNode.setAttributes(Maps.of(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC))),
                makeStyleNameParent(textStyleNode));
    }

    @Test
    public void testReplaceTextStyleName() {
        this.replaceAndCheck2(TextNode.styleName(TextStyleName.with("style123")));
    }

    @Test
    public void testReplaceTextStyleWithParent() {
        final TextStyleNameNode styled = makeStyleNameParent(this.styleName("child-style-456"));

        this.replaceAndCheck2(styled);
    }

    @Test
    public void testReplaceText() {
        this.replaceAndCheck2(TextNode.text("abc123"));
    }

    @Test
    public void testReplaceTextWithParent() {
        this.replaceAndCheck2(makeStyleNameParent(TextNode.text("child-abc123")));
    }

    private void replaceAndCheck2(final TextNode textNode) {
        this.replaceAndCheck(TextStyle.EMPTY, textNode);
    }

    // set..............................................................................................................

    @Test
    public void testSet() {
        final TextStylePropertyName<FontFamily> propertyName = TextStylePropertyName.FONT_FAMILY;
        final FontFamily familyName = FontFamily.with("Antiqua");

        this.setAndCheck(
                TextStyle.EMPTY,
                propertyName,
                familyName,
                TextStyle.EMPTY.setValues(
                        Maps.of(
                                propertyName,
                                familyName
                        )
                )
        );
    }

    // setBorder.......................................................................................................

    @Test
    public void testSetBorder() {
        final Color color = Color.parse("#123");
        final BorderStyle style = BorderStyle.DASHED;
        final Length<?> width = Length.pixel(123.5);

        this.checkEquals(
                TextStyle.EMPTY
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
                TextStyle.EMPTY.setBorder(
                        color,
                        style,
                        width
                )
        );
    }
    
    // setMargin.......................................................................................................

    @Test
    public void testSetMargin() {
        final Length<?> length = Length.pixel(123.5);

        this.checkEquals(
                TextStyleNonEmpty.with(
                        TextNodeMap.with(
                                Maps.of(
                                        TextStylePropertyName.MARGIN_TOP, length,
                                        TextStylePropertyName.MARGIN_RIGHT, length,
                                        TextStylePropertyName.MARGIN_BOTTOM, length,
                                        TextStylePropertyName.MARGIN_LEFT, length
                                )
                        )
                ),
                TextStyle.EMPTY.setMargin(length)
        );
    }
    
    // setPadding.......................................................................................................

    @Test
    public void testSetPadding() {
        final Length<?> length = Length.pixel(123.5);

        this.checkEquals(
                TextStyleNonEmpty.with(
                        TextNodeMap.with(
                                Maps.of(
                                        TextStylePropertyName.PADDING_TOP, length,
                                        TextStylePropertyName.PADDING_RIGHT, length,
                                        TextStylePropertyName.PADDING_BOTTOM, length,
                                        TextStylePropertyName.PADDING_LEFT, length
                                )
                        )
                ),
                TextStyle.EMPTY.setPadding(length)
        );
    }

    // getBorderColor...................................................................................................

    @Test
    public void testBorderColorGet() {
        this.getAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.BORDER_COLOR,
                null
        );
    }

    // setBorderColor...................................................................................................

    @Test
    public void testBorderColorSet() {
        final Color color = Color.WHITE;

        this.setAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.BORDER_COLOR,
                color,
                TextStyle.EMPTY.setValues(
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

    // removeBorderColor...................................................................................................

    @Test
    public void testBorderColorRemove() {
        this.removeAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.BORDER_COLOR,
                TextStyle.EMPTY
        );
    }

    // getBorderStyle...................................................................................................

    @Test
    public void testBorderStyleGet() {
        this.getAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.BORDER_STYLE,
                null
        );
    }

    // setBorderStyle...................................................................................................

    @Test
    public void testBorderStyleSet() {
        final BorderStyle style = BorderStyle.DOUBLE;

        this.setAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.BORDER_STYLE,
                style,
                TextStyle.EMPTY.setValues(
                        Maps.of(
                                TextStylePropertyName.BORDER_TOP_STYLE,
                                style,
                                TextStylePropertyName.BORDER_LEFT_STYLE,
                                style,
                                TextStylePropertyName.BORDER_RIGHT_STYLE,
                                style,
                                TextStylePropertyName.BORDER_BOTTOM_STYLE,
                                style
                        )
                )
        );
    }

    // removeBorderStyle...................................................................................................

    @Test
    public void testBorderStyleRemove() {
        this.removeAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.BORDER_STYLE,
                TextStyle.EMPTY
        );
    }

    // getBorderWidth...................................................................................................

    @Test
    public void testBorderWidthGet() {
        this.getAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.BORDER_WIDTH,
                null
        );
    }

    // setBorderWidth...................................................................................................

    @Test
    public void testBorderWidthSet() {
        final Length<?> width = Length.pixel(123.0);

        this.setAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.BORDER_WIDTH,
                width,
                TextStyle.EMPTY.setValues(
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

    // removeBorderWidth...................................................................................................

    @Test
    public void testBorderWidthRemove() {
        this.removeAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.BORDER_WIDTH,
                TextStyle.EMPTY
        );
    }

    // getMargin...................................................................................................

    @Test
    public void testMarginGet() {
        this.getAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.MARGIN,
                null
        );
    }

    // setMargin...................................................................................................

    @Test
    public void testMarginSet() {
        final Length<?> length = Length.pixel(100.0);

        this.setAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.MARGIN,
                length,
                TextStyle.EMPTY.setValues(
                        Maps.of(
                                TextStylePropertyName.MARGIN_TOP,
                                length,
                                TextStylePropertyName.MARGIN_LEFT,
                                length,
                                TextStylePropertyName.MARGIN_RIGHT,
                                length,
                                TextStylePropertyName.MARGIN_BOTTOM,
                                length
                        )
                )
        );
    }

    // removeMargin...................................................................................................

    @Test
    public void testMarginRemove() {
        this.removeAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.MARGIN,
                TextStyle.EMPTY
        );
    }

    // getPadding.......................................................................................................

    @Test
    public void testPaddingGet() {
        this.getAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.PADDING,
                null
        );
    }

    // setPadding.......................................................................................................

    @Test
    public void testPaddingSet() {
        final Length<?> length = Length.pixel(100.0);

        this.setAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.PADDING,
                length,
                TextStyle.EMPTY.setValues(
                        Maps.of(
                                TextStylePropertyName.PADDING_TOP,
                                length,
                                TextStylePropertyName.PADDING_LEFT,
                                length,
                                TextStylePropertyName.PADDING_RIGHT,
                                length,
                                TextStylePropertyName.PADDING_BOTTOM,
                                length
                        )
                )
        );
    }

    // removePadding....................................................................................................

    @Test
    public void testPaddingRemove() {
        this.removeAndCheck(
                TextStyle.EMPTY,
                TextStylePropertyName.PADDING,
                TextStyle.EMPTY
        );
    }
    
    // TextStyleVisitor.................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<TextStyle> visited = Lists.array();

        final TextStyleEmpty textStyle = TextStyleEmpty.instance();

        new FakeTextStyleVisitor() {
            @Override
            protected Visiting startVisit(final TextStyle n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextStyle n) {
                b.append("2");
                visited.add(n);
            }
        }.accept(textStyle);

        this.checkEquals("12", b.toString());
        this.checkEquals(Lists.of(textStyle, textStyle),
                visited,
                "visited");
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(TextStyleEmpty.instance(), "");
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(TextStyle.EMPTY, "");
    }

    @Test
    public void testUnmarshallEmptyJsonObject() {
        assertSame(
                TextStyle.EMPTY,
                TextStyle.unmarshall(
                        JsonNode.object(),
                        JsonNodeUnmarshallContexts.basic(
                                ExpressionNumberKind.DEFAULT,
                                MathContext.DECIMAL32
                        )
                )
        );
    }

    @Override
    public TextStyleEmpty createObject() {
        return Cast.to(TextStyle.EMPTY);
    }

    @Override
    Class<TextStyleEmpty> textStyleType() {
        return TextStyleEmpty.class;
    }
}
