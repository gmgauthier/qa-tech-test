package gradle.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class Stepdefs {
    private ChromeDriver driver;

    @Before
    public void setUp() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--ignore-certificate-errors"); // only for limited test envs
            options.addArguments("--start-maximized"); // for viewport issues
            driver = new ChromeDriver(options);
        }
    }

    @After
    public void tearDown() {
        if (driver!=null) {
            driver.close();
            driver.quit();
        }
    }

    @Given("The user is at the home page")
    public void the_user_is_at_the_home_page() {
        driver.get("http://localhost:3000");
        if (!driver.getTitle().equals("React App")){
            String failString = String.format("Browser is not in desired state. Title=%s", driver.getTitle());
            fail(failString);
        }
    }

    @Then("The render challenge button should be visible")
    public void the_button_should_be_visible() {
        driver.findElement(By.xpath("//*[@data-test-id='render-challenge']")).isDisplayed();
    }

    @Given("The user has navigated to the entry fields")
    public void the_user_has_navigated_to_the_entry_fields() {
        driver.findElement(By.xpath("//*[@data-test-id='render-challenge']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("tr"))).click();
    }

    @When("The user has submitted the correct entry field values")
    public void the_user_has_submitted_the_correct_entry_field_values() {
        fillInAnswers();
        driver.findElement(By.xpath("//*[@data-test-id='submit-4']")).sendKeys("Greg Gauthier");
        WebElement button = driver.findElement(By.xpath("//*[text()='Submit Answers']"));
        JavascriptExecutor jse2 = driver;
        jse2.executeScript("arguments[0].scrollIntoView()", button); //too far below the viewport to click
        button.click();
    }

    @Then("The success message is displayed")
    public void the_success_message_is_displayed() {
        String successText = "Congratulations you have succeeded. Please submit your challenge ✅";
        String resultsDialog = "body > div:nth-child(5) > div > div:nth-child(1) > div > div > div:nth-child(1)";
        String resultsText = driver.findElementByCssSelector(resultsDialog).getText();
        Assertions.assertEquals(successText, resultsText);
    }

    //*** HELPERS

    public void fillInAnswers() {
        List<WebElement> tableRows = driver.findElements(By.tagName("tr"));
        for (int i = 0; i < tableRows.size(); i++) { //old style loop gives us an enumerator for the sendkeys
            int[] row = stringToIntArray(tableRows.get(i).getText());
            String elementByDataId = String.format("//*[@data-test-id='submit-%d']", i + 1);
            driver.findElement(By.xpath(elementByDataId)).sendKeys(String.format("%d", findPartition(row)));
        }
    }

    /**
     * stringToIntArray
     * @param numbers a  string list of numbers separated by spaces
     * @return int[] a proper integer array
     *
     */
    public int[] stringToIntArray(final String numbers){
        String[] parts = numbers.split(" ");
        int[] n1 = new int[parts.length];
        for(int n = 0; n < parts.length; n++) {
            n1[n] = Integer.parseInt(parts[n]);
        }
        return n1;
    }

    /**
     * findPartition
     * @param arr integer array of any length
     * @return int
     */
    public int findPartition(final int[] arr){
        int arrLen = arr.length;
        int[] leftSum = new int[arrLen];

        //Left side
        leftSum[0] = arr[0];
        for (int i = 1; i < arrLen; i++)
            leftSum[i] = leftSum[i - 1] + arr[i];
        //Right side
        int[] rightSum = new int[arrLen];
        rightSum[arrLen - 1] = arr[arrLen - 1];
        for (int i = arrLen - 2; i >= 0; i--)
            rightSum[i] = rightSum[i + 1] + arr[i];
        //Compare
        for (int i = 1; i < arrLen - 1; i++)
            if (leftSum[i] == rightSum[i]) {
                return arr[i]; //Bingo!
            }
        return -1;
    }
}