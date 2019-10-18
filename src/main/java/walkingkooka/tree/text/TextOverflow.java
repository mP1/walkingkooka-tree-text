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

import walkingkooka.Value;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;

/**
 * Value class that holds a text-overflow
 */
public abstract class TextOverflow implements Value<Optional<String>> {

    final static String CLIP_TEXT = "clip";

    /**
     * A constant holding the font-weight of normal text
     */
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public final static TextOverflow CLIP = TextOverflowNonString.constant(CLIP_TEXT);

    final static String ELLIPSIS_TEXT = "ellipsis";

    /**
     * A constant holding the font-weight of bold text
     */
    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public final static TextOverflow ELLIPSIS = TextOverflowNonString.constant(ELLIPSIS_TEXT);

    /**
     * Prefix added to {@link TextOverflowString}.
     */
    final static String STRING_PREFIX = "string-";

    /**
     * Factory that creates a {@link TextOverflow}.
     */
    public static TextOverflow string(final String value) {
        return TextOverflowString.with(value);
    }

    /**
     * Package private to limit sub classing.
     */
    TextOverflow() {
        super();
    }

    /**
     * Getter that returns any text if present.
     */
    public abstract Optional<String> value();

    public abstract boolean isClip();

    public abstract boolean isEllipse();

    public final boolean isString() {
        return this instanceof TextOverflowString;
    }

    @Override
    public abstract String toString();

    // JsonNodeContext..................................................................................................

    /**
     * Factory that creates a {@link TextOverflow} from the given node.
     */
    static TextOverflow unmarshall(final JsonNode node,
                                   final JsonNodeUnmarshallContext context) {
        final String value = node.stringValueOrFail();
        return value.startsWith(STRING_PREFIX) ?
                string(value.substring(STRING_PREFIX.length())) :
                CLIP_TEXT.equals(value) ?
                        CLIP :
                        ELLIPSIS_TEXT.equals(value) ?
                                ELLIPSIS :
                                failInvalidOverflow(value);
    }

    private static TextOverflow failInvalidOverflow(final String value) {
        throw new IllegalArgumentException("Unknown text-overflow " + CharSequences.quote(value));
    }

    abstract JsonNode marshall(final JsonNodeMarshallContext context);

    static {
        JsonNodeContext.register("text-overflow",
                TextOverflow::unmarshall,
                TextOverflow::marshall,
                TextOverflow.class, TextOverflowNonString.class, TextOverflowString.class);
    }
}
