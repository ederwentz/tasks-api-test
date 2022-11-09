package br.ce.ederwentz.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		// servidor windows
		//RestAssured.baseURI = "http://localhost:8001/tasks-backend";
		// servidor linux	
		RestAssured.baseURI = "http://192.168.1.112:8001/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured.given()
			//.log().all()
		.when()
			.get("/todo")
			//.get("http://localhost:8001/tasks-backend/todo")
		.then()
		    //.log().all()
			.statusCode(200)
		;
	}
	
	@Test
	public void deveAdicionarTarefasComSucesso() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\", \"dueDate\": \"2022-12-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			//.log().all()
			.statusCode(201)
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefasInvalida() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\", \"dueDate\": \"2020-12-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			//.log().all()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}
	
	@Test
	public void deveRemoverTarefasComSucesso() {
		// inserir
		Integer id = RestAssured.given()
			.body("{ \"task\": \"Tarefa teste via API\", \"dueDate\": \"2022-12-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			//.log().all()
			.statusCode(201)
			.extract().path("id")
			//.body("message", CoreMatchers.is("Due date must not be in past"))
		;
		
		System.out.println(id);
		
		// remover
		RestAssured.given()
		.when()
			.delete("/todo/"+id)
		.then()
			.statusCode(204)
		;
	}
}
