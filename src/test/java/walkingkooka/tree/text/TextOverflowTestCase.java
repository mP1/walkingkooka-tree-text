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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

public abstract class TextOverflowTestCase<T extends TextOverflow> extends TextStylePropertyValueTestCase3<TextOverflow>
        implements HashCodeEqualsDefinedTesting2<TextOverflow>,
        TypeNameTesting<TextOverflow> {

    TextOverflowTestCase() {
        super();
    }

    @Test
    public final void testFromJsonStringNodeEmptyFails() {
        this.unmarshallFails(JsonNode.string(""), IllegalArgumentException.class);
    }

    @Override
    final TextStylePropertyName<TextOverflow> textStylePropertyName() {
        return TextStylePropertyName.TEXT_OVERFLOW;
    }

    // JsonNodeMarshallingTesting.......................................................................................

    @Override
    public TextOverflow unmarshall(final JsonNode from,
                                   final JsonNodeUnmarshallContext context) {
        return TextOverflow.unmarshall(from, context);
    }

    // ClassTesting.....................................................................................................

    @Override
    public final Class<TextOverflow> type() {
        return Cast.to(this.textOverflowType());
    }

    abstract Class<T> textOverflowType();

    // HashCodeEqualsDefinedTesting.....................................................................................

    @Override
    public final TextOverflow createObject() {
        return this.createTextStylePropertyValue();
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return TextOverflow.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
