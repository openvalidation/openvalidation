package io.openvalidation.test.rest.model.dto.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataProperty;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.rest.model.dto.schema.ComplexDataPropertyDTO;
import io.openvalidation.rest.model.dto.schema.DataPropertyDTO;
import io.openvalidation.rest.model.dto.schema.SchemaDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SchemaDTOTest {

  @Test
  public void SchemaDTO_constructor_simple_decimal_object_expect_this_object() throws Exception {
    String jsonSchema = "{Alter: 20}";

    DataSchema schema = SchemaConverterFactory.convert(jsonSchema);
    SchemaDTO schemaDTO = new SchemaDTO(schema);

    List<DataPropertyDTO> expectedProperties = new ArrayList<>();
    expectedProperties.add(new DataPropertyDTO("Alter", DataPropertyType.Decimal));
    List<DataPropertyDTO> actualProperties = schemaDTO.getDataProperties();

    List<ComplexDataPropertyDTO> expectedComplexData = new ArrayList<>();
    List<ComplexDataPropertyDTO> actualComplexData = schemaDTO.getComplexData();

    assertThat(actualProperties, is(expectedProperties));
    assertThat(actualComplexData, is(expectedComplexData));
  }

  @Test
  public void SchemaDTO_constructor_complex_object_expect_this_object() throws Exception {
    String jsonSchema = "{Einkaufsliste: { Preis: 20 }}";

    DataSchema schema = SchemaConverterFactory.convert(jsonSchema);
    SchemaDTO schemaDTO = new SchemaDTO(schema);

    List<DataPropertyDTO> expectedProperties = new ArrayList<>();
    expectedProperties.add(new DataPropertyDTO("Einkaufsliste", DataPropertyType.Object));
    expectedProperties.add(new DataPropertyDTO("Einkaufsliste.Preis", DataPropertyType.Decimal));
    expectedProperties.add(new DataPropertyDTO("Preis", DataPropertyType.Decimal));
    List<DataPropertyDTO> actualProperties = schemaDTO.getDataProperties();

    List<ComplexDataPropertyDTO> expectedComplexData = new ArrayList<>();
    expectedComplexData.add(
        new ComplexDataPropertyDTO(
            new DataProperty("Preis", "Einkaufsliste", DataPropertyType.Decimal)));
    List<ComplexDataPropertyDTO> actualComplexData = schemaDTO.getComplexData();

    assertThat(actualProperties, is(expectedProperties));
    assertThat(actualComplexData, is(expectedComplexData));
  }
}
