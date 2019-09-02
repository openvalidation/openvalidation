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

package io.openvalidation.core;

import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.ContentOptionKind;
import io.openvalidation.common.model.Language;
import io.openvalidation.common.utils.FileSystemUtils;
import io.openvalidation.common.validation.Validator;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;

public class OpenValidationOptions {

  private String _outputDirectory;
  private Locale _locale;
  private String _outCodeFileName;
  private ContentOptionKind _ruleOptionKind;
  private String _ruleContent;
  private boolean _verbose;
  private Language _language;
  private Map<String, Object> _params;
  private List<String> _workingDirectories;
  private DataSchema _schema;
  private boolean _singleFile;
  private boolean _hasFileOutput;

  // code class params...
  private String _packageName;
  private String _outImplementationClassName;
  private String _modelType;

  public OpenValidationOptions() {
    this._outputDirectory = FileSystemUtils.getExecutingDirectory();
    this._locale = Locale.getDefault();
    this._outCodeFileName = "OpenValidation";
    this._language = Language.Java;
    this._workingDirectories = new ArrayList<>();
    this.addWorkingDirectory(FileSystemUtils.getWorkingDirectory());
  }

  public String getOutputCodeFileName() {
    return this._outCodeFileName;
  }

  public void setOutputCodeFileName(String name) {
    this._outCodeFileName = name;
  }

  public String getOutputDirectory() {
    return this._outputDirectory.toLowerCase();
  }

  public void setVerbose(boolean verbose) {
    this._verbose = verbose;
  }

  public boolean isVerbose() {
    return this._verbose;
  }

  public Map<String, Object> getParams() {
    return this._params;
  }

  public void setParams(Map<String, Object> params) {
    this._params = params;

    this.adjustParams();
  }

  public void setParam(String paramName, String value) {
    if (this._params == null) this._params = new HashMap<>();

    this._params.put(paramName, value);
    this.adjustParams();
  }

  public void setParam(String paramName, Object value) {
    if (this._params == null) this._params = new HashMap<>();

    this._params.put(paramName, value);
    this.adjustParams();
  }

  public void setOutputDirectory(String fileOrFolderPath) {
    this.setFileOutput(true);

    if (FileSystemUtils.isFile(fileOrFolderPath)) {
      this._outCodeFileName = FileSystemUtils.getFileNameWithoutExtension(fileOrFolderPath);
      String dir = FileSystemUtils.getDirectory(fileOrFolderPath);

      if (dir != null) this._outputDirectory = dir;
    } else {
      this._outputDirectory = fileOrFolderPath;
    }
  }

  public String resolveCodeFileName() throws Exception {
    return ((Paths.get(this._outputDirectory, this._outCodeFileName)).toString()
        + "."
        + this.getOutCodeFileExtension(this.getLanguage()));
  }

  public String resolveCodeFileName(String name) throws Exception {
    return ((Paths.get(this._outputDirectory, name)).toString()
        + "."
        + this.getOutCodeFileExtension(this.getLanguage()));
  }

  public String resolveCodeFileName(Language language) throws Exception {
    return ((Paths.get(this._outputDirectory, this._outCodeFileName)).toString()
        + "."
        + this.getOutCodeFileExtension(language));
  }

  public Locale getLocale() {
    return this._locale;
  }

  public void setLocale(Locale locale) {
    this._locale = locale;
  }

  public void setLocale(String locale) throws Exception {

    if (Aliases.availableCultures.contains(locale)) {
      this.setLocale(new Locale(locale));
    } else {
      throw new OpenValidationException(
          "Language: '"
              + locale
              + "' could not be found. The following languages are currently available "
              + Aliases.availableCultures);
    }
  }

  public void setLanguage(Language _language) {
    this._language = _language;
  }

  public Language getLanguage() {
    return this._language;
  }

  public String getOutCodeFileExtension(Language framework) throws Exception {
    switch (framework) {
      case Java:
        return "java";
      case Node:
      case JavaScript:
        return "js";
      case CSharp:
        return "cs";
    }

    throw new OpenValidationException("Framework: " + framework + "could not be found");
  }

  public ContentOptionKind getRuleOptionKind() {
    return this._ruleOptionKind;
  }

  public String getRuleContent() {
    return this._ruleContent;
  }

  public void setRuleOption(String ruleContentOrURLOrFilePath) throws Exception {
    Validator.shouldNotBeEmpty(ruleContentOrURLOrFilePath, "the Rule Set");

    this._ruleOptionKind = FileSystemUtils.getContentKind(ruleContentOrURLOrFilePath.trim());
    this._ruleContent = ruleContentOrURLOrFilePath;

    if (this._ruleOptionKind == ContentOptionKind.URL
        || this._ruleOptionKind == ContentOptionKind.FilePath) {
      if (this._outCodeFileName == "OpenValidation")
        this._outCodeFileName =
            FileSystemUtils.getFileNameWithoutExtension(ruleContentOrURLOrFilePath);

      if (this._ruleOptionKind == ContentOptionKind.FilePath) {
        try {
          this.addWorkingDirectory(FileSystemUtils.getDirectory(this._ruleContent));
          this._ruleContent = _ruleContent.replace("\r", "\\r").replace("\n", "\\n");
        } catch (InvalidPathException e) {
          this._ruleOptionKind = ContentOptionKind.Content;
        }
      }
    }
  }

  public String[] getWorkingDirectories() {
    return this._workingDirectories.toArray(new String[0]);
  }

  public void setWorkingDirectories(String[] workingDirectories) {
    this._workingDirectories = Arrays.asList(workingDirectories);
  }

  public void addWorkingDirectory(String workingDirectory) {
    if (workingDirectory != null) this._workingDirectories.add(workingDirectory);
  }

  public DataSchema getSchema() {
    return this._schema;
  }

  public void setSchema(String schemaContentOrURLOrFilePath) throws Exception {
    Validator.shouldNotBeEmpty(schemaContentOrURLOrFilePath, "Schema");

    ContentOptionKind kind = FileSystemUtils.getContentKind(schemaContentOrURLOrFilePath);
    String schemaContent = schemaContentOrURLOrFilePath;

    if (kind == ContentOptionKind.URL) {
      throw new OpenValidationException(
          "NOT IMPLEMENTED: Reading Schema File from URL is not implemented!");
    } else if (kind == ContentOptionKind.FilePath) {
      try {
        schemaContent = FileSystemUtils.readFile(schemaContentOrURLOrFilePath);
      } catch (IOException e) {
        // TODO 21.8.19 SB establish logging: warn that file has not been found and kind reverts to
        // content
      }
    }
    this._schema = SchemaConverterFactory.convert(schemaContent);
  }

  public boolean isSingleFile() {
    return _singleFile;
  }

  public void setSingleFile(boolean _singleFile) {
    this._singleFile = _singleFile;
  }

  public boolean hasFileOutput() {
    return this._hasFileOutput;
  }

  public void setFileOutput(boolean hasFileOutput) {
    this._hasFileOutput = hasFileOutput;
  }

  public String getPackageName() {
    return _packageName;
  }

  public void setPackageName(String packageName) {
    this._packageName = packageName;
  }

  public String getOutImplementationClassName() {
    return _outImplementationClassName;
  }

  public void setOutImplementationClassName(String outImplementationClassName) {
    this._outImplementationClassName = outImplementationClassName;
  }

  public String getModelType() {
    return _modelType;
  }

  public void setModelType(String _modelType) {
    this._modelType = _modelType;
  }

  private void adjustParams() {
    if (this._params.containsKey("model_type"))
      this.setModelType(this._params.get("model_type").toString());

    if (this._params.containsKey("generated_class_namespace"))
      this.setPackageName(this._params.get("generated_class_namespace").toString());

    if (this._params.containsKey("generated_class_name"))
      this.setOutImplementationClassName(this._params.get("generated_class_name").toString());
  }
}
