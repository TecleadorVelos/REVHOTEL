package com.example.demo.E2ETests;

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
public class E2ESeleniumUserTests {

	
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
	
	//TESTS GENERALES USUARIO
	
	@Test
	@DisplayName("TEST 1: MOSTRAR HISTORIAL DE RESERVAS")
	public void historialReservasTest() {
		this.iniciarSesionUsuario();
		
		driver.findElement(By.id("historialReservas")).click();
		dormir(3000);
		
		WebElement encabezado = driver.findElement(By.tagName("h1"));
		
		assertTrue(encabezado.getText().equals("Historial de Reservas"), "El string recibido: "+ encabezado.getText() +" no coincide con: Historial de Reservas");
			
	}
	@Test
	@DisplayName("TEST 2: CREAR RESERVA ÉXITO")
	public void crearReservaExitoTest() {
		this.iniciarSesionUsuario();
		
		driver.findElement(By.id("gestionReservas")).click();
		dormir(3000);
		
		WebElement tabla = driver.findElement(By.id("tablaReservas"));
		List<WebElement> filas = tabla.findElements(By.tagName("tr"));
		
		Integer numfilasinicial = filas.size();
		
		driver.findElement(By.id("btnRealizarReserva")).click();
		dormir(3000);
		
		WebElement selector = driver.findElement(By.id("selectorHotel"));
		WebElement tipohabitacion = driver.findElement(By.name("tipoHabitacion"));
		WebElement fechaentrada = driver.findElement(By.name("fechaEntrada"));
		WebElement fechasalida = driver.findElement(By.name("fechaSalida"));
		
		selector.sendKeys("Blue Lagoon");
		tipohabitacion.sendKeys("Normal");
		fechaentrada.sendKeys("20/08/2024");
		fechasalida.sendKeys("27/08/2024");
		
		driver.findElement(By.id("boton_enviar_reserva")).click();
		dormir(3000);
		
		WebElement tabla2 = driver.findElement(By.id("tablaReservas"));
		List<WebElement> filasfinal = tabla2.findElements(By.tagName("tr"));
		Integer numfilasfinal = filasfinal.size();
		
		assertTrue(numfilasinicial.compareTo(numfilasfinal) < 0, "Recibido: " + numfilasinicial+ "y: "+ numfilasfinal);
	}
	
	@Test
	@DisplayName("TEST 3: CREAR RESERVA FALLO")
	public void crearReservaFailTest() {
		this.iniciarSesionUsuario();
		
		driver.findElement(By.id("gestionReservas")).click();
		dormir(3000);
		
		driver.findElement(By.id("btnRealizarReserva")).click();
		dormir(3000);
		
		WebElement selector = driver.findElement(By.id("selectorHotel"));
		WebElement tipohabitacion = driver.findElement(By.name("tipoHabitacion"));
		WebElement fechaentrada = driver.findElement(By.name("fechaEntrada"));
		WebElement fechasalida = driver.findElement(By.name("fechaSalida"));
		
		selector.sendKeys("Blue Lagoon");
		tipohabitacion.sendKeys("Normal");
		fechaentrada.sendKeys("20/08/2024");
		fechasalida.sendKeys("27/08/2024");
		
		driver.findElement(By.id("boton_enviar_reserva")).click();
		dormir(3000);
		
		WebElement encabezado = driver.findElement(By.tagName("h4"));
		
		assertTrue(encabezado.getText().equals("Ha surgido un error con la reserva:"), "Recibido: " + encabezado.getText()+ "y: Ha surgido un error con la reserva:");
	}
	
	@Test
	@DisplayName("TEST 4: EDITAR RESERVA")
	public void editarReservaExitoTest() {
		this.iniciarSesionUsuario();
		
		driver.findElement(By.id("gestionReservas")).click();
		dormir(3000);
		
		WebElement tabla = driver.findElement(By.id("tablaReservas"));
		List<WebElement> filas = tabla.findElements(By.tagName("tr"));
		
		WebElement ultimafila = filas.get(filas.size()-1);
		int ultimonumeroProm = filas.size()-1;
		WebElement fechaInicio1 = ultimafila.findElement(By.xpath("//tr["+ ultimonumeroProm +"]/td[4]"));
		WebElement fechaSalida1 = ultimafila.findElement(By.xpath("//tr["+ ultimonumeroProm +"]/td[5]"));
		
		String fechaInicio1String = fechaInicio1.getText();
		String fechaSalida1String = fechaSalida1.getText();
		
		
		ultimafila.findElement(By.xpath(".//td[6]")).click();
		dormir(3000);
		
		//Entramos en el formulario
		WebElement tipohabitacion = driver.findElement(By.name("tipoHabitacion"));
		WebElement fechaentrada = driver.findElement(By.name("fechaEntrada"));
		WebElement fechasalida = driver.findElement(By.name("fechaSalida"));
		
		
		tipohabitacion.sendKeys("Normal");
		fechaentrada.sendKeys("12/12/2024");
		fechasalida.sendKeys("15/12/2024");
		
		driver.findElement(By.id("boton_enviar_reserva")).click();
		dormir(3000);
		
		WebElement tabla1 = driver.findElement(By.id("tablaReservas"));
		List<WebElement> filas1 = tabla1.findElements(By.tagName("tr"));
		
		WebElement ultimafila1 = filas1.get(filas1.size()-1);
		int ultimonumeroProm1 = filas1.size()-1;
		WebElement fechaInicioEdit = ultimafila1.findElement(By.xpath("//tr["+ ultimonumeroProm1 +"]/td[4]"));
		WebElement fechaSalidaEdit = ultimafila1.findElement(By.xpath("//tr["+ ultimonumeroProm1 +"]/td[5]"));
		
		String fechaInicioEditString = fechaInicioEdit.getText();
		String fechaSalidaEditString = fechaSalidaEdit.getText();
		
		assertTrue(!fechaInicio1String.equals(fechaInicioEditString) && !fechaSalida1String.equals(fechaSalidaEditString));
	}
	
	@Test
	@DisplayName("TEST 5: ELIMINAR RESERVA")
	public void eliminarReservaTest() {
		this.iniciarSesionUsuario();
		
		driver.findElement(By.id("gestionReservas")).click();
		dormir(3000);
		
		WebElement tabla = driver.findElement(By.id("tablaReservas"));
		List<WebElement> filas = tabla.findElements(By.tagName("tr"));
		
		Integer numfilasinicial = filas.size();
		
		
		WebElement ultimafila = filas.get(numfilasinicial-1);
		ultimafila.findElement(By.xpath(".//td[7]")).click();
		
		dormir(3000);
		
		WebElement tabla2 = driver.findElement(By.id("tablaReservas"));
		List<WebElement> filasfinal = tabla2.findElements(By.tagName("tr"));
		Integer numfilasfinal = filasfinal.size();
		
		assertTrue(numfilasinicial.compareTo(numfilasfinal) > 0, "Recibido: " + numfilasinicial+ "y: "+ numfilasfinal);
	}
	
	
	
	
	
	//FUNCIONES AUXILIARES
	
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
	
	public void dormir(int milisegundos) {
		try {
			Thread.sleep(milisegundos);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
