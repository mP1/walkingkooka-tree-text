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

import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

/**
 * The name of a placeholder.
 */
public final class TextPlaceholderName extends TextNodeNameName<TextPlaceholderName> {

    public static TextPlaceholderName with(final String name) {
        checkName(name);
        return new TextPlaceholderName(name);
    }

    // @VisibleForTesting
    private TextPlaceholderName(final String name) {
        super(name);
    }

    // JsonNodeContext...................................................................................................

    static TextPlaceholderName unmarshall(final JsonNode from,
                                          final JsonNodeUnmarshallContext context) {
        return with(from.stringOrFail());
    }

    static {
        JsonNodeContext.register("text-placeholder-name",
            TextPlaceholderName::unmarshall,
            TextPlaceholderName::marshall,
            TextPlaceholderName.class);
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof TextPlaceholderName;
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
