package me.aboullaite.moneytransfer;

import io.restassured.http.ContentType;
import me.aboullaite.moneytransfer.models.holders.SampleHolder;
import me.aboullaite.moneytransfer.utils.TransactionPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
public class MoneyTransferTest {

    @BeforeAll
    static void setUp() {
        MoneyTransfer.startWithData();
        port= 9999;
    }

    /***
     *     Testing Holder Routes
      */
    @Test
    public void testHoldersWithoutPaginationParams(){
        get("/holders").then()
                .statusCode(400)
                .and()
                .body("errorCode", is(400))
                .body("errorMessage", is("Invalid pagination parameters"));
    }

    @Test
    public void testHoldersWithInValidPaginationParams(){
        get("/holders?page=0&limit=10").then()
                .statusCode(400)
                .and()
                .body("errorCode", is(400))
                .body("errorMessage", is("Page number should be positive and starts with 1"));
    }

    @Test
    public void testValidHolderAndAccounts(){

        List<SampleHolder> holders = get("/holders?page=2&limit=10").then()
                .statusCode(200)
                .and()
                .body("pageNumber", is(2))
                .body("recordsPerPage", is(10))
                .body("content", hasSize(10))
        .extract().jsonPath().getList("content", SampleHolder.class);

        SampleHolder someHolder = holders.get(0);

        get("/holders/"+ someHolder.getId()).then()
                .statusCode(200)
                .and()
                .body("id", is(someHolder.getId()))
                .body("name", is(someHolder.getName()))
                .body("email", is(someHolder.getEmail()));

        String accountId = get("/holders/{id}/accounts", someHolder.getId()).then()
                .statusCode(200)
                .and()
                .body("size()", is(2))
                .extract().jsonPath().getString("get(0).id");


        get("/accounts/{id}", accountId).then()
                .statusCode(200)
                .and()
                .body("holder.id", is(someHolder.getId()))
                .body("holder.name", is(someHolder.getName()))
                .body("holder.email", is(someHolder.getEmail()));
    }

    @Test
    public void testHolderWithWrongId() {
        get("/holders/123456789").then()
                .statusCode(404)
                .and()
                .body("errorCode", is(404))
                .body("errorMessage", is("Holder with id 123456789 is not found"));
    }

    /**
     *     Testing Account Routes
      */
    @Test
    public void testAccountsWithoutPaginationParams(){
        get("/accounts").then()
                .statusCode(400)
                .and()
                .body("errorCode", is(400))
                .body("errorMessage", is("Invalid pagination parameters"));
    }


    @Test
    public void testAccountsWithInValidPaginationParams(){
        get("/accounts?page=0&limit=10").then()
                .statusCode(400)
                .and()
                .body("errorCode", is(400))
                .body("errorMessage", is("Page number should be positive and starts with 1"));
    }

    @Test
    public void testValidAccountsAndTransactions(){

        String accountId = get("/accounts?page=2&limit=10").then()
                .statusCode(200)
                .and()
                .body("pageNumber", is(2))
                .body("recordsPerPage", is(10))
                .body("content", hasSize(10))
                .extract().jsonPath().getString("content.get(0).id");


        get("/accounts/{id}", accountId).then()
                .statusCode(200)
                .and()
                .body("id", is(accountId));

        get("/accounts/{id}/transactions?limit=10", accountId).then()
                .statusCode(200)
                .and()
                .body("size()", is(4));

        get("/accounts/{id}/transactions", accountId).then()
                .statusCode(400)
                .and()
                .body("errorCode", is(400))
                .body("errorMessage", is("Invalid pagination parameters"));

        get("/accounts/{id}/transactions?limit=10", accountId).then()
                .statusCode(200)
                .and()
                .body("content", hasSize(10));

    }

    @Test
    public void testAccountsWithWrongId() {
        get("/accounts/123456789").then()
                .statusCode(404)
                .and()
                .body("errorCode", is(404))
                .body("errorMessage", is("Account with id 123456789 is not found"));
    }

    /**
     *     Testing Transaction Routes
      */
    @Test
    public void testTransactionWithoutPaginationParams(){
        get("/transactions").then()
                .statusCode(400)
                .and()
                .body("errorCode", is(400))
                .body("errorMessage", is("Invalid pagination parameters"));
    }


    @Test
    public void testTransactionWithInValidPaginationParams(){
        get("/transactions?page=0&limit=10").then()
                .statusCode(400)
                .and()
                .body("errorCode", is(400))
                .body("errorMessage", is("Page number should be positive and starts with 1"));
    }

    @Test
    public void testValidTransaction(){

        String txId = get("/transactions?page=2&limit=10").then()
                .statusCode(200)
                .and()
                .body("pageNumber", is(2))
                .body("recordsPerPage", is(10))
                .body("content", hasSize(10))
                .extract().jsonPath().getList("content.id", String.class).get(0);

        get("/transactions/{id}", txId).then()
                .statusCode(200)
                .and()
                .body("id", is(txId));

        List<String> accounts = get("/accounts?page=2&limit=10").then().extract().jsonPath().getList("content.id", String.class);
        // Good data
        TransactionPayload txp = new TransactionPayload(accounts.get(0), accounts.get(1), new BigDecimal(100));
        given().body(txp)
                .contentType(ContentType.JSON)
                .post("/transactions")
                .then().statusCode(201);

        // Same Account
        TransactionPayload txpSameAcc = new TransactionPayload(accounts.get(0), accounts.get(0), new BigDecimal(100));
        given().body(txpSameAcc)
                .contentType(ContentType.JSON)
                .post("/transactions")
                .then().statusCode(400)
                .and()
                .body("errorCode", is(400))
                .body("errorMessage", is("Accounts must be different"));
        // Negative Amount
        TransactionPayload txpMenAm = new TransactionPayload(accounts.get(0), accounts.get(0), new BigDecimal(100).negate());
        given().body(txpMenAm)
                .contentType(ContentType.JSON)
                .post("/transactions")
                .then().statusCode(400)
                .and()
                .body("errorCode", is(400))
                .body("errorMessage", is("Amount must be greater than zero"));
    }

    @Test
    public void testTransferTransaction(){

        given().param("debitAccountId");
        String txId = get("/transactions?page=2&limit=10").then()
                .statusCode(200)
                .and()
                .body("pageNumber", is(2))
                .body("recordsPerPage", is(10))
                .body("content", hasSize(10))
                .extract().jsonPath().getString("content.get(0).id");


        get("/transactions/{id}", txId).then()
                .statusCode(200)
                .and()
                .body("id", is(txId));

    }

    @Test
    public void testTransactionWithWrongId() {
        get("/transactions/123456789").then()
                .statusCode(404)
                .and()
                .body("errorCode", is(404))
                .body("errorMessage", is("Transaction with id 123456789 is not found"));
    }


}
