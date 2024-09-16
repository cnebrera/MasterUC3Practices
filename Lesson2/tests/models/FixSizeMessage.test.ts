import FixSizeMessage from '../../src/models/FixSizeMessage';

describe('FixSizeMessage', () => {
    it('should correctly serialize a fixed-size message into a buffer', () => {
        const message = new FixSizeMessage(1234, 56.78, 9000);
        const buffer = Buffer.alloc(FixSizeMessage.MSG_BINARY_SIZE);

        // Serialize to binary
        message.toBinary(buffer);

        // Check that buffer content matches the original data
        expect(buffer.readInt32BE(0)).toBe(1234); // instrument
        expect(buffer.readDoubleBE(4)).toBe(56.78); // price
        expect(buffer.readInt32BE(12)).toBe(9000); // volume
    });

    it('should correctly deserialize a buffer into a fixed-size message', () => {
        // Create a buffer with known values
        const buffer = Buffer.alloc(FixSizeMessage.MSG_BINARY_SIZE);
        buffer.writeInt32BE(1234, 0); // instrument
        buffer.writeDoubleBE(56.78, 4); // price
        buffer.writeInt32BE(9000, 12); // volume

        // Deserialize from binary
        const message = FixSizeMessage.fromBinary(buffer);

        // Check that deserialized values match the buffer
        expect(message.instrument).toBe(1234);
        expect(message.price).toBe(56.78);
        expect(message.volume).toBe(9000);
    });

    it('should generate a random fixed-size message with valid values', () => {
        const randomMessage = FixSizeMessage.generateRandomMsg();

        // Check that the random message fields are valid numbers
        expect(randomMessage.instrument).toBeGreaterThan(0);
        expect(randomMessage.price).toBeGreaterThan(0);
        expect(randomMessage.volume).toBeGreaterThan(0);
    });
});
