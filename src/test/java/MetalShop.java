import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MetalShop {

    static WebDriver driver;
    String password = "Kluska";
    String username = "KasiaF";

    @BeforeAll
    public static void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterAll
    public static void closeBrowser() {
        driver.quit();
    }

    @BeforeEach
    public void goToHomePage() {
        driver.findElement(By.linkText("Sklep")).click();
    }

    @Test
    public void emptyLogin() {
        driver.findElement(By.linkText("Moje konto")).click();
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.name("login")).click();
        String error = driver.findElement(By.cssSelector(".woocommerce-error")).getText();
        Assertions.assertEquals("Błąd: Nazwa użytkownika jest wymagana.", error);

    }

    @Test
    public void emptyPassword() {
        driver.findElement(By.linkText("Moje konto")).click();
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.name("login")).click();
        String error = driver.findElement(By.cssSelector(".woocommerce-error")).getText();
        Assertions.assertEquals("Błąd: pole hasła jest puste.", error);
    }

    @Test
    public void registerSuccess(){
        driver.findElement(By.linkText("register")).click();
        Faker faker = new Faker();
        String registerUsername = faker.name().username();
        String email = registerUsername + faker.random().nextInt(10000) + "@gmail.com";
        driver.findElement(By.cssSelector("#user_login")).sendKeys(registerUsername);
        driver.findElement(By.cssSelector("#user_email")).sendKeys(email);
        driver.findElement(By.cssSelector("#user_pass")).sendKeys(password);
        driver.findElement(By.cssSelector("#user_confirm_password")).sendKeys(password);
        driver.findElement(By.cssSelector(".ur-submit-button")).click();
        Wait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".user-registration-message")));
        WebElement error = driver.findElement(By.cssSelector(".user-registration-message"));
        Assertions.assertEquals("User successfully registered.", error.getText());
    }

    @Test
    public void searchElements(){
        WebElement logo = driver.findElement(By.linkText("Softie Metal Shop"));
        Assertions.assertEquals("Softie Metal Shop", logo.getText());
        WebElement search = driver.findElement(By.id("woocommerce-product-search-field-0"));
        Assertions.assertTrue(search.isEnabled());

    }
    @Test
    public void goToContakt(){
        driver.findElement(By.linkText("Kontakt")).click();
        Wait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".entry-header")));
        Assertions.assertTrue(driver.getPageSource().contains("Kontakt"));
    }

    @Test
    public void fromSubPageToHomePage(){
        driver.findElement(By.linkText("Moje konto")).click();
        driver.findElement(By.linkText("Softie Metal Shop")).click();
        Assertions.assertEquals("Sklep", driver.findElement(By.cssSelector(".woocommerce-products-header > h1")).getText());
    }

    @Test
    public void sendMessage(){
        driver.findElement(By.linkText("Kontakt")).click();
        Faker faker = new Faker();
        String name = faker.name().firstName() + " " + faker.name().lastName();
        String email = faker.letterify("?????") + faker.random().nextInt(10000) + "@gmail.com";
        String subject = faker.letterify("?????");
        String message = faker.letterify("????? ?? ??? ??");
        driver.findElement(By.name("your-name")).sendKeys(name);
        driver.findElement(By.name("your-email")).sendKeys(email);
        driver.findElement(By.name("your-subject")).sendKeys(email);
        driver.findElement(By.name("your-message")).sendKeys(message);
        driver.findElement(By.cssSelector(".wpcf7-submit"));
        WebElement wiadomośćWysłana = driver.findElement(By.cssSelector(".wpcf7-response-output"));
        Assertions.assertTrue(wiadomośćWysłana.isEnabled());

    }

    @Test
    public void addingToCart() {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.cssSelector(".cart-contents"))).build().perform();
        driver.findElement(By.cssSelector(".woocommerce-mini-cart__empty-message")).getText();
        driver.findElement(By.cssSelector("a[data-product_id = '24']")).click();
        driver.findElement(By.linkText("Zobacz koszyk")).click();
        String fullCard = driver.findElement(By.linkText("Srebrna moneta 5g - UK 1980")).getText();
        Assertions.assertEquals(fullCard, "Srebrna moneta 5g - UK 1980");
    }
    @Test
    public void removeProduct(){
        driver.findElement(By.cssSelector("a[data-product_id = '29']")).click();
        driver.findElement(By.linkText("Zobacz koszyk")).click();
        driver.findElement(By.cssSelector(".remove")).click();
        String emptyCard = driver.findElement(By.cssSelector(".woocommerce-message")).getText();
        Assertions.assertEquals(emptyCard,"Usunięto: „Srebrna moneta 8g - USA 2002”. Cofnij?" );
    }

}
