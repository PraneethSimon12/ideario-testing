package ideario;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;

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

    @Test
    public void addNoteUsingCreateANote() throws InterruptedException {

        Thread.sleep(5000);

        Integer count = getCountOfDocuments(By.xpath("//*[@aria-label='Document-item-listicle']"));

        logger.info("Adding a note using Create a note button.");

        helper.isElementDisplayed(By.xpath("//button[text()='Create a note']"));
        helper.clickElement(By.xpath("//button[text()='Create a note']"));

        logger.info("Verifying if note is added.");

        String title = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Untitled')]")))
                .getText();

        logger.info("Title of the note is: " + title);

        softAssert.assertEquals(title, "Untitled", "Note is not added.");

        Integer newCount = getCountOfDocuments(By.xpath("//*[@aria-label='Document-item-listicle']"));

        softAssert.assertEquals(newCount, count + 1, "Note is not added.");
    }

    @Test
    public void addNoteUsingAddAPageButton() throws InterruptedException {

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
}
