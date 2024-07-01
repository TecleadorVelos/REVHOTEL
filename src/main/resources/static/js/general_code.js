// Función para generar números de promoción incremental
function generarNumerosPromocion() {
    // Obtener todas las celdas de número de promoción
    var celdasNumeroPromocion = document.querySelectorAll('#tablaPromociones .numero-promocion');
    
    // Iterar sobre cada celda y asignar el número de promoción incremental
    for (var i = 0; i < celdasNumeroPromocion.length; i++) {
        celdasNumeroPromocion[i].textContent = i + 1;
    }
}

// Ejecutar la función al cargar el contenido de la página
document.addEventListener("DOMContentLoaded", function() {
    // Llamar a la función para generar los números de promoción
    generarNumerosPromocion();
    
    //Continuar
});