import VarSizeMessage from '../../src/models/VarSizeMessage';

describe('VarSizeMessage', () => {
    it('should correctly serialize a variable-size message into a buffer', () => {
        const metadata = 'Test metadata';
        const message = new VarSizeMessage(5678, 123.45, 7890, metadata);
        const buffer = message.toBinary();

        // Check that buffer content matches the original data
        expect(buffer.readInt32BE(0)).toBe(5678); // instrument
        expect(buffer.readDoubleBE(4)).toBe(123.45); // price
        expect(buffer.readInt32BE(12)).toBe(7890); // volume

        // Check that metadata was serialized correctly
        const metadataFromBuffer = buffer.slice(16).toString('utf-8');
        expect(metadataFromBuffer).toBe(metadata);
    });

    it('should correctly deserialize a buffer into a variable-size message', () => {
        const metadata = 'Test metadata';
        const metadataBuffer = Buffer.from(metadata, 'utf-8');

        // Create a buffer with known values
        const buffer = Buffer.alloc(16 + metadataBuffer.length);
        buffer.writeInt32BE(5678, 0); // instrument
        buffer.writeDoubleBE(123.45, 4); // price
        buffer.writeInt32BE(7890, 12); // volume
        metadataBuffer.copy(buffer, 16); // metadata

        // Deserialize from binary
        const message = VarSizeMessage.fromBinary(buffer);

        // Check that deserialized values match the buffer
        expect(message.instrument).toBe(5678);
        expect(message.price).toBe(123.45);
        expect(message.volume).toBe(7890);
        expect(message.metadata).toBe(metadata);
    });

    it('should generate a random variable-size message with valid values', () => {
        const randomMessage = VarSizeMessage.generateRandomMsg(50);

        // Check that the random message fields are valid
        expect(randomMessage.instrument).toBeGreaterThan(0);
        expect(randomMessage.price).toBeGreaterThan(0);
        expect(randomMessage.volume).toBeGreaterThan(0);
        expect(randomMessage.metadata.length).toBe(50);
    });
});
