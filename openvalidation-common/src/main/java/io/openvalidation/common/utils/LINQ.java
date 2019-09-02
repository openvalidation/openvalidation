/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.openvalidation.common.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LINQ {

  public static <T> int count(List<T> list, Predicate<? super T> predicate) {
    List<T> lst = where(list, predicate);

    return lst.size();
  }

  public static <T> List<T> where(T[] arr, Predicate<? super T> predicate) {
    if (arr != null) return LINQ.where(Arrays.stream(arr).collect(Collectors.toList()), predicate);

    return new ArrayList<>();
  }

  public static <T> List<T> where(List<T> list, Predicate<? super T> predicate) {
    if (list != null) {
      return list.stream().filter(predicate).collect(Collectors.toList());
    }

    return new ArrayList<>();
  }

  public static <T> boolean any(List<T> list, Predicate<? super T> predicate) {
    if (list != null) {
      return list.stream().anyMatch(predicate);
    }

    return false;
  }

  public static <T> T findFirst(List<T> list, Predicate<? super T> predicate) {
    if (list != null && list.size() > 0) {
      Optional<T> item = where(list, predicate).stream().findFirst();
      if (item != null && item.isPresent()) return item.get();
    }

    return null;
  }

  public static <T, R> List<R> ofType(List<T> list, Class<R> cls) {
    if (list != null) {
      return list.stream()
          .filter(e -> cls.isInstance(e))
          .map(e -> (R) e)
          .collect(Collectors.toList());
    }

    return new ArrayList<>();
  }

  public static <T> T findFirst(List<T> list) {
    if (list != null && list.size() > 0) {
      return list.get(0);
    }

    return null;
  }

  // bool selecte subitems, removes null values
  public static <T, R> List<R> select(T[] array, Function<? super T, ? extends R> mapper) {
    return select(Arrays.stream(array).collect(Collectors.toList()), mapper);
  }

  public static <T, R> List<R> select(List<T> list, Function<? super T, ? extends R> mapper) {
    List<R> files = new ArrayList<>();

    if (list != null) {
      files = list.stream().map(mapper).filter(i -> i != null).collect(Collectors.toList());
    }

    return files;
  }

  /*
  public static <T> T[] toArray(List<T> list){
      return (T[])list.toArray();
  }*/

  public static <T> List<T> toList(T... array) {
    return Arrays.stream(array).collect(Collectors.toList());
  }

  public static <T> List<T> findDuplicates(List<T> collection) {
    Set<T> uniques = new HashSet<>();
    return collection.stream().filter(e -> !uniques.add(e)).collect(Collectors.toList());
  }

  public static String[] array(String arrayPath) {
    List<String> lst = new ArrayList<>();

    if (!StringUtils.isNullOrEmpty(arrayPath)) lst.add(arrayPath);

    return lst.toArray(new String[0]);
  }
}
