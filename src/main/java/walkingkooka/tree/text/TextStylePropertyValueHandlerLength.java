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

import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;

/**
 * Base class for {@link TextStylePropertyValueHandler} that have {@link Length} values.
 */
abstract class TextStylePropertyValueHandlerLength extends TextStylePropertyValueHandler<Length<?>> {

    /**
     * Package private to limit sub classes.
     */
    TextStylePropertyValueHandlerLength() {
        super();
    }

    @Override
    final Class<Length<?>> valueType() {
        return Cast.to(Length.class);
    }

    @Override
    final boolean testValue(final Object value) {
        return value instanceof Length &&
            this.lengthCheck(
            (Length<?>) value
        );
    }

    @Override
    Optional<Class<Enum<?>>> enumType() {
        return Optional.empty();
    }

    abstract boolean lengthCheck(final Length<?> length);

    @Override
    Length<?> parseValue(final TextStyleParser parser) {
        return this.parseValueText(
            parser.token()
        );
    }

    @Override
    Length<?> parseValueText(final String value) {
        return Length.parse(value);
    }

    // unmarshall ....................................................................................................

    @Override
    final Length<?> unmarshall(final JsonNode node,
                                         final TextStylePropertyName<?> name,
                                         final JsonNodeUnmarshallContext context) {
        final Length<?> length = context.unmarshall(
            node,
            Length.class
        );
        name.checkValue(length);
        return length;
    }

    @Override
    final JsonNode marshall(final Length<?> value,
                            final JsonNodeMarshallContext context) {
        return context.marshall(value);
    }
}
