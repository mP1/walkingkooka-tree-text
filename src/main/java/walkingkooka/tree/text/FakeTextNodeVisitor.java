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

import walkingkooka.test.Fake;
import walkingkooka.visit.Visiting;

public class FakeTextNodeVisitor extends TextNodeVisitor implements Fake {

    public FakeTextNodeVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final TextNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final TextNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final TextStyleNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final TextStyleNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final TextHyperlinkNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final TextHyperlinkNode node) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Visiting startVisit(final TextStyleNameNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final TextStyleNameNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final TextPlaceholderNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Text node) {
        throw new UnsupportedOperationException();
    }
}
