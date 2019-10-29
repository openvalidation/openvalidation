package io.openvalidation.test.rest.model.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import io.openvalidation.rest.model.dto.AliasesWithOperatorsDTO;
import org.junit.jupiter.api.Test;

public class AliasesWithOperatorsDTOTest {
  @Test
  public void AliasesWithOperatorsDTO_with_german_expected_not_empty_list() {
    AliasesWithOperatorsDTO aliases = new AliasesWithOperatorsDTO("de");

    assertThat(aliases.getAliases().size(), not(0));
    assertThat(aliases.getOperators().size(), not(0));
  }

  @Test
  public void AliasesWithOperatorsDTO_with_english_expected_not_empty_list() {
    AliasesWithOperatorsDTO aliases = new AliasesWithOperatorsDTO("en");

    assertThat(aliases.getAliases().size(), not(0));
    assertThat(aliases.getOperators().size(), not(0));
  }
}
