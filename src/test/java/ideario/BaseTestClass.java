package ideario;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class BaseTestClass {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected SeleniumHelper helper;
    private Duration timeout = Duration.ofSeconds(10);
    protected SoftAssert softAssert;
    protected org.slf4j.Logger logger;
    protected Actions actions;

    @Parameters({"browser", "url"})
    @BeforeClass(alwaysRun = true)
    public void setUp(String browser, String url) {

        logger = LoggerFactory.getLogger(this.getClass());
        
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                logger.error("Invalid browser name.");
                throw new IllegalArgumentException("Invalid browser name.");
        }
        driver.manage().window().maximize();
        driver.get(url);
        wait = new WebDriverWait(driver, timeout);
        helper = new SeleniumHelper(driver, wait);
        softAssert = new SoftAssert();
        actions = new Actions(driver);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
