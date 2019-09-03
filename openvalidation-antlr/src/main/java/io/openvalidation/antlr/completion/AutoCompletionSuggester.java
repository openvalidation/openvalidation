package io.openvalidation.antlr.completion;

import java.util.Set;

public interface AutoCompletionSuggester {
  Set<Integer> suggestions(EditorContext editorContext);
}
