/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package starhash.lynk;

import java.util.function.Predicate;

/**
 *
 * @author Harsh
 * @param <T>
 */
public class PredicateWrapper<T> {
    Predicate<T> wrappedFunctionalInterface;
    
    public PredicateWrapper(Predicate<T> toBeWrappedFunctionalInterface) {
        wrappedFunctionalInterface = toBeWrappedFunctionalInterface;
    }
    
    public boolean invoke(T arg) {
        return wrappedFunctionalInterface.test(arg);
    }
}
