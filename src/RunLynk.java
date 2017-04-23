
import Infinity.Grammar.Parsing.ParseTreeNode;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.stream.XMLStreamException;
import starhash.lynk.BiFunctionWrapper;
import starhash.lynk.Collections;
import starhash.lynk.PredicateWrapper;
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
public class RunLynk {

    /**
     * @param args the command line arguments
     * @throws javax.xml.stream.XMLStreamException
     */
    public static void main(String[] args) throws XMLStreamException {
        ArrayList arg0 = new ArrayList(java.util.Arrays.asList(2, 4, 4, 6, 6, 6, 8, 8, 8, 8, 9, 10));
        String expression = "all({3}) from(select(x) from({0}) where({1}(x)) distinct(x) compare({2}))";
        
        ParseTreeNode root = new ExpressionParser().parseExpression(expression);
        System.out.println(root);
        
        boolean result = Collections.evaluateBoolean(
                expression, 
                arg0, 
                new PredicateWrapper<>((Integer i) -> i > 0),
                new BiFunctionWrapper<>((Integer arg1, Integer arg2) -> arg1.equals(arg2)),
                new PredicateWrapper<>((Integer i) -> i % 2 == 0)
        );
        System.out.println("All elements above zero and divisible by 2? " + result);
    }

}
