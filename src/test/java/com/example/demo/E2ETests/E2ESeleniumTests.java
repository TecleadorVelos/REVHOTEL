package com.example.demo.E2ETests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ESeleniumTests {
	
	@LocalServerPort
	int port;
	
	private WebDriver driver;
	
	@BeforeEach
	public void setUp() {
		// Configura el WebDriver con WebDriverManager (descarga automáticamente el driver)
        WebDriverManager.edgedriver().setup();;
        driver = new EdgeDriver();
	}
	@AfterEach
	public void closeTest(){
		if(driver != null) driver.quit();
	}
	
	//TESTS GENERALES
	@Test
	@DisplayName("TEST 1: Botón ir App funciona")
	public void botonPagPrincipalTest() {
		driver.get("http://localhost:" + port);
		
		driver.findElement(By.id("accesoUsuarios")).click();
		
		new WebDriverWait (driver, Duration.ofSeconds(2)); //Espera de 2 segundos mientras carga la página
		
		WebElement heading = driver.findElement(By.tagName("h1"));
		assertTrue(heading.getText().contains("Login"), "El encabezado no es el esperado"); //Es correcto puesto que ha llegado a la página del login
    }
	
	
	//TESTS DE ADMINISTRADOR
	@Test
	@DisplayName("TEST 2: Iniciamos Sesión como Admin")
	public void iniciarSesionAdminTest() { 
		
		this.iniciarSesionAdmin();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); //Espera de 5 segundos mientras carga la página
		
		WebElement heading = driver.findElement(By.tagName("h1"));
		assertTrue(heading.getText().contains("Bienvenido Administrador"), "El encabezado no es el esperado");
	}
	
	@Test
	@DisplayName("TEST 3: Crear promoción")
	public void establecerPromocionTest() { 
		
		this.iniciarSesionAdmin();
		dormir(3000); //delay de 3 segundos
		 
		driver.findElement(By.id("accesoPromociones")).click();
		dormir(3000);
		
		WebElement tabla1 = driver.findElement(By.id("tablaPromociones"));
		List<WebElement> filasinicial = tabla1.findElements(By.tagName("tr"));
		
		driver.findElement(By.id("botonCrearPromocion")).click();
		dormir(3000);

		
		WebElement cantidad = driver.findElement(By.name("oferta"));
		WebElement fechainicio = driver.findElement(By.name("fechainicio"));
		WebElement fechafinal = driver.findElement(By.name("fechafinal"));
		
		cantidad.sendKeys("99");
		fechainicio.sendKeys("10/10/2024");
		fechafinal.sendKeys("14/10/2024");
		
		driver.findElement(By.id("boton_enviar_promocion")).click();
		dormir(3000);
		
		WebElement tabla2 = driver.findElement(By.id("tablaPromociones"));
		List<WebElement> filasfinal = tabla2.findElements(By.tagName("tr"));
		WebElement ultimafila = filasfinal.get(filasfinal.size()-1);
		int ultimonumeroProm = filasfinal.size()-1;
		WebElement celda = ultimafila.findElement(By.xpath("//tr["+ ultimonumeroProm +"]/td[1]"));
		
		//verificamos que hay una entrada en la tabla más que antes y verificamos que el número sea uno mas. 
		//es decir, si la última entrada era 3, pues la ultima entrada ahora tendrá el numero 4
		Integer numerofilasinicial = filasinicial.size();
		assertTrue(numerofilasinicial + 1 == filasfinal.size());
		
		Integer numeroPromocion = Integer.parseInt(celda.getText());
		
		//Considera la cabecera como una fila
		assertTrue(numeroPromocion.equals(numerofilasinicial), "El número de la nueva promoción es: "+ numeroPromocion+" y El numero de filas inicial era: "+filasinicial.size() );
	}
	
	@Test
	@DisplayName("TEST 4: Borrar promoción")
	public void borrarPromocionTest() {
		
		Integer numfilasinicial, numfilasfinal;
		
		this.iniciarSesionAdmin();
		dormir(3000); //delay de 3 segundos
		 
		driver.findElement(By.id("accesoPromociones")).click();
		dormir(3000);
		
		WebElement tabla1 = driver.findElement(By.id("tablaPromociones"));
		List<WebElement> filasinicial = tabla1.findElements(By.tagName("tr"));
		
		numfilasinicial = filasinicial.size();
		
		//vamos a la última fila y la borramos
		WebElement ultimafila = filasinicial.get(numfilasinicial-1);
		
		ultimafila.findElement(By.id("botonBorrar")).click();
		dormir(3000);
		
		WebElement tabla2 = driver.findElement(By.id("tablaPromociones"));
		List<WebElement> filasfinal = tabla2.findElements(By.tagName("tr"));
		
		numfilasfinal = filasfinal.size();
		
		assertTrue(numfilasinicial.compareTo(numfilasfinal) > 0 , "Error en el borrado de la promoción.");
		
		
	}
	@Test
	@DisplayName("TEST 5: Crear Hotel")
	public void crearHotelTest() {
		this.iniciarSesionAdmin();
		dormir(3000); //delay de 3 segundos
		 
		driver.findElement(By.id("accesoGestionHoteles")).click();
		dormir(3000);
		
		WebElement nombreHotel = driver.findElement(By.id("camponomHotel"));
		nombreHotel.sendKeys("Hotel Prueba");
		
		driver.findElement(By.id("btnenviar")).click();
		dormir(3000);
		//THEN
		
		WebElement tabla = driver.findElement(By.id("tablaHoteles"));
		
		WebElement hotel = tabla.findElement(By.xpath("//tr/td[2][text()='Hotel Prueba']"));
		assertTrue(hotel.getText().equals("Hotel Prueba"), "La cadena obtenida es: "+ hotel.getText() + "y buscamos: Hotel Prueba");
	}
	
	@Test
	@DisplayName("TEST 6: Borrar Hotel éxito")
	public void borrarHotelExitoTest() {
		this.iniciarSesionAdmin();
		dormir(3000); //delay de 3 segundos
		 
		driver.findElement(By.id("accesoGestionHoteles")).click();
		dormir(3000);
		
		WebElement identificadorHotel = driver.findElement(By.name("id"));
		
		//vamos a buscar el id del último hotel en la tabla
		WebElement tabla = driver.findElement(By.id("tablaHoteles"));
		List<WebElement> filas = tabla.findElements(By.tagName("tr"));
		WebElement ultimafila = filas.get(filas.size() - 1);
		WebElement identificador = ultimafila.findElement(By.xpath(".//td[1]"));
		String textoid = identificador.getText();
		identificadorHotel.sendKeys(textoid);
		driver.findElement(By.id("btnenviardelete")).click();
		dormir(3000);
		
		//vamos a comparar el numero de filas
		
		WebElement tabla2 = driver.findElement(By.id("tablaHoteles"));
		List<WebElement> filastrasborrado = tabla2.findElements(By.tagName("tr"));
		
		Integer numfilasinicial = filas.size();
		Integer numfilasfinal = filastrasborrado.size();
		
		assertTrue(numfilasinicial.compareTo(numfilasfinal) > 0, "El resultado obtenido es: "+ numfilasinicial+" y: "+ numfilasfinal);
		
	}
	
	@Test
	@DisplayName("TEST 7: Borrar Hotel fail")
	public void borrarHotelFailTest() {
		
		this.iniciarSesionAdmin();
		dormir(3000); //delay de 3 segundos
		 
		driver.findElement(By.id("accesoGestionHoteles")).click();
		dormir(3000);
		
		WebElement identificadorHotel = driver.findElement(By.name("id"));
		identificadorHotel.sendKeys("2338474");
		driver.findElement(By.id("btnenviardelete")).click();
		dormir(3000);
		
		WebElement encabezado = driver.findElement(By.tagName("p"));
		
		assertTrue(encabezado.getText().equals("Se ha producido un error, hotel no encontrado."), "Error en el test.");
	}
	@Test
	@DisplayName("TEST 8: Crear Habitación")
	public void crearHabitacionTest() {
		
		this.iniciarSesionAdmin();
		dormir(3000); //delay de 3 segundos
		 
		driver.findElement(By.id("accesoGestionHoteles")).click();
		dormir(3000);
		
		driver.findElement(By.id("tablaHoteles")).findElement(By.xpath(".//td[1]")).click();
		dormir(3000);
		
		//cuantos elementos tiene la tabla
		WebElement tabla = driver.findElement(By.id("tablaHabitaciones"));
		List<WebElement> filas = tabla.findElements(By.tagName("tr"));
		Integer numfilasinicial = filas.size();
		
		driver.findElement(By.id("btnhabitacion")).click();
		dormir(3000);
		
		WebElement nombrehab = driver.findElement(By.id("nomHabitacion"));
		WebElement selector = driver.findElement(By.name("tipoHabitacion"));
		
		nombrehab.sendKeys("XXX404");
		selector.sendKeys("Normal");
		
		driver.findElement(By.id("boton_enviar_habitacion")).click();
		dormir(3000);
		
		WebElement tabla2 = driver.findElement(By.id("tablaHabitaciones"));
		List<WebElement> filasfinal = tabla2.findElements(By.tagName("tr"));
		Integer numfilasfinal = filasfinal.size();
		
		assertTrue(numfilasinicial.compareTo(numfilasfinal) < 0, "Recibido: " + numfilasinicial+ "y: "+ numfilasfinal);
	}
	
	@Test
	@DisplayName("TEST 9: Borrar Habitación")
	public void borrarHabitacionTest() {
		
		this.iniciarSesionAdmin();
		dormir(3000); //delay de 3 segundos
		 
		driver.findElement(By.id("accesoGestionHoteles")).click();
		dormir(3000);
		
		driver.findElement(By.id("tablaHoteles")).findElement(By.xpath(".//td[1]")).click();
		dormir(3000);
		
		//cuantos elementos tiene la tabla
		WebElement tabla = driver.findElement(By.id("tablaHabitaciones"));
		List<WebElement> filas = tabla.findElements(By.tagName("tr"));
		
		Integer numfilasinicial = filas.size();
		
		
		WebElement ultimafila = filas.get(numfilasinicial-1);
		ultimafila.findElement(By.xpath(".//td[5]")).click();;
		
		dormir(3000);
		
		WebElement tabla2 = driver.findElement(By.id("tablaHabitaciones"));
		List<WebElement> filasfinal = tabla2.findElements(By.tagName("tr"));
		Integer numfilasfinal = filasfinal.size();
		
		assertTrue(numfilasinicial.compareTo(numfilasfinal) > 0, "Recibido: " + numfilasinicial+ "y: "+ numfilasfinal);
	}
	
	
	//FUNCIONES AUXILIARES
	
	public void iniciarSesionAdmin() {
		
		driver.get("http://localhost:" + port);
		
		driver.findElement(By.id("accesoUsuarios")).click();
		
		new WebDriverWait (driver, Duration.ofSeconds(2)); //Espera de 2 segundos mientras carga la página
		
		WebElement nombreUsuario = driver.findElement(By.id("nombreUsuario"));
		WebElement contrasena = driver.findElement(By.id("contrasena"));
		
		nombreUsuario.sendKeys("admin2");
		contrasena.sendKeys("admin2");
		
		driver.findElement(By.id("enviarForm")).click();
	}
	
	public void dormir(int milisegundos) {
		try {
			Thread.sleep(milisegundos);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void iniciarSesionUsuario() {
		
		driver.get("http://localhost:" + port);
		
		driver.findElement(By.id("accesoUsuarios")).click();
		
		new WebDriverWait (driver, Duration.ofSeconds(2)); //Espera de 2 segundos mientras carga la página
		
		WebElement nombreUsuario = driver.findElement(By.id("nombreUsuario"));
		WebElement contrasena = driver.findElement(By.id("contrasena"));
		
		nombreUsuario.sendKeys("jorge");
		contrasena.sendKeys("jorge");
		
		driver.findElement(By.id("enviarForm")).click();
	}
	
	//TESTS DE USUARIO
	
	
}