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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsUtils {

  public static String getGenericTypeName(Class<?> cls) {
    return getGenericTypeName(cls, 0);
  }

  public static String getGenericTypeName(Class<?> cls, int position) {
    Type type = getGenericType(cls, position);

    return (type != null) ? type.getTypeName() : null;
  }

  public static Type getGenericType(Class<?> cls, int position) {

    if (cls.getGenericSuperclass() != null
        && cls.getGenericSuperclass() instanceof ParameterizedType) {
      ParameterizedType type = ((ParameterizedType) cls.getGenericSuperclass());

      if (type != null
          && type.getActualTypeArguments() != null
          && type.getActualTypeArguments().length > position) {

        return type.getActualTypeArguments()[position];
      }
    }

    return null;
  }

  public static <T> Class<T> getGenericClass(Class<?> cls, int position) {
    Type type = getGenericType(cls, position);

    return (type != null) ? (Class<T>) type : null;
  }

  public static <T> T cast(Object obj, Class<T> cls) {
    return (obj != null && cls.isInstance(obj)) ? (T) obj : null;
  }
}
