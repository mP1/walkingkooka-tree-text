[![Build Status](https://travis-ci.com/mP1/walkingkooka-tree-text.svg?branch=master)](https://travis-ci.com/mP1/walkingkooka-tree-text.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-tree-text/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-tree-text?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-tree-text.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree-text/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-tree-text.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree-text/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)



A [walkingkooka/tree.Node](https://github.com/mP1/walkingkooka/blob/master/Node.md) implementation representing rich text in a tree graph.

## Java sample

```java
final TextNode node = TextNode.styleName(TextStyleName.with("HTML"))
        .appendChild(TextNode.styleName(TextStyleName.with("head")).appendChild(TextNode.styleName(TextStyleName.with("TITLE")).appendChild(TextNode.text("title123"))))
        .appendChild(TextNode.styleName(TextStyleName.with("BODY"))
                .appendChild(TextNode.text("before"))
                .appendChild(TextNode.text("something gray").setAttributes(Maps.of(TextStylePropertyName.COLOR, Color.parse("#345"))).parentOrFail())
                .appendChild(TextNode.text("after"))
        );

final StringBuilder html = new StringBuilder();
final LineEnding eol = LineEnding.SYSTEM;
final IndentingPrinter printer = Printers.stringBuilder(html, eol)
        .indenting(Indentation.with("  "));

// very simple html printer
new FakeTextNodeVisitor(){
    @Override
    protected Visiting startVisit(final TextNode node) {
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final TextNode node) {
    }

    @Override
    protected Visiting startVisit(final TextStyleNode node) {
        printer.lineStart();
        printer.print("<SPAN style=\"");

        node.attributes()
                .forEach((p, v) -> {
                    printer.print(p + ": " + v + ";");
                });

        printer.print("\">" + eol);
        printer.indent();
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final TextStyleNode node) {
        printer.outdent();
        printer.lineStart();
        printer.print("</SPAN>" + eol);
    }

    @Override
    protected Visiting startVisit(final TextStyleNameNode node) {
        this.beginElement(node.styleName().value());
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final TextStyleNameNode node) {
        this.endElement(node.styleName().value());
    }

    @Override
    protected void visit(final Text node) {
        printer.print(node.value());
    }

    private void beginElement(final String element) {
        printer.lineStart();
        printer.print("<" + element + ">" + eol);
        printer.indent();
    }

    private void endElement(final String element) {
        printer.outdent();
        printer.lineStart();
        printer.print("</" + element + ">"+eol);
    }
}.accept(node);
```

prints

```text
<HTML>
  <head>
    <TITLE>
      title123
    </TITLE>
  </head>
  <BODY>
    before
    <SPAN style="color: #334455;">
      something gray
    </SPAN>
    after
  </BODY>
</HTML>
```