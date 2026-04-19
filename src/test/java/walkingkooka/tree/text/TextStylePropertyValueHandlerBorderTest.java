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
import walkingkooka.tree.json.JsonNode;

public final class TextStylePropertyValueHandlerBorderTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerBorder, Border> {

    @Test
    @Override
    public void testCheckValueWithIncompatibleTypeFails() {
        this.checkValueFails(
            this,
            "Invalid \"border\" expected Border but got " + this.getClass().getSimpleName()
        );
    }

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseValueTextInvalidFails2() {
        this.parseStringFails(
            "Invalid!",
            new RuntimeException("Unknown color name \"Invalid!\"")
        );
    }

    @Test
    public void testParseValueTextEmpty() {
        final Border border = Border.parse("");

        this.parseStringAndCheck(
            border.text(),
            border
        );
    }

    @Test
    public void testParseValueText() {
        final Border border = Border.parse("top-color: black; right-style: SOLID; bottom-width: 1px;");

        this.parseStringAndCheck(
            border.text(),
            border
        );
    }

    @Test
    public void testUnmarshall() {
        final Border border = Border.parse("BLACK SOLID 1px")
            .setEdge(BoxEdge.BOTTOM);

        this.unmarshallAndCheck(
            JsonNode.string(
                BoxEdge.BOTTOM + " " + border.text()
            ),
            border
        );
    }

    @Test
    public void testMarshall() {
        final Border border = Border.parse("BLACK SOLID 1px")
            .setEdge(BoxEdge.BOTTOM);

        this.marshallAndCheck(
            border,
            JsonNode.string(
                BoxEdge.BOTTOM + " " + border.text()
            )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.handler(),
            "Border"
        );
    }

    @Override
    TextStylePropertyValueHandlerBorder handler() {
        return TextStylePropertyValueHandlerBorder.INSTANCE;
    }

    @Override
    TextStylePropertyName<Border> propertyName() {
        return TextStylePropertyName.BORDER;
    }

    @Override
    Border propertyValue() {
        return Border.parse("BLACK SOLID 1px, WHITE DASHED 2px, RED DOTTED 3px, GREEN SOLID 4px");
    }

    @Override
    public String typeNameSuffix() {
        return Border.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerBorder> type() {
        return TextStylePropertyValueHandlerBorder.class;
    }
}
