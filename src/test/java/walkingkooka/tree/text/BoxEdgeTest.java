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
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BoxEdgeTest implements ClassTesting2<BoxEdge> {

    @Test
    public void testBorderColorPropertyName() {
        this.checkEquals(
            Lists.empty(),
            Arrays.stream(BoxEdge.values())
                .filter(v -> v != BoxEdge.ALL)
                .flatMap(b -> {
                    final String name = b.borderColorPropertyName()
                        .value();
                    final String boxEdgeName = "border-" +
                        b.name()
                            .toLowerCase() +
                        "-color";
                    return Optional.ofNullable(
                        name.equals(boxEdgeName) ?
                            null :
                            b + " " + boxEdgeName + "!=" + name
                    ).stream();
                }).collect(Collectors.toList())
        );
    }

    @Test
    public void testBorderStylePropertyName() {
        this.checkEquals(
            Lists.empty(),
            Arrays.stream(BoxEdge.values())
                .filter(v -> v != BoxEdge.ALL)
                .flatMap(b -> {
                    final String name = b.borderStylePropertyName()
                        .value();
                    final String boxEdgeName = "border-" +
                        b.name()
                            .toLowerCase() +
                        "-style";
                    return Optional.ofNullable(
                        name.equals(boxEdgeName) ?
                            null :
                            b + " " + boxEdgeName + "!=" + name
                    ).stream();
                }).collect(Collectors.toList())
        );
    }

    @Test
    public void testBorderWidthPropertyName() {
        this.checkEquals(
            Lists.empty(),
            Arrays.stream(BoxEdge.values())
                .filter(v -> v != BoxEdge.ALL)
                .flatMap(b -> {
                    final String name = b.borderWidthPropertyName()
                        .value();
                    final String boxEdgeName = "border-" +
                        b.name()
                            .toLowerCase() +
                        "-width";
                    return Optional.ofNullable(
                        name.equals(boxEdgeName) ?
                            null :
                            b + " " + boxEdgeName + "!=" + name
                    ).stream();
                }).collect(Collectors.toList())
        );
    }

    @Test
    public void testMarginPropertyName() {
        this.checkEquals(
            Lists.empty(),
            Arrays.stream(BoxEdge.values())
                .filter(v -> v != BoxEdge.ALL)
                .flatMap(b -> {
                    final String name = b.marginPropertyName()
                        .value();
                    final String boxEdgeName = "margin-" +
                        b.name()
                            .toLowerCase();
                    return Optional.ofNullable(
                        name.equals(boxEdgeName) ?
                            null :
                            b + " " + boxEdgeName + "!=" + name
                    ).stream();
                }).collect(Collectors.toList())
        );
    }

    @Test
    public void testPaddingPropertyName() {
        this.checkEquals(
            Lists.empty(),
            Arrays.stream(BoxEdge.values())
                .filter(v -> v != BoxEdge.ALL)
                .flatMap(b -> {
                    final String name = b.paddingPropertyName()
                        .value();
                    final String boxEdgeName = "padding-" +
                        b.name()
                            .toLowerCase();
                    return Optional.ofNullable(
                        name.equals(boxEdgeName) ?
                            null :
                            b + " " + boxEdgeName + "!=" + name
                    ).stream();
                }).collect(Collectors.toList())
        );
    }

    @Test
    public void testFlipBottom() {
        this.flipAndCheck(BoxEdge.BOTTOM, BoxEdge.TOP);
    }

    @Test
    public void testFlipLeft() {
        this.flipAndCheck(BoxEdge.LEFT, BoxEdge.RIGHT);
    }

    @Test
    public void testFlipRight() {
        this.flipAndCheck(BoxEdge.RIGHT, BoxEdge.LEFT);
    }

    @Test
    public void testFlipTop() {
        this.flipAndCheck(BoxEdge.TOP, BoxEdge.BOTTOM);
    }

    private void flipAndCheck(final BoxEdge edge, final BoxEdge expected) {
        assertSame(expected, edge.flip(), () -> edge + " flip");
    }

    // setBorder........................................................................................................

    @Test
    public void testSetBorderWithNullColorAllFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.ALL.setBorder(
                null,
                Optional.empty(), // style
                Optional.empty() // width
            )
        );
    }

    @Test
    public void testSetBorderWithNullColorLeftFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.LEFT.setBorder(
                null,
                Optional.empty(), // style
                Optional.empty() // width
            )
        );
    }

    @Test
    public void testSetBorderWithNullStyleAllFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.ALL.setBorder(
                Optional.empty(), // color
                null,
                Optional.empty() // width
            )
        );
    }

    @Test
    public void testSetBorderWithNullStyleRightFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.RIGHT.setBorder(
                Optional.empty(), // color
                null,
                Optional.empty() // width
            )
        );
    }

    @Test
    public void testSetBorderWithNullWidthAllFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.ALL.setBorder(
                Optional.empty(), // color
                Optional.empty(), // style
                null
            )
        );
    }

    @Test
    public void testSetBorderWithNullWidthTopFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.TOP.setBorder(
                Optional.empty(), // color
                Optional.empty(), // style
                null
            )
        );
    }

    @Test
    public void testSetBorderWithAll() {
        this.setBorderAndCheck(
            BoxEdge.ALL,
            Color.BLACK,
            BorderStyle.DASHED,
            Length.pixel(2.0),
            "top-color: black; top-style: dashed; top-width: 2.0px; right-color: black; right-style: dashed; right-width: 2.0px;" +
                "bottom-color: black; bottom-style: dashed; bottom-width: 2.0px; left-color: black; left-style: dashed; left-width: 2.0px;"
        );
    }

    @Test
    public void testSetBorderWithTop() {
        this.setBorderAndCheck(
            BoxEdge.TOP,
            Color.BLACK,
            BorderStyle.DASHED,
            Length.pixel(2.0),
            "top-color: black; top-style: dashed; top-width: 2.0px;"
        );
    }

    @Test
    public void testSetBorderWithLeft() {
        this.setBorderAndCheck(
            BoxEdge.LEFT,
            Color.BLACK,
            BorderStyle.DASHED,
            Length.pixel(2.0),
            "left-color: black; left-style: dashed; left-width: 2.0px;"
        );
    }

    private void setBorderAndCheck(final BoxEdge edge,
                                   final Color color,
                                   final BorderStyle style,
                                   final Length<?> width,
                                   final String expected) {
        this.setBorderAndCheck(
            edge,
            Optional.of(color),
            Optional.of(style),
            Optional.of(width),
            expected
        );
    }

    private void setBorderAndCheck(final BoxEdge edge,
                                   final Optional<Color> color,
                                   final Optional<BorderStyle> style,
                                   final Optional<Length<?>> width,
                                   final String expected) {
        this.setBorderAndCheck(
            edge,
            color,
            style,
            width,
            Border.parse(expected)
                .setEdge(edge)
        );
    }

    private void setBorderAndCheck(final BoxEdge edge,
                                   final Color color,
                                   final BorderStyle style,
                                   final Length<?> width,
                                   final Border expected) {
        this.setBorderAndCheck(
            edge,
            Optional.of(color),
            Optional.of(style),
            Optional.of(width),
            expected
        );
    }

    private void setBorderAndCheck(final BoxEdge edge,
                                   final Optional<Color> color,
                                   final Optional<BorderStyle> style,
                                   final Optional<Length<?>> width,
                                   final Border expected) {
        this.checkEquals(
            expected,
            edge.setBorder(
                color,
                style,
                width
            ),
            () -> edge + " setBorder " + color + " " + style + " " + width
        );
    }

    // setMargin........................................................................................................

    @Test
    public void testSetMarginWithNullWidthAllFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.ALL.setMargin(
                null
            )
        );
    }

    @Test
    public void testSetMarginWithNullWidthTopFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.TOP.setMargin(
                null
            )
        );
    }

    @Test
    public void testSetMarginWithAll() {
        this.setMarginAndCheck(
            BoxEdge.ALL,
            Length.pixel(2.0),
            "top: 2.0px; right: 2.0px; bottom: 2.0px; left: 2.0px;"
        );
    }

    @Test
    public void testSetMarginWithTop() {
        this.setMarginAndCheck(
            BoxEdge.TOP,
            Length.pixel(2.0),
            "top: 2.0px;"
        );
    }

    @Test
    public void testSetMarginWithTopAndEmptyWidth() {
        this.setMarginAndCheck(
            BoxEdge.TOP,
            Optional.empty(),
            ""
        );
    }

    @Test
    public void testSetMarginWithLeft() {
        this.setMarginAndCheck(
            BoxEdge.LEFT,
            Length.pixel(2.0),
            "left: 2.0px;"
        );
    }

    private void setMarginAndCheck(final BoxEdge edge,
                                   final Length<?> width,
                                   final String expected) {
        this.setMarginAndCheck(
            edge,
            Optional.of(width),
            expected
        );
    }

    private void setMarginAndCheck(final BoxEdge edge,
                                   final Optional<Length<?>> width,
                                   final String expected) {
        this.setMarginAndCheck(
            edge,
            width,
            Margin.parse(expected)
                .setEdge(edge)
        );
    }

    private void setMarginAndCheck(final BoxEdge edge,
                                   final Length<?> width,
                                   final Margin expected) {
        this.setMarginAndCheck(
            edge,
            Optional.of(width),
            expected
        );
    }

    private void setMarginAndCheck(final BoxEdge edge,
                                   final Optional<Length<?>> width,
                                   final Margin expected) {
        this.checkEquals(
            expected,
            edge.setMargin(width),
            () -> edge + " setMargin " + width
        );
    }

    // setPadding.......................................................................................................

    @Test
    public void testSetPaddingWithNullWidthAllFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.ALL.setPadding(
                null
            )
        );
    }

    @Test
    public void testSetPaddingWithNullWidthTopFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.TOP.setPadding(
                null
            )
        );
    }

    @Test
    public void testSetPaddingWithAll() {
        this.setPaddingAndCheck(
            BoxEdge.ALL,
            Length.pixel(2.0),
            "top: 2.0px; right: 2.0px; bottom: 2.0px; left: 2.0px;"
        );
    }

    @Test
    public void testSetPaddingWithTop() {
        this.setPaddingAndCheck(
            BoxEdge.TOP,
            Length.pixel(2.0),
            "top: 2.0px;"
        );
    }

    @Test
    public void testSetPaddingWithLeft() {
        this.setPaddingAndCheck(
            BoxEdge.LEFT,
            Length.pixel(2.0),
            "left: 2.0px;"
        );
    }

    private void setPaddingAndCheck(final BoxEdge edge,
                                    final Length<?> width,
                                    final String expected) {
        this.setPaddingAndCheck(
            edge,
            Optional.of(width),
            expected
        );
    }

    private void setPaddingAndCheck(final BoxEdge edge,
                                    final Optional<Length<?>> width,
                                    final String expected) {
        this.setPaddingAndCheck(
            edge,
            width,
            Padding.parse(expected)
                .setEdge(edge)
        );
    }

    private void setPaddingAndCheck(final BoxEdge edge,
                                    final Length<?> width,
                                    final Padding expected) {
        this.setPaddingAndCheck(
            edge,
            Optional.of(width),
            expected
        );
    }

    private void setPaddingAndCheck(final BoxEdge edge,
                                    final Optional<Length<?>> width,
                                    final Padding expected) {
        this.checkEquals(
            expected,
            edge.setPadding(
                width
            ),
            () -> edge + " setPadding " + width
        );
    }

    // parseBorder......................................................................................................

    @Test
    public void testParseBorderWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> BoxEdge.ALL.parseBorder(null)
        );
    }

    @Test
    public void testParseBorderWithEmpty() {
        this.parseBorderAndCheck(
            BoxEdge.ALL,
            "",
            BoxEdge.ALL.setBorder(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseBorderInvalidColorFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> BoxEdge.ALL.parseBorder("!invalid-color SOLID 1px")
        );

        this.checkEquals(
            "Invalid rgb \"!invalid-color\"",
            thrown.getMessage()
        );
    }

    @Test
    public void testParseBorderInvalidColorFails2() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> BoxEdge.ALL.parseBorder("  !invalid-color SOLID 1px")
        );

        this.checkEquals(
            "Invalid rgb \"!invalid-color\"",
            thrown.getMessage()
        );
    }

    @Test
    public void testParseBorderInvalidBorderStyleFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> BoxEdge.ALL.parseBorder("black !INVALID 1px")
        );

        this.checkEquals(
            "Unknown style \"!INVALID\"",
            thrown.getMessage()
        );
    }

    @Test
    public void testParseBorderInvalidBorderWidthFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> BoxEdge.ALL.parseBorder("black SOLID !invalid")
        );

        this.checkEquals(
            "Invalid number length \"!invalid\"",
            thrown.getMessage()
        );
    }

    @Test
    public void testParseBorderInvalidBorderWidthFails2() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> BoxEdge.ALL.parseBorder("black SOLID !invalid ")
        );

        this.checkEquals(
            "Invalid number length \"!invalid\"",
            thrown.getMessage()
        );
    }

    @Test
    public void testParseBorderColorNameStyleWidth() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "black SOLID 1px",
            BoxEdge.TOP.setBorder(
                Optional.of(Color.BLACK),
                Optional.of(BorderStyle.SOLID),
                Optional.of(
                    Length.pixel(1.0)
                )
            )
        );
    }

    @Test
    public void testParseBorderRgbColorStyleWidth() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "#123 SOLID 1px",
            BoxEdge.TOP.setBorder(
                Optional.of(
                    Color.parse("#123")
                ),
                Optional.of(BorderStyle.SOLID),
                Optional.of(
                    Length.pixel(1.0)
                )
            )
        );
    }

    @Test
    public void testParseBorderColorMixedCaseStyleWidth() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "#123 SOlid 1px",
            BoxEdge.TOP.setBorder(
                Optional.of(
                    Color.parse("#123")
                ),
                Optional.of(
                    BorderStyle.SOLID
                ),
                Optional.of(
                    Length.pixel(1.0)
                )
            )
        );
    }

    @Test
    public void testParseBorderColor() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "black",
            BoxEdge.TOP.setBorder(
                Optional.of(Color.BLACK),
                Optional.empty(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseBorderStyle() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "SOLID",
            BoxEdge.TOP.setBorder(
                Optional.empty(),
                Optional.of(BorderStyle.SOLID),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseBorderStyleWidth() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "SOLID 1px",
            BoxEdge.TOP.setBorder(
                Optional.empty(),
                Optional.of(BorderStyle.SOLID),
                Optional.of(
                    Length.pixel(1.0)
                )
            )
        );
    }

    @Test
    public void testParseBorderWidth() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "1px",
            BoxEdge.TOP.setBorder(
                Optional.empty(),
                Optional.empty(),
                Optional.of(
                    Length.pixel(1.0)
                )
            )
        );
    }

    @Test
    public void testParseBorderExtraSpaces() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "  black  SOLID  1px  ",
            BoxEdge.TOP.setBorder(
                Optional.of(Color.BLACK),
                Optional.of(BorderStyle.SOLID),
                Optional.of(
                    Length.pixel(1.0)
                )
            )
        );
    }

    @Test
    public void testParseBorderNoneLength() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "black SOLID none",
            BoxEdge.TOP.setBorder(
                Optional.of(Color.BLACK),
                Optional.of(BorderStyle.SOLID),
                Optional.of(
                    Length.none()
                )
            )
        );
    }

    @Test
    public void testParseBorderTrailingSpace() {
        this.parseBorderAndCheck(
            BoxEdge.TOP,
            "black SOLID none  ",
            BoxEdge.TOP.setBorder(
                Optional.of(Color.BLACK),
                Optional.of(BorderStyle.SOLID),
                Optional.of(
                    Length.none()
                )
            )
        );
    }

    private void parseBorderAndCheck(final BoxEdge edge,
                                     final String text,
                                     final Border expected) {
        this.checkEquals(
            expected,
            edge.parseBorder(text),
            () -> edge + " parseBorder " + CharSequences.quoteAndEscape(text)
        );
    }

    // parseMargin......................................................................................................

    @Test
    public void testParseMarginAll() {
        this.parseMarginAndCheck(
            BoxEdge.ALL,
            "1px",
            BoxEdge.ALL.setMargin(
                Optional.of(
                    Length.pixel(1.0)
                )
            )
        );
    }

    @Test
    public void testParseMarginAllEmpty() {
        this.parseMarginAndCheck(
            BoxEdge.ALL,
            "",
            BoxEdge.ALL.setMargin(
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseMarginTop() {
        this.parseMarginAndCheck(
            BoxEdge.TOP,
            "1px",
            TextStyle.parse("margin-top: 1px")
                .margin(BoxEdge.TOP)
        );
    }

    @Test
    public void testParseMarginLeft() {
        this.parseMarginAndCheck(
            BoxEdge.LEFT,
            "1px",
            TextStyle.parse("margin-left: 1px")
                .margin(BoxEdge.LEFT)
        );
    }

    @Test
    public void testParseMarginRightWithEmpty() {
        this.parseMarginAndCheck(
            BoxEdge.LEFT,
            "",
            TextStyle.EMPTY.margin(BoxEdge.LEFT)
        );
    }

    private void parseMarginAndCheck(final BoxEdge edge,
                                      final String text,
                                      final Margin expected) {
        this.checkEquals(
            expected,
            edge.parseMargin(text),
            () -> edge + " parseMargin " + CharSequences.quoteAndEscape(text)
        );
    }

    // parsePadding.....................................................................................................

    @Test
    public void testParsePaddingAll() {
        this.parsePaddingAndCheck(
            BoxEdge.ALL,
            "1px",
            BoxEdge.ALL.setPadding(
                Optional.of(
                    Length.pixel(1.0)
                )
            )
        );
    }

    @Test
    public void testParsePaddingTop() {
        this.parsePaddingAndCheck(
            BoxEdge.TOP,
            "1px",
            TextStyle.parse("padding-top: 1px")
                .padding(BoxEdge.TOP)
        );
    }

    @Test
    public void testParsePaddingLeft() {
        this.parsePaddingAndCheck(
            BoxEdge.LEFT,
            "1px",
            TextStyle.parse("padding-left: 1px")
                .padding(BoxEdge.LEFT)
        );
    }

    @Test
    public void testParsePaddingRightWithEmpty() {
        this.parsePaddingAndCheck(
            BoxEdge.LEFT,
            "",
            TextStyle.EMPTY.padding(BoxEdge.LEFT)
        );
    }

    private void parsePaddingAndCheck(final BoxEdge edge,
                                      final String text,
                                      final Padding expected) {
        this.checkEquals(
            expected,
            edge.parsePadding(text),
            () -> edge + " parsePadding " + CharSequences.quoteAndEscape(text)
        );
    }

    // isTextStyleProperty..............................................................................................

    @Test
    public void testIsTextStylePropertyBoxEdgeTopAndBorderTopColor() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.TOP,
            TextStylePropertyName.BORDER_TOP_COLOR,
            true
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeTopAndBorderTopStyle() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.TOP,
            TextStylePropertyName.BORDER_TOP_STYLE,
            true
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeTopAndBorderTopWidth() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.TOP,
            TextStylePropertyName.BORDER_TOP_WIDTH,
            true
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeRightAndBorderTopWidth() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.RIGHT,
            TextStylePropertyName.BORDER_TOP_WIDTH,
            false
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeLeftAndBorderRightWidth() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.LEFT,
            TextStylePropertyName.BORDER_RIGHT_WIDTH,
            false
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeLeftAndMargin() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.LEFT,
            TextStylePropertyName.MARGIN,
            false
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeLeftAndPadding() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.LEFT,
            TextStylePropertyName.PADDING,
            false
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeAllAndBorderTopWidth() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.ALL,
            TextStylePropertyName.BORDER_TOP_WIDTH,
            true
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeAllAndBorderRightWidth() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.ALL,
            TextStylePropertyName.BORDER_RIGHT_WIDTH,
            true
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeAllAndBorderBottomColor() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.ALL,
            TextStylePropertyName.BORDER_BOTTOM_COLOR,
            true
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeAllAndColor() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.ALL,
            TextStylePropertyName.COLOR,
            false
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeAllAndMargin() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.ALL,
            TextStylePropertyName.MARGIN,
            true
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeAllAndMarginLeft() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.ALL,
            TextStylePropertyName.MARGIN_LEFT,
            true
        );
    }

    @Test
    public void testIsTextStylePropertyBoxEdgeAllAndPaddingLeft() {
        this.isTextStylePropertyAndCheck(
            BoxEdge.ALL,
            TextStylePropertyName.PADDING_LEFT,
            true
        );
    }

    private void isTextStylePropertyAndCheck(final BoxEdge edge,
                                             final TextStylePropertyName<?> propertyName,
                                             final boolean expected) {
        this.checkEquals(
            expected,
            edge.isTextStyleProperty(propertyName),
            () -> edge + " isTextStyleProperty " + propertyName
        );
    }

    // class............................................................................................................

    @Override
    public Class<BoxEdge> type() {
        return BoxEdge.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
