package ideario;

import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumHelper {

    private WebDriver driver;
    private WebDriverWait wait;

    public SeleniumHelper(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    public boolean isElementDisplayed(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.isDisplayed();
    }

    public boolean isElementInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public String getElementText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText();
    }

    public void enterText(String cssSelector, String text) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        field.clear();
        field.sendKeys(text);
    }

    public void enterUserFields(String firstName, String lastName, String username, String password) {
        enterText("input[name='firstName']", firstName);
        enterText("input[name='lastName']", lastName);
        enterText("input[name='username']", username);
        enterText("input[name='password']", password);
    }

    public void clickButton(String cssSelector) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssSelector)));
        button.click();
    }

    public String generateDummyPassword(Integer length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        
        return password.toString();
    }
}
