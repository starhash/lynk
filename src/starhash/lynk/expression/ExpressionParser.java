/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starhash.lynk.expression;

import Infinity.Grammar.Parsing.ParseTreeNode;
import Infinity.Grammar.RepetitiveType;
import Infinity.Utility.RefSupport;
import InfinityX.Grammar.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Ref;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import jdk.nashorn.internal.ir.IdentNode;

/**
 *
 * @author Harsh
 */
public class ExpressionParser {

    public ParseTreeNode parseExpression(String expression) {
        try {
            XRetractableBufferedTokenStream stream = new XRetractableBufferedTokenStream(expression);
            XRetractionBufferState state = stream.stateNode();
            XParseResult result = new Expression().validate(stream);
            return result.getNode();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ExpressionParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExpressionParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

class Expression extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("Expression");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        on(new SelectStatement(), stream, resultRef, null, () -> {
            on(new AnyStatement(), stream, resultRef, null, () -> {
                on(new AllStatement(), stream, resultRef, null, null);
            });
        });
        return result;
    }

}

class AllStatement extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("AllStatement");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        on(new AllBlock(), stream, resultRef, () -> {
            and(new FromBlock(), stream, resultRef, null, null);
        }, null);
        return result;
    }

}

class AnyStatement extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("AnyStatement");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        on(new AnyBlock(), stream, resultRef, () -> {
            and(new FromBlock(), stream, resultRef, null, null);
        }, null);
        return result;
    }

}

class SelectStatement extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("SelectStatement");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        on(new SelectBlock(), stream, resultRef, () -> {
            and(new FromBlock(), stream, resultRef, () -> {

            }, null);
            or(new WhereBlock(), stream, resultRef, () -> {

            }, null);
            or(new DistinctBlock(), stream, resultRef, null, null);
            or(new CompareBlock(), stream, resultRef, null, null);
        }, null);
        return result;
    }

}

class AllBlock extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("All");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        off(new XLiteralGrammarElement("all"), stream, resultRef, () -> {
            off(new XLiteralGrammarElement("\\("), stream, resultRef, () -> {
                on(new ArgumentIndexer(), stream, resultRef, () -> {
                    off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                }, null);
            }, null);
        }, null);
        return result;
    }

}

class AnyBlock extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("Any");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        off(new XLiteralGrammarElement("any"), stream, resultRef, () -> {
            off(new XLiteralGrammarElement("\\("), stream, resultRef, () -> {
                on(new ArgumentIndexer(), stream, resultRef, () -> {
                    off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                }, null);
            }, null);
        }, null);
        return result;
    }

}

class CompareBlock extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("Compare");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        off(new XLiteralGrammarElement("compare"), stream, resultRef, () -> {
            off(new XLiteralGrammarElement("\\("), stream, resultRef, () -> {
                on(new ArgumentIndexer(), stream, resultRef, () -> {
                    off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                }, null);
            }, null);
        }, null);
        return result;
    }

}

class DistinctBlock extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("Distinct");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        off(new XLiteralGrammarElement("distinct"), stream, resultRef, () -> {
            off(new XLiteralGrammarElement("\\("), stream, resultRef, () -> {
                on(new Identifier(), stream, resultRef, () -> {
                    off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                }, null);
            }, null);
        }, null);
        return result;
    }

}

class TransformBlock extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("Transform");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        off(new XLiteralGrammarElement("transform"), stream, resultRef, () -> {
            off(new XLiteralGrammarElement("\\("), stream, resultRef, () -> {
                on(new ArgumentIndexer(), stream, resultRef, () -> {
                    off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                }, null);
            }, null);
        }, null);
        return result;
    }

}

class WhereBlock extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("Where");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        off(new XLiteralGrammarElement("where"), stream, resultRef, () -> {
            off(new XLiteralGrammarElement("\\("), stream, resultRef, () -> {
                on(new CallableArgumentIndexer(), stream, resultRef, () -> {
                    off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                }, null);
            }, null);
        }, null);
        return result;
    }

}

class FromBlock extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("From");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        off(new XLiteralGrammarElement("from"), stream, resultRef, () -> {
            off(new XLiteralGrammarElement("\\("), stream, resultRef, () -> {
                on(new ArgumentIndexer(), stream, resultRef, () -> {
                    off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                }, () -> {
                    on(new Expression(), stream, resultRef, () -> {
                        off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                    }, () -> {
                        on(new Identifier(), stream, resultRef, null, null);
                    });
                });
            }, null);
        }, null);
        return result;
    }

}

class SelectBlock extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("Select");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        off(new XLiteralGrammarElement("select"), stream, resultRef, () -> {
            off(new XLiteralGrammarElement("\\("), stream, resultRef, () -> {
                on(new XRepetitiveGrammarElement(new Identifier(), RepetitiveType.Plus), stream, resultRef, () -> {
                    off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                }, null);
            }, null);
        }, null);
        return result;
    }

}

class Identifier extends XLiteralGrammarElement {

    public Identifier() {
        super("[A-Za-z_][A-Za-z0-9_]*");
        setName("Identifier");
    }
}

class Integer extends XLiteralGrammarElement {

    public Integer() {
        super("[0-9]+");
        setName("Integer");
    }
}

class CallableArgumentIndexer extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("CallableArgumentIndexer");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        on(new ArgumentIndexer(), stream, resultRef, () -> {
            off(new XLiteralGrammarElement("\\("), stream, resultRef, () -> {
                on(new XRepetitiveGrammarElement(new Identifier(), RepetitiveType.Plus), stream, resultRef, () -> {
                    off(new XLiteralGrammarElement("\\)"), stream, resultRef, null, null);
                }, null);
            }, null);
        }, null);
        return result;
    }

}

class ArgumentIndexer extends XGrammarElement {

    @Override
    public XParseResult validate(XRetractableBufferedTokenStream stream) {
        XParseResult result = new XParseResult("ArgumentIndexer");
        RefSupport<XParseResult> resultRef = new RefSupport<>(result);
        off(new XLiteralGrammarElement("\\{"), stream, resultRef, () -> {
            on(new Integer(), stream, resultRef, () -> {
                off(new XLiteralGrammarElement("\\}"), stream, resultRef, null, null);
            }, null);
        }, null);
        return result;
    }

}
