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
import walkingkooka.collect.map.Maps;

public final class MarginTest extends BorderMarginPaddingTestCase<Margin> {

    // toString.........................................................................................................

    @Test
    public void testToStringWithAll() {
        this.toStringAndCheck(
            TextStyle.parse("margin-left: 11px; margin-top: 22px; margin-right: 33px; margin-bottom: 44px;")
                .margin(BoxEdge.ALL),
            "margin ALL bottom: 44px; left: 11px; right: 33px; top: 22px;"
        );
    }
    
    @Test
    public void testToStringBottom() {
        this.toStringAndCheck(
            Margin.with(
                BoxEdge.BOTTOM,
                TextStyle.EMPTY.setValues(
                    Maps.of(
                        TextStylePropertyName.MARGIN_BOTTOM, Length.pixel(12.5)
                    )
                )
            ),
            "margin BOTTOM bottom: 12.5px;"
        );
    }

    // text.............................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(
            TextStyle.EMPTY.setMargin(
                Length.pixel(2.0)
            ).margin(BoxEdge.ALL),
            "bottom: 2px; left: 2px; right: 2px; top: 2px;"
        );
    }

    // helpers..........................................................................................................

    @Override
    Margin createBorderMarginPadding(final BoxEdge edge, final TextStyle textStyle) {
        return edge.margin(textStyle);
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName(final BoxEdge edge) {
        return edge.marginPropertyName();
    }

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseInvalidMarginTextStylePropertyFails() {
        this.parseStringFails(
            "top-invalid: 1px;",
            new IllegalArgumentException("Unknown text style property name: top-invalid")
        );
    }

    @Test
    public void testParsePaddingPropertyFails() {
        this.parseStringFails(
            "top: 1px; padding-top: 2px",
            new IllegalArgumentException("Invalid margin property \"padding-top\"")
        );
    }
    
    @Test
    public void testParseEmptyString() {
        this.parseStringAndCheck(
            "",
            TextStyle.EMPTY.margin(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseWithPrefix() {
        final String text = "margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-right: 4px;";

        this.parseStringAndCheck(
            text,
            TextStyle.parse(text)
                .margin(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseWithoutPrefix() {
        final String text = "margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-right: 4px;";

        this.parseStringAndCheck(
            text.replace("margin-", ""),
            TextStyle.parse(text)
                .margin(BoxEdge.ALL)
        );
    }

    @Test
    public void testParseWithAndWithoutPrefix() {
        this.parseStringAndCheck(
            "margin-top: 1px; margin-right: 2px; bottom: 3px; right: 4px;",
            TextStyle.parse("margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-right: 4px;")
                .margin(BoxEdge.ALL)
        );
    }

    @Override
    public Margin parseString(final String text) {
        return Margin.parse(text);
    }

    // class............................................................................................................

    @Override
    public Class<Margin> type() {
        return Margin.class;
    }
}
