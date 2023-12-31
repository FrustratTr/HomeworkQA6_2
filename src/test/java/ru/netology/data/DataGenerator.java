package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    // спецификация
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    //метод, который выполняет запрос
    private static void sendRequest(RegistrationDto user) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    //метод, который генерирует логин
    public static String getRandomLogin() {
        String login = faker.name().username();
        return login;
    }

    //метод, который генерирует пароль
    public static String getRandomPassword() {
        String password = faker.internet().password();
        return password;
    }

    //можно отдельно сделать класс Registration (невложенный)
    public static class Registration {
        private Registration() {
        }

        //метод, который генерирует пользователя (генерирует и возвращает пользователя)
        //собирает логин, пароль, статус в пользователя
        public static RegistrationDto getUser(String status) {
            var user = new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }
    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}