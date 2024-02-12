package com.scooter;

import com.scooter.pageobject.BasePage;
import com.scooter.pageobject.MainPage;
import com.scooter.pageobject.OrderPage;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderPositiveTest{

    private final String name;
    private final String surname;
    private final String address;
    private final int stationIndex;
    private final String phone;
    private final String date;
    private final int duration;
    private final String color;
    private final String comment;
    private final int indexButton;
    private WebDriver driver;

    public OrderPositiveTest(String name, String surname,
                             String address, int stationIndex, String phone,
                             String date, int duration, String color,String comment, int indexButton) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.stationIndex = stationIndex;
        this.phone = phone;
        this.date = date;
        this.duration = duration;
        this.color = color;
        this.comment = comment;
        this.indexButton = indexButton;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {"Леонид", "Вацок", "ул. Пушкина, д. Колотушкина", 25, "+78005553535", "10.02.24", 0,"серый", "Крутой мужик", 1},
                {"Олег", "Газманов", "ул. Крутая, д. Большой", 35, "+77777777777", "15.05.24", 2,"черный", "Без комментариев", 0},
                {"Григорий", "Измайлов", "Барвиха", 13, "+79551234567", "11.09.24", 2,"черный", "У подъезда", 1},

        };
    }

    @Before
    public void setUp() {
        String browser = System.getenv("BROWSER");
        driver = getDriver(browser == null ? "chrome" : browser);
        driver.get(BasePage.URL);

        setCookie(new Cookie("Cartoshka","true"));
        setCookie(new Cookie("Cartoshka-legacy","true"));
    }

    private void setCookie(Cookie cookie) {
        driver.manage().addCookie(cookie);
        driver.navigate().refresh();
    }

    //Поддержка разных браузеров
    private WebDriver getDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                return new ChromeDriver();

            case "firefox":
                return new FirefoxDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser");
        }
    }
    //Проверка позитивного сценария по кнопкам "Заказать"
    @Test
    public void orderScooterPositiveCase() {
        MainPage mainPage = new MainPage(driver);

            mainPage.clickOrderButton(indexButton);
            OrderPage orderPage = new OrderPage(driver);
            orderPage.fillOrderFormFirstPage(name, surname, address, stationIndex, phone);
            orderPage.fillOrderFormSecondPage(date, duration, color, comment);
            orderPage.clickConfirmOrderButton();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
            MatcherAssert.assertThat(orderPage.getTextAboutOrderResult(), containsString("Заказ оформлен"));
    }

    @After
    public void teardown() {
        driver.quit();
    }
}
