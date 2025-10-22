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

import walkingkooka.collect.map.Maps;

import java.util.Map;

public final class TextStylePerfTest {

    public static void main(final String[] args) {
        final TextStyle textStyle = TextStyle.parse("color:red; font-weight: bold; text-align: left");

        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(TextStylePropertyName.COLOR, "red");
        map.put(TextStylePropertyName.FONT_WEIGHT, FontWeight.BOLD);
        map.put(TextStylePropertyName.TEXT_ALIGN, TextAlign.LEFT);

        final int loop = 12345678;

        mapStyleLoop(map, loop);
        textStyleLoop(textStyle, loop);

        mapStyleLoop(map, loop);
        textStyleLoop(textStyle, loop);

        mapStyleLoop(map, loop);
        textStyleLoop(textStyle, loop);
    }

    private static void mapStyleLoop(final Map<TextStylePropertyName<?>, Object> map,
                              final int count) {
        final long start = System.nanoTime();

        for(int i = 0; i < count; i++ ) {
            map.get(TextStylePropertyName.COLOR);
            map.get(TextStylePropertyName.FONT_WEIGHT);
            map.get(TextStylePropertyName.TEXT_ALIGN);
            map.get(TextStylePropertyName.BACKGROUND_COLOR);
            map.get(TextStylePropertyName.FONT_FAMILY);
            map.get(TextStylePropertyName.WORD_BREAK);
        }

        final long stop = System.nanoTime();

        System.out.println("Map:\t\t" + (stop - start));
    }

    private static void textStyleLoop(final TextStyle style,
                               final int count) {
        final long start = System.nanoTime();

        for(int i = 0; i < count; i++ ) {
            style.get(TextStylePropertyName.COLOR);
            style.get(TextStylePropertyName.FONT_WEIGHT);
            style.get(TextStylePropertyName.TEXT_ALIGN);
            style.get(TextStylePropertyName.BACKGROUND_COLOR);
            style.get(TextStylePropertyName.FONT_FAMILY);
            style.get(TextStylePropertyName.WORD_BREAK);
        }

        final long stop = System.nanoTime();

        System.out.println("TextStyle:\t" + (stop - start));
    }
}
