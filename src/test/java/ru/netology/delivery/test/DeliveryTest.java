package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

class  DeliveryTest {

    private final DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
    private final int daysToAddForFirstMeeting = 4;
    private final String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

    @BeforeAll
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void teardownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() { open("http://localhost:9999");
    }

    @AfterEach
    void clear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    @DisplayName("1. Should plan and replan meeting - use validUser and generateUser()")
    void shouldPlanAndReplanMeetingOneWay() {
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".notification__content").shouldHave(Condition.text("успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(".button__text").click();
        $("[data-test-id=replan-notification] .notification__content").shouldHave(Condition.text("Перепланировать"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $(byText("Перепланировать")).click();
        $(".notification__content").shouldHave(Condition.text("успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
    @Test
    @DisplayName("2. Should plan and replan meeting - use generateCity(), generateName(), generatePhone()")
    void shouldPlanAndReplanMeetingAnotherWay( ) {
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(DataGenerator.generateCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone("ru"));
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(".notification__content").shouldHave(Condition.text("успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(".button__text").click();
        $("[data-test-id=replan-notification] .notification__content").shouldHave(Condition.text("Перепланировать"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $(byText("Перепланировать")).click();
        $(".notification__content").shouldHave(Condition.text("успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("3. Should display error string if phone not inputted")
    void shouldNotPlanIfInvalidPhone() {
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $("[data-test-id=phone].input_invalid").shouldBe(visible).shouldHave(text("телефон"));
    }

    @Test
    @DisplayName("4. Should display error string if invalid city inputted")
    void shouldNotPlanIfInvalidCity() {
        $("[data-test-id=city] input").setValue("Мирный");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $("[data-test-id=city] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    @DisplayName("5. Should display error string if invalid name inputted")
    void shouldNotPlanIfInvalidName() {
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue("Ivan Ivanov");
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $("[data-test-id=name].input_invalid").shouldBe(visible).shouldHave(text("русск"));
    }

    @Test
    @DisplayName("6. Should display error string if invalid date inputted")
    void shouldNotPlanIfInvalidDate() {
        int daysToAddForFirstMeeting = 0;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $("[data-test-id=date] .input_invalid").shouldBe(visible).shouldHave(text("дат"));
    }
}