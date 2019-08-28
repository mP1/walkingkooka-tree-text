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

import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.FromJsonNodeContext;
import walkingkooka.tree.json.marshall.ToJsonNodeContext;

/**
 * Base class for {@link TextStylePropertyValueHandler} that have {@link Length} values.
 */
abstract class LengthTextStylePropertyValueHandler extends TextStylePropertyValueHandler<Length<?>> {

    /**
     * Package private to limit sub classes.
     */
    LengthTextStylePropertyValueHandler() {
        super();
    }

    @Override
    final void check0(final Object value, final TextStylePropertyName<?> name) {
        final Length<?> length = this.checkType(value, Length.class, name);
        if (this.lengthCheck(length)) {
            // ok
        } else {
            throw this.textStylePropertyValueException(value, name);
        }
    }

    abstract boolean lengthCheck(final Length<?> length);

    // fromJsonNode ....................................................................................................

    @Override
    final Length<?> fromJsonNode(final JsonNode node,
                                 final TextStylePropertyName<?> name,
                                 final FromJsonNodeContext context) {
        final Length<?> length = context.fromJsonNode(node, Length.class);
        this.check0(length, name);
        return length;
    }

    @Override
    final JsonNode toJsonNode(final Length<?> value,
                              final ToJsonNodeContext context) {
        return context.toJsonNode(value);
    }
}
