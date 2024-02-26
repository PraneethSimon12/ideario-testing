package ideario;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

public class PageFunctionalityTest extends BaseTestClass {

    @Parameters({ "username", "password" })
    @BeforeClass(alwaysRun = true)
    public void initialize(String username, String password) {
        logger.info("Opening Get Started menu.");

        helper.clickElement(By.xpath("//button[text()='Get started']"));
        logger.info("Get Started menu opened successfully.");

        helper.enterText("input[name='identifier']", username);
        helper.clickButton(".cl-formButtonPrimary");

        helper.enterTextAfterWait(By.cssSelector("input[name='password']"), password);

        helper.clickButton(".cl-formButtonPrimary");
        helper.visibilityOfElementLocated(By.xpath("/html/body/div[1]/main/div/div[1]/div[1]/a")).click();

        logger.info("Logged in successfully.");
    }

    private Integer getCountOfDocuments(By locator) {
        List<WebElement> documentItems = driver.findElements(locator);

        Integer count = 0;

        for (WebElement documentItem : documentItems) {

            String text = documentItem.getText();
            if (!text.equals("Settings") && !text.equals("New page") && !text.equals("Add a page")
                    && !text.equals("Trash") && !text.contains("Search")) {
                count++;
            }
        }
        return count;
    }

    @Test(groups = "PageFunctionalityTests")
    public void addPage() throws InterruptedException {

        Thread.sleep(5000);

        Integer count = getCountOfDocuments(By.xpath("//*[@aria-label='Document-item-listicle']"));

        logger.info("Adding a note using Add a page button.");

        helper.isElementDisplayed(By.xpath("//*[@aria-placeholder='Add a page']"));
        helper.clickElement(By.xpath("//*[@aria-placeholder='Add a page']"));

        logger.info("Verifying if note is added.");

        String title = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Untitled')]")))
                .getText();

        softAssert.assertEquals(title, "Untitled", "Note is not added.");

        Integer newCount = getCountOfDocuments(By.xpath("//*[@aria-label='Document-item-listicle']"));

        softAssert.assertEquals(newCount, count + 1, "Note is not added.");
    }

    @Test(groups = "PageFunctionalityTests", dependsOnMethods = "addPage")
    public void deleteTopMostPage() throws InterruptedException {
        logger.info("Deleting top most page.");

        Thread.sleep(2000);


        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@aria-label='Document-item-listicle']")));

        var count = getCountOfDocuments(By.xpath("//*[@aria-label='Document-item-listicle']"));

        logger.info("Count of documents before deleting: " + count);

        WebElement element = driver.findElement(By.cssSelector("#radix-\\:rc\\:"));
        element.click();

        List<WebElement> menuItems = driver.findElements(
                By.cssSelector("div[role='menuitem'][class*='relative flex cursor-default select-none items-center']"));

        for (WebElement item : menuItems) {
            if (item.getText().contains("Delete")) {
                item.click();
                break;
            }
        }

        Thread.sleep(1500);
        softAssert.assertTrue(driver.getPageSource().contains("Note moved to trash!"), "Note is not deleted successfully.");
        

        logger.info("Verifying if top most page is deleted.");


        Thread.sleep(2000);
        var newCount = getCountOfDocuments(By.xpath("//*[@aria-label='Document-item-listicle']"));

        softAssert.assertEquals(newCount, count - 1, "Top most page is not deleted.");

        softAssert.assertAll();
    }

    @Parameters("searchText")
    @Test(groups = "PageFunctionalityTests", dependsOnMethods = "deleteTopMostPage")
    void checkSearch(String searchText) {

        logger.info("Checking search functionality.");

        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL).sendKeys("k").keyUp(Keys.CONTROL).perform();

        driver.findElement(By.ByTagName.tagName("input")).sendKeys(searchText);


        var option = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-selected='true']")));
        option.click();

        var searchTextHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), '" + searchText + "')]")));
        
        softAssert.assertTrue(searchTextHeader.isDisplayed(), "Search is not working.");
    }
}
