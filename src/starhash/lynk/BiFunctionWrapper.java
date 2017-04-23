/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package starhash.lynk;

import java.util.function.BiFunction;

/**
 *
 * @author Harsh
 */
public class BiFunctionWrapper<T, U, R> {
    BiFunction<T, U, R> wrappedFunctionalInterface;
    
    public BiFunctionWrapper(BiFunction<T, U, R> toBeWrappedFunctionalInterface) {
        wrappedFunctionalInterface = toBeWrappedFunctionalInterface;
    }
    
    public R invoke(T arg0, U arg1) {
        return wrappedFunctionalInterface.apply(arg0, arg1);
    }
}
