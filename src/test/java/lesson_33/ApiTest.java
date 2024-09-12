
package lesson_33;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.specification.RequestSpecification;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import io.qameta.allure.restassured.AllureRestAssured;




public class ApiTest {
    private String token;
    private int bookingId;
    private List<Integer> listIds;

    @BeforeMethod
    public void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com/";
    }

    @Test(priority = 1)
    public void getUserData() {
        JSONObject jsonObj = new JSONObject()
                .put("username", "admin")
                .put("password", "password123");

        RequestSpecification requestSpecification = RestAssured.given().contentType("application/json").body(jsonObj.toString());
        Response res = requestSpecification.post("https://restful-booker.herokuapp.com/auth");
        System.out.println(res.asString());
        this.token = res.jsonPath().getString("token");
    }

    @Test(priority = 2)
    public void createBooking() {
        //        LocalDate checkin = LocalDate.parse("2024-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //        LocalDate checkout = LocalDate.parse("2025-01-09", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date checkin = formatter.parse("2024-01-01");
            Date checkout = formatter.parse("2024-01-10");

            BookModelRequest book = BookModelRequest.builder()
                    .firstname("Ksenya IDE")
                    .lastname("frasyniuk")
                    .totalprice(132)
                    .depositpaid(true)
                    .bookingdates(
                            BookingDate
                                    .builder()
                                    .checkin(checkin)
                                    .checkout(checkout)
                                    .build()
                    )
                    .build();
            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .body(book)
                    .post("/booking");
            response.then().statusCode(200);
            this.bookingId = response.as(BookingResponse.class).getBookingid();
            System.out.println(bookingId);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 3)
    public void getAllBooking() {
        System.out.println("test 3");
        Response response = RestAssured.get("booking/");
        response.then().statusCode(200);
        this.listIds = response.jsonPath().getList("bookingid");
        System.out.println("ID list =>" + listIds);
    }

    @Test(priority = 4)
    public void editTotalPrice() {
        RequestSpecification spec = new RequestSpecBuilder()
                .addCookie("token", this.token)
                .build();
        BookModelRequest book = BookModelRequest.builder()
                .totalprice(3332)
                .build();
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .spec(spec)
                .body(book)
                .patch("/booking/{id}", this.bookingId);
        response.then().statusCode(200);
        System.out.println(response.as(BookModelRequest.class).getFirstname());
        System.out.println(response.as(BookModelRequest.class).getTotalprice());
    }

    @Test(priority = 5)
    public void editLastName() {
        Random rand = new Random();
        int randomElement = this.listIds.get(rand.nextInt(this.listIds.size()));
        RequestSpecification spec = new RequestSpecBuilder()
                .addCookie("token", this.token)
                .build();
        BookModelRequest book = BookModelRequest.builder()
                .lastname("Mokanu")
                .build();
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .spec(spec)
                .body(book)
                .patch("/booking/{id}", randomElement);
        response.then().statusCode(200);
        System.out.println(response.as(BookModelRequest.class).getLastname());
    }

    @Test(priority = 6)
    public void deleteBooking() {
        Random rand = new Random();
        int randomElement = this.listIds.get(rand.nextInt(this.listIds.size()));
        RequestSpecification spec = new RequestSpecBuilder()
                .addCookie("token", this.token)
                .build();
        Response responseDelete = RestAssured
                .given()
                .contentType("application/json")
                .spec(spec)
                .delete("/booking/{id}", randomElement);
        responseDelete.then().statusCode(201);
        Response responseGet = RestAssured
                .given()
                .contentType("application/json")
                .get("/booking/{id}", randomElement);
        responseGet.then().statusCode(404);
    }
}


