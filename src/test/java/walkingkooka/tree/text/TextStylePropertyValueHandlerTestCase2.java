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
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

public abstract class TextStylePropertyValueHandlerTestCase2<P extends TextStylePropertyValueHandler<T>, T> extends TextStylePropertyValueHandlerTestCase<P, T> {

    TextStylePropertyValueHandlerTestCase2() {
        super();
    }

    @Test
    public final void testCheckValue() {
        this.checkValue(
            this.propertyValue()
        );
    }

    @Test
    public final void testRoundtripJson() {
        final T value = this.propertyValue();
        final P handler = this.handler();

        final JsonNode json = handler.marshall(value, this.marshallContext());

        this.checkEquals(value,
            handler.unmarshall(json, this.propertyName(), this.unmarshallContext()),
            () -> "value " + CharSequences.quoteIfChars(value) + " to json " + json);
    }

    final void unmarshallAndCheck(final JsonNode node, final T value) {
        this.checkEquals(value,
            this.handler().unmarshall(node, this.propertyName(), this.unmarshallContext()),
            () -> "from JsonNode " + node);
    }

    final void marshallAndCheck(final T value, final JsonNode node) {
        this.checkEquals(node,
            this.handler().marshall(value, this.marshallContext()),
            () -> "marshall " + CharSequences.quoteIfChars(value));
    }

    // helper...........................................................................................................

    abstract T propertyValue();
}
