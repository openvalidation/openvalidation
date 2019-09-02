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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ResourceUtils {
  private static final String cultureResourcePrefix = "aliases_";
  private static final String cultureResourceExt = ".properties";

  public static String getPathWithoutResourceName(String resourceName) {
    String path = getPath(resourceName, null);

    if (path != null) return path.substring(0, path.lastIndexOf('/'));

    return null;
  }

  public static String getPath(String resourceName) {
    return getPath(resourceName, null);
  }

  public static String getPath(String resourceName, Class classOfResourcePackage) {
    try {
      Class cls = (classOfResourcePackage == null) ? getCallerClass() : classOfResourcePackage;

      return String.format("classpath:" + resourceName).replace("classpath:/", "classpath:");
    } catch (Exception exp) {
      return null;
    }
  }

  public static String getResourceContentByClassPath(String resourceClassPath) {
    if (resourceClassPath == null) return null;

    try {
      String rp = resourceClassPath.replace("classpath:/", "classpath:");

      URL url = new URL(null, rp, new ClassPathURLHandler());
      return getContentFromInputStream(url.openStream());

    } catch (Exception exp) {
      return null;
    }
  }

  public static String getResourceConent(String resourceName) {
    return getResourceConent(resourceName, null);
  }

  public static String getResourceConent(String resourceName, Class classOfResourcePackage) {
    if (resourceName == null) return null;

    try {
      if (resourceName.startsWith("classpath")) return getResourceContentByClassPath(resourceName);

      Class cls = (classOfResourcePackage == null) ? getCallerClass() : classOfResourcePackage;
      return getContentFromInputStream(cls.getClassLoader().getResourceAsStream(resourceName));
    } catch (Exception e) {
      return null;
    }
  }

  public static boolean exists(String resourceName) {
    if (resourceName == null) return false;

    try {
      URL url = null;
      boolean streamNotNull = false;

      if (resourceName.startsWith("classpath")) {
        String rp = resourceName.replace("classpath:/", "classpath:");

        URL tmp = new URL(null, rp, new ClassPathURLHandler());
        InputStream stream = tmp.openStream();
        if (stream != null) {
          streamNotNull = true;
          stream.close();
        }
        return streamNotNull;
      } else {
        if (resourceName.startsWith("/")) resourceName = resourceName.substring(1);
        url = getCallerClass().getClassLoader().getResource(resourceName);

        //                if (url.toString().startsWith("jar:")){
        //                    String tmp = url.toString().replace("jar:", "");
        //
        //                    int str = tmp.indexOf("jar!/");
        //                    if (str > -1){
        //                        String pathToResource = tmp.substring(str+1);
        //                        tmp = tmp.replace(pathToResource, "");
        //                        int lastIndexOf = tmp.lastIndexOf("/");
        //                        if (lastIndexOf > -1) {
        //                            tmp = tmp.substring(0, lastIndexOf) + pathToResource;
        //                            url = new URL(null, tmp);
        //                        }
        //                    }
        //                }
      }

      return (url != null);
    } catch (Exception exp) {
      return false;
    }
  }

  //    public static String[] getAvailableLocales(String resourceName) throws Exception {
  //
  //
  //        PathMatchingResourcePatternResolver resolver = new
  // PathMatchingResourcePatternResolver();
  //        Resource[] resources = resolver.getResources("classpath*:" + resourceName +
  // "_*.properties");
  //
  //        if (resources != null ){
  //                return Arrays.stream(resources)
  //                        .filter(r -> r.getFilename().contains(resourceName + "_"))
  //                        .map(f -> f.getFilename().replace(resourceName + "_", "")
  //                                .replace(".properties", ""))
  //                        .collect(Collectors.toList()).toArray(new String[]{});
  //        }
  //
  //        return null;
  //    }

  //    public static boolean exists(String resourceName) throws Exception{
  //        PathMatchingResourcePatternResolver resolver = new
  // PathMatchingResourcePatternResolver();
  //        Resource[] resources = resolver.getResources("classpath*:" + resourceName);
  //
  //        return (resources != null && resources.length > 0);
  //    }

  public static Class getCallerClass() throws Exception {
    String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
    return Class.forName(callerClassName);
  }

  public static List<String> getAvailableCultures() {
    List<String> localesList = new ArrayList<>();
    Set<String> localesSet =
        Arrays.stream(Locale.getAvailableLocales())
            .map(Locale::getLanguage)
            .collect(Collectors.toSet());

    for (String lang : localesSet) {
      if (ResourceUtils.exists(cultureResourcePrefix + lang + cultureResourceExt)) {
        localesList.add(lang);
      }
    }
    return localesList;
  }

  private static String getContentFromInputStream(InputStream is) {
    if (is != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    return null;
  }
}
