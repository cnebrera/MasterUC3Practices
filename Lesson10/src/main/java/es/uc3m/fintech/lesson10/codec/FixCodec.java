package es.uc3m.fintech.lesson10.codec;

import java.util.ArrayList;
import java.util.List;

/**
 * Parte 0 - Codificación tag=value de FIX "a mano", sin QuickFIX/J.
 *
 * Esta clase NO puede usar ninguna clase de QuickFIX/J: el objetivo es entender
 * qué hace la librería por debajo.
 *
 * @author Mario Cao
 */
public final class FixCodec {

    /** Separador de campos FIX (Start Of Header, ASCII 0x01). */
    public static final char SOH = '\u0001';

    /** Un campo FIX: tag numérico y valor. */
    public record Field(int tag, String value) {
    }

    private FixCodec() {
    }

    /**
     * Calcula el valor del campo CheckSum(10).
     *
     * @param data todos los caracteres del mensaje que preceden al campo 10 (incluido el último SOH)
     * @return el checksum con exactamente 3 dígitos, por ejemplo "007" o "153"
     */
    public static String checksum(String data) {
        // TODO 0.1: Calcula el CheckSum(10) según la especificación FIX.
        throw new UnsupportedOperationException("TODO 0.1");
    }

    /**
     * Construye un mensaje FIX completo.
     *
     * @param beginString valor del tag 8 (por ejemplo "FIXT.1.1")
     * @param body        campos del mensaje en orden, empezando por MsgType(35).
     *                    No incluye BeginString(8), BodyLength(9) ni CheckSum(10).
     * @return el mensaje en formato tag=value separado por SOH, con 8, 9 y 10 calculados
     */
    public static String encode(String beginString, List<Field> body) {
        // TODO 0.2: Genera el mensaje completo: BeginString(8), BodyLength(9), body y CheckSum(10).
        throw new UnsupportedOperationException("TODO 0.2");
    }

    /**
     * Separa un mensaje FIX en sus campos y comprueba su CheckSum(10).
     *
     * @param raw mensaje en formato tag=value separado por SOH
     * @return todos los campos en el orden en que aparecen (incluidos 8, 9 y 10)
     * @throws IllegalArgumentException si el CheckSum(10) no es correcto
     */
    public static List<Field> decode(String raw) {
        // TODO 0.3: Separa los campos del mensaje y comprueba que el CheckSum(10) es correcto.
        throw new UnsupportedOperationException("TODO 0.3");
    }

    /** Devuelve el mensaje con '|' en lugar de SOH, para poder leerlo en consola. */
    public static String toReadable(String raw) {
        return raw.replace(SOH, '|');
    }
}
