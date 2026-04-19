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

import java.util.Optional;

public final class TextStylePropertyValueHandlerMarginTest extends TextStylePropertyValueHandlerTestCase2<TextStylePropertyValueHandlerMargin, Margin> {

    @Test
    @Override
    public void testCheckValueWithIncompatibleTypeFails() {
        this.checkValueFails(
            this,
            "Invalid \"margin\" expected Margin but got " + this.getClass().getSimpleName()
        );
    }

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseValueTextWithInvalidFails() {
        this.parseStringFails(
            "!invalid",
            new IllegalArgumentException("Invalid number length \"!invalid\"")
        );
    }

    @Test
    public void testParseValueTextWithInvalidFails2() {
        this.parseStringFails(
            "-1",
            new IllegalArgumentException("Invalid length -1 < 0")
        );
    }

    @Test
    public void testParseValueTextWithEmptyString() {
        final String text = "";

        this.parseStringAndCheck(
            text,
            Margin.parse(text)
        );
    }

    @Test
    public void testUnmarshall() {
        final Margin margin = BoxEdge.BOTTOM.setMargin(
            Optional.of(
                Length.pixel(1.0)
            )
        );

        this.unmarshallAndCheck(
            JsonNode.string(
                BoxEdge.BOTTOM + " " + margin.text()
            ),
            margin
        );
    }

    @Test
    public void testMarshall() {
        final Margin margin = BoxEdge.BOTTOM.setMargin(
            Optional.of(
                Length.pixel(1.0)
            )
        );

        this.marshallAndCheck(
            margin,
            JsonNode.string(
                BoxEdge.BOTTOM + " " + margin.text()
            )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.handler(),
            "Margin"
        );
    }

    @Override
    TextStylePropertyValueHandlerMargin handler() {
        return TextStylePropertyValueHandlerMargin.INSTANCE;
    }

    @Override
    TextStylePropertyName<Margin> propertyName() {
        return TextStylePropertyName.MARGIN;
    }

    @Override
    Margin propertyValue() {
        return Margin.parse("1px 2px 3px 4px");
    }

    @Override
    public String typeNameSuffix() {
        return Margin.class.getSimpleName();
    }

    @Override
    public Class<TextStylePropertyValueHandlerMargin> type() {
        return TextStylePropertyValueHandlerMargin.class;
    }
}
