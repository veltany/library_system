
package com.miva.packages.sort;

import java.util.Comparator;

/**
 *
 * Sort Interface
 */

public interface Sort {

    public <T> void sort(T[] arr, Comparator<T> comp, boolean isAscending);
}
