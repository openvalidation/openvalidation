// package io.openvalidation.test.rest;
//
// import OpenValidationResult;
// import OpenValidationController;
// import OpenValidationResponseStatusException;
// import OpenValidationServiceImpl;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import java.util.HashMap;
// import java.util.Map;
//
// import static org.junit.jupiter.api.Assertions.assertThrows;
//
// @ExtendWith(MockitoExtension.class)
// public class OpenValidationControllerTest {
//
//    @Mock
//    OpenValidationServiceImpl ovService;
//
//    @InjectMocks
//    OpenValidationController ovController;
//
//    private OpenValidationResult ovResult;
//
//    @BeforeEach
//    private void setUpMocks() throws Exception {
//        ovResult = new OpenValidationResult();
//        //todo
//        // when(ovService.generate(any())).thenReturn(ovResult);
//    }
//
//    //@Test
//    public void should_forward_parameters_to_open_validation_service () throws Exception {
//
//        Map<String, String> goodParams = TestUtils.correctParams();
//
//        ovController.generate(goodParams);
//
//        Mockito.verify(ovService).generate(goodParams);
//    }
//
//
//    //@Test
//    public void should_throw_on_empty_params_or_null_params() {
//        assertThrows(OpenValidationResponseStatusException.class, () -> {
//            ovController.generate(null);
//        }, "OpenValidationController.generate(Map<tring, String> should throw
// OpenValidationResponseStatusException" +
//                " when called with null."); // TODO replace with message supplier and check
// correct status codes
//        assertThrows(OpenValidationResponseStatusException.class, () -> {
//            ovController.generate(new HashMap<>());
//        }, "OpenValidationController.generate(Map<tring, String> should throw
// OpenValidationResponseStatusException" +
//                " when called with empty parameter map.");
//    }
//
//    //TODO test für mixed params hängt von gewünschtem Verhalten des Controllers ab (check in
// service, controller oder
//    // forward/catch OVException
// }
