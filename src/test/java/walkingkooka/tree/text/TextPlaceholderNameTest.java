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
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.json.JsonNode;

public final class TextPlaceholderNameTest extends TextNodeNameNameTestCase<TextPlaceholderName> {

    @Override
    public TextPlaceholderName createName(final String name) {
        return TextPlaceholderName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public TextPlaceholderName fromJsonNode(final JsonNode jsonNode) {
        return TextPlaceholderName.fromJsonNode(jsonNode);
    }

    @Override
    public Class<TextPlaceholderName> type() {
        return Cast.to(TextPlaceholderName.class);
    }
}
