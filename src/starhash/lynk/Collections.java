package starhash.lynk;

import Infinity.Grammar.Parsing.ParseTreeNode;
import Infinity.Utility.RefSupport;
import java.sql.Ref;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import starhash.lynk.expression.ExpressionParser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Harsh
 */
public class Collections {

    public static Collection evaluate(String expr, Object... args) {
        ParseTreeNode root = new ExpressionParser().parseExpression(expr);
        if (root != null) {
            if (root.getChild(0).getName().equals("SelectStatement")) {
                return evaluateSelectStatement(root.getChild(0), args);
            }
        }
        return null;
    }
    
    public static boolean evaluateBoolean(String expr, Object... args) {
        ParseTreeNode root = new ExpressionParser().parseExpression(expr);
        if (root != null) {
            if (root.getChild(0).getName().equals("AnyStatement")) {
                return evaulateAnyStatement(root.getChild(0), args);
            }
        }
        return false;
    }

    public static boolean evaulateAnyStatement(ParseTreeNode anyNode, Object... args) {
        int fromArgument = Integer.parseInt(anyNode.getNode("From").getChild(0, 0).getValue());
        int conditionalArgument = Integer.parseInt(anyNode.getNode("Any").getChild(0, 0, 0).getValue());
        Collection source = (Collection) args[fromArgument];
        PredicateWrapper condition = (PredicateWrapper)args[conditionalArgument];
        for (Object object : source) {
            if (condition.invoke(object))
                return true;
        }
        return false;
    }

    public static Collection evaluateSelectStatement(ParseTreeNode selectNode, Object... args) {
        Collection destination = new LinkedList();

        int fromArgument = Integer.parseInt(selectNode.getNode("From").getChild(0, 0).getValue());

        int whereArgument = Integer.parseInt(selectNode.getNode("Where").getChild(0, 0, 0).getValue());

        PredicateWrapper predicate = (PredicateWrapper) args[whereArgument];

        String whereVariable = selectNode.getNode("Where").getChild(0, 1, 0).getValue();

        String sourceElementVariable = selectNode.getChild(0, 0, 0).getValue();

        Collection source = (Collection) args[fromArgument];
        HashMap<String, RefSupport<Object>> variableMap = new HashMap<>();
        for (Iterator it = source.iterator(); it.hasNext();) {
            Object object = it.next();
            variableMap.put(sourceElementVariable, new RefSupport<>(object));
            if (predicate.invoke(object)) {
                if (selectNode.getNode("Distinct") != null) {
                    String which = selectNode.getNode("Distinct").getChild(0).getValue();
                    if (selectNode.getNode("Compare") != null) {
                        int whichCompare = Integer.parseInt(selectNode.getNode("Compare").getChild(0, 0).getValue());
                        BiFunctionWrapper compare = (BiFunctionWrapper) args[whichCompare];
                        if (evaluateBoolean("any({1}) from({0})", destination, new PredicateWrapper<>((Object arg) -> (Boolean)compare.invoke(arg, object)))) {
                            continue;
                        }
                    } else if (evaluateBoolean("any({1}) from({0})", destination, new PredicateWrapper<>((Object arg) -> arg.equals(object)))) {
                        continue;
                    }
                }
                destination.add(object);
            }
        }
        return destination;
    }
}
