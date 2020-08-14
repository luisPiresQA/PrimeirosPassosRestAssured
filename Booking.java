package org.example;

import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class Booking {

    private String url= "https://restful-booker.herokuapp.com/";
    private String TOKEN;

    @Before
    public void login(){
        Map<String, String> params = new HashMap<>();
        params.put("username", "admin");
        params.put("password", "password123");
             TOKEN =  given()
                .body(params)
                .when()
                .post(url+"auth")
                .then()
                .statusCode(200)//status
                .extract().path("token");
    }

    @Test
    public void validarListaBookings()
    {
      given()
              .when()
              .get(url+"booking")
              .then()
              .log().all()
              .statusCode(200)
              .body("bookingid",hasSize(10));
    }

    @Test
    public void validarIncluirBooking() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "JWT" + TOKEN)
                .body("{\"firstname\": \"Jose Booking\"," +
                        "\"lastname\": \"Brown\"," +
                        "\"totalprice\": 111," +
                        "\"depositpaid\": true," +
                        "\"bookingdates\": " +
                        "{\"checkin\": \"2013-02-23\"," +
                        "\"checkout\": \"2014-10-23\"}," +
                        "\"additionalneeds\": \"Breakfast\"}")
                .when()
                .post(url+"booking/")
                .then()
                .log().all()
                .statusCode(200)
                .body("booking.firstname", is("Jose Booking")).log().all();
                 given()
                .get(url+"booking/")
                         .then()
                         .log().body();
    }

    @Test
    public void validarReservaCriada()throws JSONException
    {
                 given()
                .when().log().all()
                .get(url+"booking")
                .then()
                .statusCode(200)
                .assertThat().log().all()
                .body("bookingid[2]", is(21)).log().all();

    }
        @Test
        public  void validarDeleteBooking() {
            String cookie = given()
                    .log().all()
                    .formParam("username","admin")
                    .formParam("password","password123")
                    .contentType(ContentType.URLENC.withCharset("UTF-8"))
                    .when()
                    .post(url+"auth")
                    .then().log().all()
                    .statusCode(200)
                    .extract().path("token");

                     given()
                    .log().all()
                    .cookie("token",cookie)
                    .when()
                    .delete(url+"booking/7")
                    .then().log().all()
                    .statusCode(201);
        }

    @Test
    public  void validarAlterarBookingComSucesso() {
        String cookie = given()
                .log().all()
                .formParam("username","admin")
                .formParam("password","password123")
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .when()
                .post(url+"auth")
                .then().log().all()
                .statusCode(200)
                .extract().path("token");
                 given()
                .log().all()
                .cookie("token",cookie)
                .when()
                 .body("{\"firstname\": \"Luis Booking alterado\",\"lastname\": \"Brown\"}")
                .put(url+"booking/5")
                .then().log().all()
                .statusCode(201);
                 given().get(url+"booking/5")
                 .then().log().all();
    }
}
