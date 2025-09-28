# ENTREGABLE 3.2: Análisis Comparativo de Criptografía Simétrica vs Asimétrica

## Instrucciones
1. **Ejecuta el benchmark**: Compila y ejecuta la clase `Benchmark.java`
2. **Analiza los resultados**: Observa los tiempos de ejecución mostrados
3. **Responde las preguntas**: Basándote en los resultados obtenidos y tu comprensión teórica

---

## PARTE A: Análisis de Rendimiento

### A1. Comparación de Velocidad
**Pregunta**: Según los resultados de tu benchmark, ¿cuál es más rápido: AES o RSA?
- [ ] AES es más rápido
- [ ] RSA es más rápido  
- [ ] Son igual de rápidos

**Justifica tu respuesta con datos concretos de tu ejecución:**
```
Tiempo promedio AES: _____ ms
Tiempo promedio RSA: _____ ms
Diferencia: AES es aproximadamente _____ veces más rápido que RSA
```

### A2. Orden de Magnitud
**Pregunta**: ¿Cuál es aproximadamente el orden de magnitud de diferencia entre AES y RSA?
- [ ] AES es 2-5 veces más rápido
- [ ] AES es 10-50 veces más rápido
- [ ] AES es 100-1000 veces más rápido
- [ ] AES es más de 5000 veces más rápido

### A3. Operaciones RSA
**Pregunta**: En RSA, ¿qué operación es generalmente más rápida?
- [ ] Cifrado (con clave pública)
- [ ] Descifrado (con clave privada)
- [ ] Ambas tardan lo mismo

**Explica por qué:**
_________________________________________________

### A4. Limitaciones de Tamaño
**Pregunta**: ¿Qué pasaría si intentaras cifrar con RSA un mensaje de 1MB?
- [ ] Funcionaría normalmente, solo sería muy lento
- [ ] Fallaría porque RSA tiene límites de tamaño de datos
- [ ] Sería más rápido que AES para archivos grandes

---

## PARTE B: Casos de Uso Prácticos

### B1. Cifrado de Archivos Grandes
**Escenario**: Necesitas cifrar un archivo de 100MB para enviarlo por email.
**¿Qué algoritmo usarías?**
- [ ] RSA
- [ ] AES
- [ ] Cualquiera de los dos

**Justificación:**
_________________________________________________

### B2. Intercambio Seguro de Claves
**Escenario**: Dos personas que nunca se han conocido quieren establecer una comunicación segura por internet.
**¿Qué algoritmo usarías para el intercambio inicial de claves?**
- [ ] RSA
- [ ] AES
- [ ] Cualquiera de los dos

**Justificación:**
_________________________________________________

### B3. Chat en Tiempo Real
**Escenario**: Una aplicación de chat que envía miles de mensajes cortos por segundo.
**¿Qué algoritmo usarías para cifrar los mensajes?**
- [ ] RSA para cada mensaje
- [ ] AES para cada mensaje
- [ ] RSA para establecer la sesión, luego AES para los mensajes

**Justificación:**
_________________________________________________

### B4. Firma Digital
**Escenario**: Necesitas garantizar que un documento no ha sido modificado y verificar quién lo envió.
**¿Qué algoritmo usarías?**
- [ ] RSA
- [ ] AES
- [ ] Ninguno de los dos puede hacer esto

**Justificación:**
_________________________________________________

---

## PARTE C: Comprensión Conceptual

### C1. Criptografía Híbrida
**Pregunta**: ¿Por qué HTTPS usa tanto RSA como AES en lugar de solo uno de ellos?

**Respuesta:**
_________________________________________________
_________________________________________________
_________________________________________________

---

## ENTREGA
- **Formato**: Este mismo archivo con tus respuestas completadas
- **Nombre del archivo**: `LESSON5_[NOMBRE_APELLIDO].md`
- **Incluye**: Captura de pantalla de la ejecución del benchmark
