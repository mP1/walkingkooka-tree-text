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
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.type.JavaVisibility;

import java.util.Set;

public final class TextOverflowTest implements ClassTesting2<TextOverflow>,
        ConstantsTesting<TextOverflow>,
        JsonNodeMarshallingTesting<TextOverflow> {

    @Test
    public void testClipJsonRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.CLIP);
    }

    @Test
    public void testEllipsisJsonRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.ELLIPSIS);
    }

    @Test
    public void testStringJsonRoundtrip() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.string("abc123"));
    }

    @Test
    public void testStringJsonRoundtrip2() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.string("clip"));
    }

    @Test
    public void testStringJsonRoundtrip3() {
        this.marshallRoundTripTwiceAndCheck(TextOverflow.string("ellipsis"));
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextOverflow> type() {
        return TextOverflow.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ConstantTesting..................................................................................................

    @Override
    public Set<TextOverflow> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public TextOverflow unmarshall(final JsonNode from,
                                   final JsonNodeUnmarshallContext context) {
        return TextOverflow.unmarshall(from, context);
    }

    @Override
    public TextOverflow createJsonNodeMappingValue() {
        return TextOverflow.string("hello-123");
    }
}
