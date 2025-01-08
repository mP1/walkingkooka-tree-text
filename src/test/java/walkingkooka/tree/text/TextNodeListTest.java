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
import walkingkooka.collect.list.ImmutableListTesting;
import walkingkooka.collect.list.ListTesting2;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextNodeListTest implements ListTesting2<TextNodeList, TextNode>,
    ClassTesting<TextNodeList>,
    ImmutableListTesting<TextNodeList, TextNode>,
    JsonNodeMarshallingTesting<TextNodeList> {

    private final static TextNode NODE1 = TextNode.text("Hello");

    private final static TextNode NODE2 = TextNode.hyperlink(
        Url.parseAbsolute("https://example.com/link123")
    );

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextNodeList.with(null)
        );
    }

    @Test
    public void testDoesntDoubleWrap() {
        final TextNodeList list = this.createList();
        assertSame(
            list,
            TextNodeList.with(list)
        );
    }

    @Test
    public void testGet() {
        this.getAndCheck(
            this.createList(),
            0, // index
            NODE1 // expected
        );
    }

    @Test
    public void testGet2() {
        this.getAndCheck(
            this.createList(),
            1, // index
            NODE2 // expected
        );
    }

    @Test
    public void testSetFails() {
        this.setFails(
            this.createList(),
            0, // index
            NODE1 // expected
        );
    }

    @Test
    public void testRemoveIndexFails() {
        final TextNodeList list = this.createList();

        this.removeIndexFails(
            list,
            0
        );
    }

    @Test
    public void testRemoveElementFails() {
        final TextNodeList list = this.createList();

        this.removeFails(
            list,
            list.get(0)
        );
    }

    @Override
    public TextNodeList createList() {
        return TextNodeList.with(
            Lists.of(
                NODE1,
                NODE2
            )
        );
    }

    @Override
    public Class<TextNodeList> type() {
        return TextNodeList.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // Json...........................................................................................................

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
            this.createList(),
            "[\n" +
                "  {\n" +
                "    \"type\": \"text\",\n" +
                "    \"value\": \"Hello\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"hyperlink\",\n" +
                "    \"value\": {\n" +
                "      \"url\": \"https://example.com/link123\"\n" +
                "    }\n" +
                "  }\n" +
                "]"
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            "[\n" +
                "  {\n" +
                "    \"type\": \"text\",\n" +
                "    \"value\": \"Hello\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"hyperlink\",\n" +
                "    \"value\": {\n" +
                "      \"url\": \"https://example.com/link123\"\n" +
                "    }\n" +
                "  }\n" +
                "]",
            this.createList()
        );
    }

    @Override
    public TextNodeList unmarshall(final JsonNode json,
                                   final JsonNodeUnmarshallContext context) {
        return TextNodeList.unmarshall(
            json,
            context
        );
    }

    @Override
    public TextNodeList createJsonNodeMarshallingValue() {
        return this.createList();
    }
}
