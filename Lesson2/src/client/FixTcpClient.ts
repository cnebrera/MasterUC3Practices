import * as net from 'net';
import FixSizeMessage from '../models/FixSizeMessage';

/**
 * TCP Client class that handles fixed-size messages
 */
class FixTcpClient {
    private client: net.Socket;
    private buffer: Buffer = Buffer.alloc(0); // Buffer to accumulate incoming data

    constructor() {
        this.client = new net.Socket();
    }

    /**
     * Start the client and connect to the server
     * @param port The port to connect to
     * @param host The server address (default: '127.0.0.1')
     */
    public start(port: number, host = '127.0.0.1'): void {
        this.client.connect(port, host, () => {
            console.log(`Connected to server on ${host}:${port}`);
        });

        this.client.on('data', (data: Buffer) => this.handleData(data));
        this.client.on('close', () => console.log('Connection closed by server'));
        this.client.on('error', (err) => console.error('Client error:', err));
    }

    /**
     * Gracefully stop the client by ending the connection
     */
    public stop(): void {
        this.client.end(() => {
            console.log('Client connection closed');
        });
        this.client.destroy(); // Ensures the client is fully closed
    }

    /**
     * Handle incoming data from the server
     * Accumulates incoming data to handle potentially fragmented TCP packets.
     * @param data The data received from the server
     */
    private handleData(data: Buffer): void {
        // Accumulate data in buffer
        this.buffer = Buffer.concat([this.buffer, data]);
        this.handleFixSizeMessage();
    }

    /**
     * Handle fixed-size messages (MSG_BINARY_SIZE = 16 bytes)
     * Extract and process fixed-size messages from the accumulated buffer.
     */
    private handleFixSizeMessage(): void {
        const messageSize = FixSizeMessage.MSG_BINARY_SIZE;
        while (this.buffer.length >= messageSize) {
            // Extract the message of fixed size
            const messageBuffer = this.buffer.subarray(0, messageSize);
            // Remove processed message from buffer
            this.buffer = this.buffer.subarray(messageSize);

            // TODO 4: Deserialize the fixed-size message from the buffer and print it
            // Hint: Use `FixSizeMessage.fromBinary` to convert the buffer into a message object.
            // const message = ;

            this.doSomethingWithMessage(message);
        }
    }

    /**
     * Process the received fixed-size message
     * @param message The fixed-size message to process
     */
    private doSomethingWithMessage(message: FixSizeMessage): void {
        console.log("Received:", message.toString());
    }
}

export default FixTcpClient;
