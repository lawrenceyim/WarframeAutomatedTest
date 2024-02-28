package com.lawrenceyim.testing.api;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.lawrenceyim.testing.api.GetArchonHuntByPlatform;
import com.zebrunner.carina.api.apitools.validation.JsonComparatorContext;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ArchonHuntTest {
    private String activationRegexPattern = "\\d{4}-\\d{2}-\\d{2}T00:00:00.000Z";
    private String startRegexPattern = "-\\dd \\d{1,2}h \\d{1,2}m \\d{1,2}s";
    private String expiryRegexPattern = "\\d{4}-\\d{2}-\\d{2}T00:00:00.000Z";
    private String etaRegexPattern = "\\dd \\d{1,2}h \\d{1,2}m \\d{1,2}s";
    private Predicate<String> activationPredicate;
    private Predicate<String> startPredicate;
    private Predicate<String> expiryPredicate;
    private Predicate<String> etaPredicate;

    @BeforeClass
    public void setUpRegex() {
        activationPredicate = Pattern.compile(activationRegexPattern).asPredicate();
        startPredicate = Pattern.compile(startRegexPattern).asPredicate();
        expiryPredicate = Pattern.compile(expiryRegexPattern).asPredicate();
        etaPredicate = Pattern.compile(etaRegexPattern).asPredicate();
    }

    @DataProvider(name = "platformProvider")
    public Object[][] platformProvider() {
        return new Object[][] {{"pc"}, {"ps4"}, {"xb1"}, {"swi"}};
    }

    @Test(description = "Test if the API call for getting current Archon hunt data works.",
            dataProvider = "platformProvider")
    public void verifyGetArchonHuntByPlatformTest(String platform) {
        GetArchonHuntByPlatform getArchonHuntByPlatform = new GetArchonHuntByPlatform(platform);
        JsonComparatorContext comparatorContext = new JsonComparatorContext().context()
                .<String>withPredicate("activationPredicate", date -> activationPredicate.test(date))
                .<String>withPredicate("startPredicate", date -> startPredicate.test(date))
                .<String>withPredicate("expiryPredicate", date -> expiryPredicate.test(date))
                .<String>withPredicate("etaPredicate", date -> etaPredicate.test(date));

        getArchonHuntByPlatform.expectResponseStatus(HttpResponseStatusType.OK_200);
        getArchonHuntByPlatform.callAPI();
        getArchonHuntByPlatform.validateResponse(comparatorContext);
    }

    @Test(description = "Test if the API call rejeects an invalid platform.")
    public void verifyGetArchonHuntWithInvalidPlatformTest() {
        GetArchonHuntByPlatform getArchonHuntByPlatform = new GetArchonHuntByPlatform("xbox");
        getArchonHuntByPlatform.expectResponseStatus(HttpResponseStatusType.NOT_FOUND_404);
    }
}
