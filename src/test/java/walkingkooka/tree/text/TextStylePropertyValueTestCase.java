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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TextStylePropertyValueTestCase<V> implements ClassTesting2<V>, ToStringTesting<V> {

    TextStylePropertyValueTestCase() {
        super();
    }

    @Test
    public final void testParseValueNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.textStylePropertyName().parseValue(null)
        );
    }

    @Test
    public final void testTextStylePropertyJsonRoundtrip() {
        final TextNode properties = TextNode.style(TextStyleNode.NO_CHILDREN)
            .setAttributes(
                Maps.of(
                    this.textStylePropertyName(),
                    this.createTextStylePropertyValue()
                )
            );
        final JsonNode json = JsonNodeMarshallContexts.basic().marshallWithType(properties);
        this.checkEquals(
            properties,
            JsonNodeUnmarshallContexts.basic(
                (String cc) -> {
                    throw new UnsupportedOperationException();
                },
                (String lt) -> {
                    throw new UnsupportedOperationException();
                },
                ExpressionNumberKind.DEFAULT,
                MathContext.DECIMAL32
            ).unmarshallWithType(json),
            () -> "" + properties);
    }

    @Test
    public final void testTextStylePropertyNameCheck() {
        this.textStylePropertyName().checkValue(this.createTextStylePropertyValue());
    }

    abstract V createTextStylePropertyValue();

    abstract TextStylePropertyName<V> textStylePropertyName();
}
