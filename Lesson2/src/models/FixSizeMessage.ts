import { randomInt } from 'crypto';

/**
 * Represents a fixed-size message
 * Each message contains an instrument (number), price (number), and volume (number).
 */
class FixSizeMessage {
    public static readonly MSG_BINARY_SIZE = 16;

    /**
     * Creates a new fixed-size message
     * @param instrument - Instrument ID as a number (integer BE, 4 bytes)
     * @param price - Price as a floating-point number (double BE, 8 bytes)
     * @param volume - Volume as a number (integer BE, 4 bytes)
     */
    constructor(public instrument: number, public price: number, public volume: number) { }

    /**
     * Serializes the message into binary format and writes it into the buffer
     * @param buffer - The buffer to store the binary data
     */
    public toBinary(buffer: Buffer): void {
        // TODO 1: Serialize the instrument, price, and volume into the buffer
        // Hint: Use `buffer.writeInt32BE` for integers and `buffer.writeDoubleBE` for the floating-point number.
        // Remember to increase the `offset` while you write to the `buffer`.
    }

    /**
     * Deserializes a message from binary format
     * @param buffer - The buffer containing the binary message (must be 16 bytes)
     * @returns A new FixSizeMessage instance
     */
    public static fromBinary(buffer: Buffer): FixSizeMessage {
        // TODO 2: Serialize the instrument, price, and volume into the buffer
        // Hint: Use `buffer.writeInt32BE` for integers and `buffer.writeDoubleBE` for the floating-point number.
        // Remember to increase the `offset` while you read to the `buffer`.
        // const instrument = ;
        // const price = ;
        // const volume = ;

        return new FixSizeMessage(instrument, price, volume);
    }

    /**
     * Generates a random fixed-size message
     * @returns A randomly generated FixSizeMessage instance
     */
    public static generateRandomMsg(): FixSizeMessage {
        return new FixSizeMessage(
            randomInt(1, 10000),
            parseFloat((Math.random() * 1000).toFixed(2)),
            randomInt(1, 10000),
        );
    }

    /**
     * Returns a string representation of the message
     * @returns A string describing the message
     */
    public toString(): string {
        return `FixSizeMessage(${FixSizeMessage.MSG_BINARY_SIZE}){instrument=${this.instrument}, price=${this.price}, volume=${this.volume}}`;
    }
}

export default FixSizeMessage;
