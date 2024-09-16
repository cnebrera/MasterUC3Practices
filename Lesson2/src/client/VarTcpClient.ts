import * as net from 'net';
import VarSizeMessage from '../models/VarSizeMessage';
import VarTcpServer from '../server/VarTcpServer';

/**
 * TCP Client class that handles variable-size messages
 */
class VarTcpClient {
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
        this.client.on('close', () => console.log('Connection closed'));
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
        this.handleVarSizeMessage();
    }

    /**
     * Handle variable-size messages with a header indicating the message size
     */
    private handleVarSizeMessage(): void {
        const headerSize = VarTcpServer.VAR_HEADER_SIZE;
        while (this.buffer.length >= headerSize) {
            // Check if we have enough data to read the message size (first 4 bytes)
            const messageSize = this.buffer.readInt32BE(0);
            const messageWithHeaderSize = headerSize + messageSize;
            // Check if we have the entire message in the buffer
            if (this.buffer.length >= messageWithHeaderSize) {
                // Extract message data
                const messageBuffer = this.buffer.subarray(headerSize, messageWithHeaderSize);
                // Remove processed message from buffer
                this.buffer = this.buffer.subarray(messageWithHeaderSize);

                // TODO 8: Decode the variable-size message from the buffer and print it
                // Hint: Use `VarSizeMessage.fromBinary` for decoding.
                // const message = ;

                this.doSomethingWithMessage(message);
            } else {
                // If we don't have enough data yet, break the loop and wait for more data
                break;
            }
        }
    }

    /**
     * Process the received variable-size message
     * @param message The variable-size message to process
     */
    private doSomethingWithMessage(message: VarSizeMessage): void {
        console.log("Received:", message.toString());
    }
}

export default VarTcpClient;
