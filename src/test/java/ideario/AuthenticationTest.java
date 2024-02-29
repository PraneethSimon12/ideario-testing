package ideario;

import org.openqa.selenium.By;
import org.testng.annotations.*;

public class AuthenticationTest extends BaseTestClass{

    @Test(groups = "GetStartedTests")
    public void openGetStartedMenu() {

        logger.info("Opening Get Started menu.");

        helper.clickElement(By.xpath("//button[text()='Get started']"));
        logger.info("Get Started menu opened successfully.");
    }

    @Test(groups = "GetStartedTests", dependsOnMethods = "openGetStartedMenu")
    public void checkIfStartedIsMenu() {
        logger.info("Checking if Get Started menu is displayed.");

        softAssert.assertTrue(helper.isElementDisplayed(By.className("cl-card")), "Get started menu is not displayed");
        softAssert.assertTrue(helper.isElementDisplayed(By.cssSelector("input[name='identifier']")),
                "Username field is not displayed");
        softAssert.assertTrue(helper.isElementDisplayed(By.cssSelector("button[aria-label='Close modal']")),
                "Cross button is not displayed");

        helper.clickElement(By.xpath("//a[text()='Sign up']"));

        softAssert.assertTrue(helper.isElementDisplayed(By.cssSelector("input[name='firstName']")),
                "First name field is not displayed");
        softAssert.assertTrue(helper.isElementDisplayed(By.cssSelector("input[name='lastName']")),
                "Last name field is not displayed");
        softAssert.assertTrue(helper.isElementDisplayed(By.cssSelector("input[name='username']")),
                "Username field is not displayed");
        softAssert.assertTrue(helper.isElementDisplayed(By.cssSelector("input[name='password']")),
                "Password field is not displayed");

        softAssert.assertTrue(
                helper.isElementDisplayed(By.cssSelector("button[data-localization-key='formButtonPrimary']")),
                "Sign Up button is not displayed");

        softAssert.assertAll();
    }

    @Test(groups = "GetStartedTests", dependsOnMethods = "checkIfStartedIsMenu")
    public void checkCrossButtonOfGetStartedMenu() {
        logger.info("Checking if cross button of Get Started menu is displayed.");

        helper.clickElement(By.cssSelector("button[aria-label='Close modal']"));

        softAssert.assertTrue(helper.isElementInvisible(By.className("cl-card")),
                "Get started menu is still displayed");
        softAssert.assertAll();
    }

    @Test(groups = "SignUpTests", dependsOnMethods = "openGetStartedMenu")
    public void openSignUpMenu() {
        logger.info("Opening Sign Up menu.");
        helper.clickElement(By.xpath("//a[text()='Sign up']"));
    }

    @Test(groups = "SignUpTests", dependsOnMethods = "openSignUpMenu")
    public void verifySignUpMenuOpening() {
        logger.info("Verifying if Sign Up menu is displayed.");

        String signUpTextValue = helper
                .getElementText(By.cssSelector("h1[data-localization-key='signUp.start.title']"));

        softAssert.assertEquals(signUpTextValue, "Create your account", "Sign Up menu is not displayed.");
        softAssert.assertAll();
    }

    @DataProvider(name="commonPasswordsUserData")
    public Object[][] createUserData() {
        return new Object[][] {
            { "John", "Doe", "johnDoe", "password" },
            { "Jane", "Doe", "janeDoe", "Password" },
            { "Jim", "Beam", "jimBeam", "PASSWORD" },
            { "Jack", "Daniels", "jackDaniels", "12345678" },
            { "Johnny", "Walker", "johnnyWalker", "abcdefgh" }
        };
    }

    @Test(groups = "SignUpTests", dependsOnMethods = "verifySignUpMenuOpening", dataProvider = "commonPasswordsUserData")
    public void verifyCommonPasswordCasesSignUp(String firstName, String lastName, String username, String password) throws InterruptedException {

        logger.info("Starting Sign Up test case for user: " + username);


        logger.info("Checking password: " + password);
        helper.enterUserFields(firstName, lastName, username, password);
        helper.clickButton("button[data-localization-key='formButtonPrimary']");
        Thread.sleep(2000);
        softAssert.assertEquals(helper.getElementText(By.cssSelector(".cl-formFieldErrorText")),
                "This password has been found as part of a breach and can not be used, please try another password instead.",
                "Password validation message is not displayed for password: " + password);
        
        softAssert.assertAll();
    }

    @Test(groups = "SignUpTests", dependsOnMethods = "verifyCommonPasswordCasesSignUp")
    public void verifyBoundaryPasswordCasesSignUp() {

        logger.info("Checking edge cases for Sign In.");

        String[] passwords = { helper.generateDummyPassword(7), helper.generateDummyPassword(8),
                helper.generateDummyPassword(9) };

        for (int i = 0; i < passwords.length; i++) {
            helper.enterText("input[name='password']", passwords[i]);
            if (i == 0) {

                helper.clickButton("button[data-localization-key='formButtonPrimary']");

                softAssert.assertEquals(helper.getElementText(By.cssSelector(".cl-formFieldErrorText")),
                        "Your password must contain 8 or more characters.",
                        "Password validation message is not displayed for password: " + passwords[i]);
            } else {
                softAssert.assertEquals(helper.getElementText(By.cssSelector(".cl-formFieldSuccessText")),
                        "Your password meets all the necessary requirements.",
                        "Password validation message is not displayed for password: " + passwords[i]);
            }
        }
        softAssert.assertAll();
    }

    @Test(groups = "SignUpTests", dependsOnMethods = "verifyBoundaryPasswordCasesSignUp")
    @Parameters("existingUsername")
    public void checkForUsedUsernameCase(String existingUsername) {

        logger.info("Checking for used username case.");

        helper.enterUserFields("John", "Doe", existingUsername, helper.generateDummyPassword(10));
        helper.clickButton("button[data-localization-key='formButtonPrimary']");

        softAssert.assertEquals(helper.getElementText(By.cssSelector(".cl-formFieldErrorText")),
                "That username is taken. Please try another.",
                "Username validation message is not displayed for used username.");
        softAssert.assertAll();
    }

    @Test(groups = "SignUpTests", dependsOnMethods = "checkForUsedUsernameCase")
    @Parameters({ "firstName", "lastName", "username", "password" })
    public void createUser(String firstName, String lastName, String username, String password) {

        logger.info("Creating user with details: " + firstName + " " + lastName + " " + username + " " + password);

        helper.enterUserFields(firstName, lastName, username, password);
        helper.clickButton("button[data-localization-key='formButtonPrimary']");

        softAssert.assertTrue(
                helper.isElementDisplayed(By.xpath("/html/body/div[1]/div/div[2]/div/div/button/div/img")),
                "User is not created successfully.");
        logger.info("User created successfully.");
        softAssert.assertAll();
    }

    @Test(groups = "LoginTests", dependsOnMethods = "openGetStartedMenu")
    @Parameters( "invalidUsername" )
    public void checkForInvalidUsernameCase(String invalidUsername) {

        logger.info("Checking for invalid username case.");

        helper.enterText("input[name='identifier']", invalidUsername);
        helper.clickButton(".cl-formButtonPrimary");

        softAssert.assertEquals(helper.getElementText(By.cssSelector(".cl-formFieldErrorText")),
                "Couldn't find your account.",
                "Username validation message is not displayed for invalid username.");
        softAssert.assertAll();
    }

    @Test(groups = "LoginTests", dependsOnMethods = {"openGetStartedMenu", "checkForInvalidUsernameCase"})
    @Parameters({"username", "invalidPassword"})
    public void checkForInvalidPassword(String username, String invalidPassword){
            
            logger.info("Checking for invalid password case.");
    
            helper.enterText("input[name='identifier']", username);
            helper.clickButton(".cl-formButtonPrimary");
    
            helper.enterTextAfterWait(By.cssSelector("input[name='password']"), invalidPassword);
            
            helper.clickButton(".cl-formButtonPrimary");
    
            softAssert.assertEquals(helper.getElementText(By.cssSelector(".cl-formFieldErrorText")),
                    "Password is incorrect. Try again, or use another method.",
                    "Password validation message is not displayed for invalid password.");

            helper.clickButton(".cl-identityPreviewEditButton");

            softAssert.assertAll();
    }

    @Test(groups = "LoginTests", dependsOnMethods = {"checkForInvalidPassword"})
    @Parameters({ "username", "password" })
    public void loginTest(String username, String password){

        helper.enterText("input[name='identifier']", username);
        helper.clickButton(".cl-formButtonPrimary");

        helper.enterTextAfterWait(By.cssSelector("input[name='password']"), password);
        
        helper.clickButton(".cl-formButtonPrimary");
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
