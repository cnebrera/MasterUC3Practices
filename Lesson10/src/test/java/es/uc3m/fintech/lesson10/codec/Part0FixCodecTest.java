package es.uc3m.fintech.lesson10.codec;

import es.uc3m.fintech.lesson10.codec.FixCodec.Field;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Parte 0: codificación FIX a mano. */
class Part0FixCodecTest {

    /** Ejemplo del enunciado. */
    private static final String HEARTBEAT =
            "8=FIXT.1.1|9=63|35=0|49=STUDENT|56=UC3M_EXCHANGE|34=2|52=20261024-10:00:00.000|10=231|";

    /** Convierte la notación con '|' a SOH. */
    private static String soh(String readable) {
        return readable.replace('|', FixCodec.SOH);
    }

    private static List<Field> heartbeatFields() {
        return List.of(
                new Field(35, "0"),
                new Field(49, "STUDENT"),
                new Field(56, "UC3M_EXCHANGE"),
                new Field(34, "2"),
                new Field(52, "20261024-10:00:00.000"));
    }

    @Test
    void checksumOfHeartbeat() {
        String withoutChecksum = HEARTBEAT.substring(0, HEARTBEAT.indexOf("10="));
        assertEquals("231", FixCodec.checksum(soh(withoutChecksum)));
    }

    @Test
    void encodeHeartbeat() {
        assertEquals(HEARTBEAT, FixCodec.toReadable(FixCodec.encode("FIXT.1.1", heartbeatFields())));
    }

    @Test
    void decodeHeartbeat() {
        List<Field> fields = FixCodec.decode(soh(HEARTBEAT));
        assertEquals(8, fields.size());
        assertEquals(new Field(8, "FIXT.1.1"), fields.get(0));
        assertEquals(new Field(9, "63"), fields.get(1));
        assertEquals(new Field(35, "0"), fields.get(2));
        assertEquals(new Field(10, "231"), fields.get(7));
    }

    @Test
    void decodeRejectsWrongChecksum() {
        assertThrows(IllegalArgumentException.class,
                () -> FixCodec.decode(soh(HEARTBEAT.replace("10=231", "10=232"))));
    }
}
