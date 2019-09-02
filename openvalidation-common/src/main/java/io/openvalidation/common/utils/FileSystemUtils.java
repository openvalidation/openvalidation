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

import io.openvalidation.common.model.ContentOptionKind;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class FileSystemUtils {
  public static final String NewLine = "\\n|\\r\\n";

  public static String readFile(String filePath) throws IOException {
    StringBuilder contentBuilder = new StringBuilder();
    // TODO java.io.UncheckedIOException: java.nio.charset.MalformedInputException: Input length =
    // 1java.nio.charset.MalformedInputException: Input length = 1  Input length = 1
    // Die Datei muss in UTF-8 sein damit der Fehler nicht auftritt
    Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8);
    stream.forEach(s -> contentBuilder.append(s).append("\n"));
    return contentBuilder.toString();
  }

  public static String readFileFromURL(String url) throws Exception {
    URL oracle = new URL(url);
    BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

    String out = "";
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      out += inputLine;
    }

    in.close();

    return out;
  }

  public static void writeFile(String filePath, String content) throws Exception {
    File f = new File(filePath);
    FileUtils.writeStringToFile(f, content, "UTF-8");
  }

  public static void fileShouldExists(String filePath, String error) throws FileNotFoundException {
    if (!fileExists(filePath)) {
      throw new FileNotFoundException(error);
    }
  }

  public static boolean fileExists(String filePath) {
    File f = new File(filePath);
    return (f.exists() && !f.isDirectory());
  }

  public static boolean isDirectory(String filePath) {
    return !isFile(filePath);
  }

  public static boolean isFile(String filePath) {
    String extension = FilenameUtils.getExtension(filePath);

    return (extension == null || !extension.isEmpty());
  }

  public static boolean isLocalFile(String filePath) {
    if (filePath == null) return false;
    if (filePath.startsWith("file:///")) return true;

    // todo 20190301 jgeske this accepts content like 'IF Person.Age GREATER 18 THEN ERROR as' as a
    // Filename
    String extension = FilenameUtils.getExtension(filePath.trim());
    boolean hasExtension = (extension == null || !extension.trim().isEmpty());

    boolean isUrl = filePath.contains("://");
    boolean hasNewLines = filePath.contains("\n");
    boolean hasSpace = extension.contains(" ");

    return hasExtension && !isUrl && !hasNewLines && !hasSpace && !filePath.contains("{");
  }

  public static boolean isURL(String filePath) {
    if (filePath == null) return false;

    return filePath.contains("://") && !filePath.contains("file://") && !filePath.contains("{");
  }

  public static String getDirectory(String filePath) {

    Path p = Paths.get(filePath);

    if (p != null && p.getParent() != null) return p.getParent().toString();

    return null;
  }

  public static String getWorkingDirectory() {
    return System.getProperty("user.dir");
  }

  public static String getExecutingDirectory() {
    String path = new File(".").getAbsolutePath();

    return path.endsWith(".") ? path.substring(0, path.length() - 1) : path;
  }

  public static void deleteFile(String file) throws Exception {
    FileUtils.forceDelete(new File(file));
  }

  public static ContentOptionKind getContentKind(String value) {
    if (isURL(value)) return ContentOptionKind.URL;
    if (isLocalFile(value)) return ContentOptionKind.FilePath;

    return ContentOptionKind.Content;
  }

  public static String getFileNameWithoutExtension(String filePathOrURL) {
    return FilenameUtils.getBaseName(filePathOrURL);
  }

  public static String combinePath(String first, String... more) {
    return Paths.get(first, more).toString();
  }

  public static List<String> getFilesFromDirectory(
      String dirname, String filterExt, boolean recursive) throws IOException {
    List<String> files = new ArrayList<>();

    Files.find(
            Paths.get(dirname),
            Integer.MAX_VALUE,
            (filePath, fileAttr) -> {
              return ((recursive) ? fileAttr.isRegularFile() : !fileAttr.isDirectory())
                  && filePath.toAbsolutePath().toString().endsWith("." + filterExt);
            })
        .forEach(
            f -> {
              files.add(f.toAbsolutePath().toString());
            });

    //        return Stream.of(new File(dirname).listFiles())
    //                .filter(file -> !file.isDirectory() && file.getName().endsWith("." +
    // filterExt))
    //                .map(File::getAbsolutePath)
    //                .collect(Collectors.toList());

    return files;
  }
}
