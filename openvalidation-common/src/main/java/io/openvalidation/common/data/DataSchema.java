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

package io.openvalidation.common.data;

import java.util.*;
import java.util.stream.Collectors;

public class DataSchema {

  private final String marker = "#123#<><><>#123#";

  private List<DataPropertyBase> _properties = new ArrayList<>();
  private HashMap<String, DataProperty> uniqueProperties = new HashMap<>();

  public void complete(List<DataVariableReference> dataVariableReferences) {
    addVariables(dataVariableReferences);
    sort();
  }

  public void add(DataPropertyBase property) {
    this._properties.add(property);
  }

  public void addProperty(DataProperty prop) {
    this._properties.add(prop);
  }

  public void addProperty(String name, String path, DataPropertyType type) {
    DataPropertyBase prop = null;

    DataPropertyBase parent = this.getArrayParentProperty(path);

    if (parent != null) {
      String propPath =
          (parent instanceof DataArrayProperty)
              ? ((DataArrayProperty) parent).getFullPathExceptArrayPath()
              : "";
      String arrayPath =
          (parent instanceof DataArrayProperty)
              ? ((DataArrayProperty) parent).getArrayPath()
              : path;

      prop = new DataArrayProperty(name, propPath, arrayPath, type);
    } else prop = new DataProperty(name, path, type);

    this._properties.add(prop);
  }

  public void addVariable(String name, DataPropertyType type) {
    this.addVariable(new DataVariableReference(name, type));
  }

  public void addVariable(DataVariableReference variable) {
    this._properties.add(variable);
  }

  public void addVariables(List<DataVariableReference> variables) {
    this._properties.addAll(variables);
  }

  public DataPropertyType assertVariableType(DataVariableReference ref) {
    Optional<DataPropertyBase> firstPlausibleMatch =
        this._properties.stream()
            .filter(
                prop ->
                    (prop != ref)
                        && (ref.getOriginText()
                            .toLowerCase()
                            .contains(prop.getFullNameLowerCase())))
            .findFirst();
    if (firstPlausibleMatch.isPresent()) {
      return firstPlausibleMatch.get().getType();
    }
    return DataPropertyType.String;
  }

  public List<DataProperty> getProperties() {
    return this._properties.stream()
        .filter(p -> p instanceof DataProperty)
        .map(p -> (DataProperty) p)
        .collect(Collectors.toList());
  }

  public List<DataPropertyBase> getAllProperties() {
    return this._properties.stream()
        .filter(p -> p instanceof DataProperty || p instanceof DataArrayProperty)
        .collect(Collectors.toList());
  }

  public List<DataVariableReference> getVariableReferences() {
    return this._properties.stream()
        .filter(p -> p instanceof DataVariableReference)
        .map(p -> (DataVariableReference) p)
        .collect(Collectors.toList());
  }

  public List<DataArrayProperty> getArrayProperties() {
    return this._properties.stream()
        .filter(p -> p instanceof DataArrayProperty)
        .map(p -> (DataArrayProperty) p)
        .collect(Collectors.toList());
  }

  public boolean isArrayAccessor(String path) {
    if (path.contains(".")) {
      String pth = path.substring(0, path.lastIndexOf("."));

      Optional<DataPropertyBase> prop =
          this._properties.stream()
              .filter(p -> p.getType() == DataPropertyType.Array)
              .filter(p -> p.getFullName().toLowerCase().equals(pth.toLowerCase()))
              .findFirst();

      return (prop != null && prop.isPresent());
    }

    return false;
  }

  public List<String> getAllNames() {
    return this._properties.stream().map(p -> p.getName()).collect(Collectors.toList());
  }

  public boolean exists(String fullName) {
    DataPropertyBase prop = this.findByFullName(fullName);

    return (prop != null);
  }

  public boolean oneOf(String name) {
    if (name != null) {
      for (String n : name.split(" ")) {
        if (this.exists(n)) return true;
      }
    }

    return false;
  }

  public boolean isLambdaPropertyOfArray(String partialPropertyName) {
    if (this.getArrayProperties() != null
        && partialPropertyName != null
        && partialPropertyName.trim().length() > 0) {
      return this.getArrayProperties().stream()
          .anyMatch(a -> a.getFullNameLowerCase().endsWith(partialPropertyName.trim()));
    }

    return false;
  }

  public boolean isPropertyOfArray(String fullName) {
    return getArrayParentProperty(fullName) != null;
  }

  public DataPropertyBase getArrayParentProperty(String fullName) {
    if (fullName != null && fullName.length() > 0 && this.exists(fullName)) {

      DataPropertyBase prop = this.findByFullName(fullName);

      if (prop != null
          && (prop.getType() == DataPropertyType.Array || prop instanceof DataArrayProperty))
        return prop;
    }

    return null;
  }

  public List<DataProperty> getPropertiesByPath(String path) {
    return this.getProperties().stream()
        .filter(p -> p.getPath().toLowerCase().equals(path.toLowerCase()))
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");

    List<DataPropertyBase> dataProperties = getAllProperties();
    for (int i = 0; i < dataProperties.size(); i++) {
      sb.append(dataProperties.get(i).toString());
      if (i < dataProperties.size() - 1) sb.append(", ");
    }
    sb.append("] [");
    List<DataVariableReference> references = getVariableReferences();
    for (int i = 0; i < references.size(); i++) {
      sb.append(references.get(i).toString());
      if (i < references.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  public void determineUniqueProperties() {
    List<DataProperty> properties = getProperties();
    boolean unique;
    for (DataProperty property : properties) {
      String propertyName = property.getName();
      String propertyPath = property.getPath();

      unique = true;

      for (int j = 0; j < properties.size(); j++) {
        DataProperty otherProp = properties.get(j);
        if (otherProp.getFullName().startsWith(propertyPath)) {
          int nextPartPos = property.getFullNameAsParts().length;
          for (int k = nextPartPos; k < otherProp.getFullNameAsParts().length; k++) {
            if (otherProp.getFullNameAsParts()[k].equals(propertyName)) {
              unique = false;
            }
          }
        } else {
          if (otherProp.getFullName().contains(propertyName)) {
            unique = false;
          }
        }
        if (!unique) break;
      }
      if (unique) {
        uniqueProperties.put(propertyName, property);
      }
    }
  }

  public DataPropertyBase resolve(String content) {
    if (content != null) {
      DataPropertyBase property;

      String cnt = maskInput(content);
      Optional<DataPropertyBase> prop =
          this._properties.stream()
              .filter(
                  p -> {
                    return cnt.contains(
                        marker + p.getFullNameLowerCase().replace(" ", marker) + marker);
                  })
              .findFirst();
      property = prop.isPresent() ? prop.get() : null;
      if (property != null) return property;

      // todo lazevedo 30.7.19 currently explicit properties are of higher priority than unique ones
      DataPropertyBase uniqueProperty;
      uniqueProperty = lookUpInUniqueProperties(content);
      return uniqueProperty;
    }

    return null;
  }

  public List<DataPropertyBase> resolveAll(String content) {
    if (content != null) {

      // determine explicits
      Map<String, DataPropertyBase> explicitProps = resolveAllExplicitProperties(content);

      // determine uniques
      Map<String, DataPropertyBase> uniqueProps = resolveAllUniqueProperties(content);

      // determine shortcuts
      Map<String, DataPropertyBase> shortcutProps = resolveAllShortcutProperties(content);

      Map<String, DataPropertyBase> allResolved = new HashMap<>();
      allResolved.putAll(explicitProps);
      allResolved.putAll(uniqueProps);
      allResolved.putAll(shortcutProps);

      return new ArrayList<>(allResolved.values());
    }

    return null;
  }

  private Map<String, DataPropertyBase> resolveAllExplicitProperties(String inputContent) {
    String content = maskInput(inputContent);

    return this._properties.stream()
        .filter(
            p -> content.contains(marker + p.getFullNameLowerCase().replace(" ", marker) + marker))
        .collect(Collectors.toMap(DataPropertyBase::getFullName, p -> p));
  }

  private Map<String, DataPropertyBase> resolveAllUniqueProperties(String inputContent) {
    String content = maskInput(inputContent);

    return this.uniqueProperties.entrySet().stream()
        .filter(
            p ->
                content.contains(
                    marker
                        + p.getKey().toLowerCase().replace(" ", marker).replace("_", marker)
                        + marker))
        .map(e -> e.getValue())
        .collect(Collectors.toMap(p -> p.getFullName(), p -> p));
  }

  private Map<String, DataPropertyBase> resolveAllShortcutProperties(String inputContent) {
    String content = maskInput(inputContent);

    List<DataProperty> allShortcutsFromUnique = lookUpAllShortCutAtStart(content);
    // take Person.Age where Person is unique. Person then is a shortcut for e.g. Contract.Person,
    // so we want to replace Person.Age by Contract.Person.Age
    if (!allShortcutsFromUnique.isEmpty()) {
      for (DataProperty shortcutProperty : allShortcutsFromUnique) {
        if (!shortcutProperty.getPath().isEmpty()) {
          content =
              (content)
                  .replaceAll(
                      marker + shortcutProperty.getName() + "\\.",
                      marker + shortcutProperty.getFullName() + ".");
        }
      }
    }

    return resolveAllExplicitProperties(content);
  }

  /**
   * This method looks up if the given content refers to a unique property of a schema. It first
   * checks if it begins with a shortcut. If it does it replaces the shortcut by its full path. E.g.
   * is may resolve 'Person.Age' to 'Contract.Person.Age' where Person is the shortcut for
   * Contract.Person. It then resolves the replaced content like any other content If it doesnt it
   * just checks if the given single word is a shortcut itself and returns the referenced unique
   * proeprty instantly.
   *
   * @param inputContent The input that may reference a unique property in a JSON or YAML schema.
   * @return A resolved object of type DataPropertyBase if resolution was successful. Else null.
   */
  private DataPropertyBase lookUpInUniqueProperties(String inputContent) {
    DataPropertyBase propertyBase;

    String content = maskInput(inputContent);
    DataProperty shortcutFromUnique = lookUpShortCutAtStart(content);
    // take Person.Age where Person is unique. Person then is a shortcut for e.g. Contract.Person,
    // so we want to replace Person.Age by Contract.Person.Age
    if (shortcutFromUnique != null && !shortcutFromUnique.getPath().isEmpty()) {
      inputContent =
          (content)
              .replaceFirst(
                  marker + "[^.]+" + "\\.", marker + shortcutFromUnique.getFullName() + ".");
      propertyBase = resolve(inputContent);
    } else {
      Optional<Map.Entry<String, DataProperty>> entry =
          this.uniqueProperties.entrySet().stream()
              .filter(
                  p -> {
                    return content.contains(
                        marker
                            + p.getKey().toLowerCase().replace(" ", marker).replace("_", marker)
                            + marker);
                  })
              .findFirst();

      propertyBase = entry.isPresent() ? entry.get().getValue() : null;
    }

    return propertyBase;
  }

  private DataProperty lookUpShortCutAtStart(String inputContent) {
    if (inputContent.contains(".")) {
      for (Map.Entry<String, DataProperty> e : uniqueProperties.entrySet()) {
        if (inputContent.contains(marker + e.getKey().toLowerCase() + ".")) {
          return e.getValue();
        }
      }
    }
    return null;
  }

  private List<DataProperty> lookUpAllShortCutAtStart(String inputContent) {
    List<DataProperty> shortCutProperties = new ArrayList<>();
    if (inputContent.contains(".")) {
      for (Map.Entry<String, DataProperty> e : uniqueProperties.entrySet()) {
        if (inputContent.contains(marker + e.getKey().toLowerCase() + ".")) {
          shortCutProperties.add(e.getValue());
        }
      }
    }
    return shortCutProperties;
  }

  private String maskInput(String input) {
    return marker
        + input
            .toLowerCase()
            .replaceAll(" ", marker)
            .replaceAll("\n", marker)
            // replace noise and parentheses (necessary for arithmetic content)
            .replaceFirst("([^(]*\\()+", "")
            .replaceFirst("(\\)+[^)]*)+", "")
        + marker;
  }

  public String filterPropertyString(String content) {
    return filterPropertyString("", content);
  }

  // returns name of Property without the scope property
  public String filterPropertyString(String scope, String content) {
    if (content != null && scope != null) {
      String propertyPath = null;
      String scopePrefix = (!scope.isEmpty() ? scope + "." : "").toLowerCase();

      String cnt = maskInput(content);

      Optional<DataPropertyBase> prop =
          this._properties.stream()
              .filter(
                  p ->
                      p.getFullNameLowerCase().startsWith(scopePrefix)
                          && cnt.contains(
                              marker
                                  + p.getFullNameLowerCase()
                                      .replace(scopePrefix, "")
                                      .replace(" ", marker)
                                      .replace("_", marker)
                                  + marker))
              .findFirst();

      // if prop exists strip prefix form return value
      propertyPath =
          (prop != null && prop.isPresent())
              ? prop.get().getFullName().substring(scopePrefix.length())
              : null;
      if (propertyPath != null) return propertyPath;

      if (scopePrefix.length() == 0) {
        DataPropertyBase uniqueProperty = lookUpInUniqueProperties(content);
        propertyPath =
            uniqueProperty != null
                ? uniqueProperty.getFullName().substring(scopePrefix.length())
                : null;
      }

      return propertyPath;
    }
    return null;
  }

  public void sort() {
    List<String> keys =
        this._properties.stream().map(p -> p.getFullNameLowerCase()).collect(Collectors.toList());

    keys = keys.stream().sorted(Comparator.comparing(String::length)).collect(Collectors.toList());

    Collections.reverse(keys);

    List<DataPropertyBase> sorted = new ArrayList<>();

    keys.forEach(
        k -> {
          sorted.add(this.findByFullName(k));
        });

    this._properties = sorted;
  }

  public DataPropertyBase getPropertyIfIsInPath(String part) {
    if (this.getProperties() != null) {
      for (DataProperty prop : this.getProperties()) {
        if (prop.getFullNameAsParts() != null
            && Arrays.stream(prop.getFullNameAsParts()).anyMatch(p -> p.equals(part))) return prop;
      }
    }

    return null;
  }

  // old stuff
  public DataProperty findPropertyByFullName(String fullName) {
    DataPropertyBase prop = this.findByFullName(fullName);

    return (prop != null && prop instanceof DataProperty) ? (DataProperty) prop : null;
  }

  public DataVariableReference findVariableByFullName(String fullName) {
    DataPropertyBase prop = this.findByFullName(fullName);

    return (prop != null && prop instanceof DataVariableReference)
        ? (DataVariableReference) prop
        : null;
  }

  public DataPropertyBase findByFullName(String fullName) {
    String fl = fullName.toLowerCase();

    Optional<DataPropertyBase> prop =
        this._properties.stream().filter(p -> p.getFullNameLowerCase().equals(fl)).findFirst();

    return (prop != null && prop.isPresent()) ? prop.get() : null;
  }

  public DataPropertyBase extract(String name) {
    if (name != null) {
      for (String n : name.split(" ")) {

        DataPropertyBase prop = this.findByFullName(n);
        if (prop != null) return prop;
      }
    }

    return null;
  }

  public HashMap<String, DataProperty> getUniqueProperties() {
    return uniqueProperties;
  }
}
