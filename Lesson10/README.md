# Lección 10 - Protocolos de mercado: FIX 5.0 SP2 (FIXT.1.1) con QuickFIX/J

Práctica sobre un mercado electrónico simulado, el **UC3M Exchange**: implementación del gateway de datos de
mercado del exchange (acceptor) y de un cliente (initiator) que se autentica y se suscribe a precios mediante
FIX 5.0 SP2 (FIXT.1.1).

El enunciado completo, con las *Rules of Engagement* que debe cumplir la implementación, se encuentra en el
documento de la práctica.

## Requisitos

- Java 21 o superior
- Maven 3.6+

## Partes y TODOs

El código está en `src/main/java/es/uc3m/fintech/lesson10`, en tres paquetes:

| Paquete | Contenido | TODOs |
|---|---|---|
| `codec` | `FixCodec`: codificación FIX a mano (Parte 0) | 0.1, 0.2, 0.3 |
| `client` | `InitiatorApp`, `InitiatorRunner`: el cliente | 1.1, 2.1, 2.3a, 2.3b |
| `exchange` | `AcceptorApp`, `MarketDataService`, `AcceptorRunner`: el exchange | 1.2, 2.2a, 2.2b, 2.4 |

## Comandos

```bash
mvn test                           # tests públicos (un subconjunto de los de la corrección)
mvn test -Dtest=Part0FixCodecTest  # solo los de una parte
mvn compile exec:java@acceptor     # terminal 1: UC3M Exchange
mvn compile exec:java@initiator    # terminal 2: cliente + guion de demostración
```

Debe iniciarse siempre primero el acceptor. Los mensajes FIX se guardan en `logs/`.

Hasta completar los TODOs, los tests fallan: es el comportamiento esperado.

## Consideraciones para la entrega

- Los tests incluidos sirven de guía; la evaluación final emplea una batería de pruebas más exhaustiva.
- Se corrige el código de `src/main` con el `pom.xml` y los tests de la asignatura; la carpeta `logs/` se utiliza para verificar la ejecución.
- No deben modificarse los nombres de paquetes y clases ni la firma de los métodos públicos.
- `FixCodec` no debe utilizar QuickFIX/J.
- El proyecto debe compilar.
- El ZIP debe incluir la carpeta `logs/` generada al ejecutar la demostración con la práctica completada.
- Las trazas FIX no deben modificarse.
