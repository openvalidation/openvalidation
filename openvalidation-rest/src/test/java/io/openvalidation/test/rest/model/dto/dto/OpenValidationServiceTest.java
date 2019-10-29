// package io.openvalidation.test.rest;
//
// import OpenValidation;
// import OpenValidationParameters;
// import OpenValidationServiceImpl;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import java.util.Map;
//
// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//
// @ExtendWith(MockitoExtension.class)
// public class OpenValidationServiceTest {
//
//    @Mock
//    OpenValidation ovInstance;
//    private Map<String, String> wrongParams;
//    private Map<String, String> correctParams;
//    private Map<String, String> mixedParams;
//    @InjectMocks
//    private OpenValidationServiceImpl ovService;
//
//    @BeforeAll
//    public static void setUp() {
//
//    }
//
//    @BeforeEach
//    public void setUpMocks() {
//        wrongParams = TestUtils.wrongParams();
//        correctParams = TestUtils.correctParams();
//        mixedParams = TestUtils.mixedParams();
//        //when(ovInstance.generate()).thenReturn(new OpenValidationResult());
//        //TODO
//    }
//
//    //    @Test
//    public void should_forward_parameters_to_ov_instance() {
//        Map<String, String> params = TestUtils.correctParams();
//        OpenValidationParameters ovParams = OpenValidationParameters.of(params);
//        ovParams.setOpenValidationInstance(ovInstance);
//        assertDoesNotThrow(() -> {
//            ovService.generate(ovParams);
//        });
//        assertSchema();
//        assertRule();
//        assertCulture();
//        assertLanguage();
//        assertParams();
//        assertNotHelp();
//        assertNotVerbose();
//        assertNotAl();
//        assertNotBanner();
//        assertNotOutput();
//        assertNotSingleFile();
//    }
//
//    private void assertNotSingleFile() {
//        //verify(ovInstance).
//        //todo
//    }
//
//    private void assertNotOutput() {
//
//    }
//
//    private void assertNotBanner() {
//
//    }
//
//    private void assertNotAl() {
//    }
//
//    private void assertNotVerbose() {
//
//    }
//
//    private void assertNotHelp() {
//
//    }
//
//    private void assertParams() {
//
//    }
//
//    private void assertLanguage() {
//
//    }
//
//    private void assertCulture() {
//
//    }
//
//    private void assertRule() {
//
//    }
//
//    private void assertSchema() {
//
//    }
//
// }
