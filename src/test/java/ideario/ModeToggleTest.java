package ideario;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;

public class ModeToggleTest extends BaseTestClass {

    @BeforeClass(alwaysRun = true)
    public void openToggleMenu(){

        logger.info("Opening toggle menu.");
        helper.clickButton("#radix-\\:R4jbekq\\:");
    }
    
    @Test
    public void checkIfToggleMenuIsDisplayed(){

        logger.info("Checking if toggle menu is displayed.");

        helper.isElementDisplayed(By.xpath("//*[@id=\"radix-:R4jbekqH1:\"]"));

        Assert.assertTrue(helper.isElementDisplayed(By.xpath("//*[@id=\"radix-:R4jbekqH1:\"]")), "Toggle menu is not displayed");
    }

    @Test
    public void checkLightDarkModeButton(){

        logger.info("Checking if light mode is working.");
        helper.clickButton("div.cursor-default:nth-child(1)");
        Assert.assertTrue(driver.findElement(By.xpath("/html")).getAttribute("class").contains("light"), "Light mode is not enabled");

        openToggleMenu();

        logger.info("Checking if dark mode is working.");
        helper.clickButton("div.cursor-default:nth-child(2)");
        Assert.assertTrue(driver.findElement(By.xpath("/html")).getAttribute("class").contains("dark"), "Dark mode is not enabled");
    }
}
