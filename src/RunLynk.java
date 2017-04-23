
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
        ArrayList arg0 = new ArrayList(java.util.Arrays.asList(1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5));
        String expression = "all({3}) from(select(x) from({0}) where({1}(x)) distinct(x) compare({2}))";
        
        ParseTreeNode root = new ExpressionParser().parseExpression(expression);
        System.out.println(root);
        
        Collection result = Collections.evaluate(
                expression, 
                arg0, 
                new PredicateWrapper<>((Integer i) -> i > 0),
                new BiFunctionWrapper<>((Integer arg1, Integer arg2) -> arg1.equals(arg2))
        );
        for (Object object : result) {
            System.out.println(object);
        }
    }

}
