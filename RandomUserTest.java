package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RandomUserTest {

    @Test
    public void validarRequisicao20Usuarios() {
        given().
                contentType(ContentType.JSON)
                .param("results", 20)
                .when()
                .get("https://randomuser.me/api/")
                .then()
                .log().all()
                .assertThat()
                .body("info.results", is(20));
    }
    @Test
    public void validarNacionalidadeBr() {
        given().
                contentType(ContentType.JSON)
                .param("nat", "BR")
                .when()
                .get("https://randomuser.me/api/")
                .then()
                .log().all()
                .assertThat()
                .body("results[0].nat", is("BR"));
    }

    @Test
    public void validarNumeroPaginacao() {
                given().
                contentType(ContentType.JSON)
                .param("page", 3)
                .when()
                .get("https://randomuser.me/api/")
                .then()
                .log().all()
                .assertThat()
                .body("info.page", is(3));
    }

    @Test
    public void validarNacionalidadeBrUsCaEs() {
        String response =
                         given()
                        .param("nat", "BR,US,CA,ES")
                        .when()
                        .get("https://randomuser.me/api/")
                        .getBody().path("results[0].nat");
        Assert.assertTrue(response.equals("BR") || response.equals("US") || response.equals("CA") || response.equals("ES"));
    }

    @Test
    public void validarNacionalidadeBrUmReultado() {
                 given().
                 contentType(ContentType.JSON)
                .param("nat", "BR")
                .param("results", 1)
                .when()
                .get("https://randomuser.me/api/")
                .then()
                .log().all()
                .assertThat()
                .body("results[0].nat", is("BR"))
                .body("info.results", is(1));
    }

    @Test
    public void validarNomeEEmailusuario() {

        ArrayList response = RestAssured.
                      given()
                         .contentType(ContentType.JSON)
                        .param("name.first", "Alicia")
                        .param( "email", "alicia.lemoine@example.com")
                        .when()
                        .get("https://randomuser.me/api/")
                        .getBody().path("results");
        Assert.assertTrue(response.equals("BR") || response.equals("US") || response.equals("CA") || response.equals("ES"));
//
//
//anyOf()
//        for(Object item : response) {
//            if(item.toString().contains("Lotta"))
//            {
//              assert true;
//            }
//              //  System.out.println("\n");
//            System.out.println( (item));
//        }
    }
}
