import { randomInt } from 'crypto';

/**
 * Represents a variable-size message
 * Each message contains an instrument, price, volume, and metadata (which varies in size).
 */
class VarSizeMessage {
    private static readonly STRING_LOG_MAX = 100;

    public instrument: number;
    public price: number;
    public volume: number;
    public metadata: string;

    /**
     * Creates a new variable-size message
     * @param instrument - Instrument ID as a number
     * @param price - Price as a floating-point number
     * @param volume - Volume as a number
     * @param metadata - Additional metadata as a string
     */
    constructor(instrument: number, price: number, volume: number, metadata: string) {
        this.instrument = instrument;
        this.price = price;
        this.volume = volume;
        this.metadata = metadata;
    }

    /**
     * Serializes the message into binary format
     * @returns A buffer containing the binary representation of the message
     */
    public toBinary(): Buffer {
        const metadataBinary = Buffer.from(this.metadata, 'utf-8');
        const msgSize = 4 + 4 + 8 + metadataBinary.length; // 4 for instrument, 4 for volume, 8 for price
        const buffer = Buffer.alloc(msgSize);

        // TODO 5: Serialize the instrument, price and volume into the buffer
        // Hint: Use `buffer.writeInt32BE` for integers and `buffer.writeDoubleBE` for the floating-point number.
        // buffer.;     // Instrument: 4 bytes
        // buffer.;     // Price: 4 bytes
        // buffer.;     // Volume: 8 bytes
        metadataBinary.copy(buffer, 16);

        return buffer;
    }

    /**
     * Deserializes a message from binary format
     * @param buffer - The buffer containing the binary message
     * @returns A new VarSizeMessage instance
     */
    public static fromBinary(buffer: Buffer): VarSizeMessage {
        // TODO 6: Deserialize the instrument, volume, price, and metadata from the buffer
        // Hint: Use `buffer.readInt32BE` for integers, `buffer.readDoubleBE` for the floating-point number, and `buffer.subarray` for metadata.
        // Remember to increase the `offset` while you write to the `buffer`.
        // const instrument = ;
        // const volume = ;
        // const price = ;
        // const metadataBinary = ;
        const metadata = metadataBinary.toString('utf-8');

        return new VarSizeMessage(instrument, price, volume, metadata);
    }

    /**
     * Generates a random variable-size message
     * @param metadataLength - Length of the metadata string
     * @returns A randomly generated VarSizeMessage instance
     */
    public static generateRandomMsg(metadataLength: number): VarSizeMessage {
        const instrument = randomInt(1, 10000);
        const price = parseFloat((Math.random() * 1000).toFixed(2));
        const volume = randomInt(1, 10000);
        const metadata = VarSizeMessage.generateRandomMetadata(metadataLength);

        return new VarSizeMessage(instrument, price, volume, metadata);
    }

    /**
     * Generates a random metadata string of the given length
     * @param length - The length of the metadata string
     * @returns A random string of the given length
     */
    private static generateRandomMetadata(length: number): string {
        const chars = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        let result = '';
        for (let i = 0; i < length; i++) {
            result += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        return result;
    }

    /**
     * Returns a string representation of the message
     * @returns A string describing the message
     */
    public toString(): string {
        return `VarSizeMessage(${this.length()}){instrument=${this.instrument}, price=${this.price}, volume=${this.volume}, metadata=${this.metadata}}`;
    }

    /**
     * Calculates the length of the message in bytes
     * @returns The length of the message
     */
    public length(): number {
        return 4 + 4 + 8 + this.metadata.length;
    }
}

export default VarSizeMessage;
