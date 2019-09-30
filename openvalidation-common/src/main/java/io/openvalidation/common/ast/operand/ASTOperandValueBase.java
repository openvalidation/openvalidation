package io.openvalidation.common.ast.operand;

import io.openvalidation.common.ast.operand.property.ASTPropertyPart;
import io.openvalidation.common.ast.operand.property.ASTPropertyStaticPart;
import io.openvalidation.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ASTOperandValueBase extends ASTOperandBase {
  private List<ASTPropertyPart> path = new ArrayList<>();

  public ASTOperandValueBase add(ASTPropertyPart part) {
    this.path.add(part);
    return this;
  }

  public ASTOperandValueBase add(String... parts) {

    if (parts != null) {
      for (String p : parts) {
        ASTPropertyStaticPart propPart = new ASTPropertyStaticPart(p);
        propPart.setSource(p);
        this.path.add(propPart);
      }
    }

    return this;
  }

  public List<ASTPropertyPart> getPath() {
    return path;
  }

  public void setPath(List<ASTPropertyPart> path) {
    this.path = path;
  }

  public String[] getPathAsArray() {
    if (this.getPath() != null)
      return this.getPath().stream()
          .map(p -> p.getPart().toString())
          .collect(Collectors.toList())
          .toArray(new String[0]);

    return null;
  }

  public String getPathAsString() {
    if (this.path.size() > 0) {
      return String.join(
          ".", this.getPath().stream().map(p -> p.getPart()).collect(Collectors.toList()));
    }

    return "";
  }

  public String getFullPathAsString() {
    String name = this.getName();
    String path = this.getPathAsString();

    return StringUtils.isNullOrEmpty(path) ? name : path + "." + name;
  }
}
