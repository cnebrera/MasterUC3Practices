import * as net from 'net';
import FixSizeMessage from '../models/FixSizeMessage';

class FixTcpServer {
    private readonly MESSAGE_INTERVAL = 1000; // Interval in milliseconds for sending messages

    private server: net.Server;
    private intervalId: NodeJS.Timeout | null = null; // Stores the interval ID for stopping periodic messages

    /**
     * Create a new Fix-Size TCP server
     */
    constructor() {
        // Create the TCP server
        this.server = net.createServer((socket) => this.handleClientConnection(socket));
    }

    /**
     * Start the server on the specified port
     * @param port The port to listen on
     * @param onReady Optional callback to be executed once the server is ready
     */
    public start(port: number, onReady?: () => void): void {
        this.server.listen(port, '127.0.0.1', () => {
            console.log(`Server is listening on port ${port} for incoming clients...`);
            if (onReady) onReady();
        });
    }

    /**
     * Gracefully stop the server by closing all connections and freeing the port
     * @param callback Optional callback to be executed once the server has stopped
     */
    public stop(callback?: (err?: Error) => void): void {
        if (this.server) {
            this.server.close((err?: Error) => {
                console.log('Server closed');
                if (callback) callback(err);
            });
        }
    }

    /**
     * Handle the connection to a client and periodically send fixed-size messages
     * @param socket The client socket connection
     */
    private handleClientConnection(socket: net.Socket): void {
        console.log('Client connected');

        // Start sending messages periodically
        this.intervalId = setInterval(() => {
            this.sendFixSizeMessage(socket);
        }, this.MESSAGE_INTERVAL);

        // Handle socket disconnection (client disconnect)
        socket.on('end', () => {
            console.log('Client disconnected');
            if (this.intervalId) {
                clearInterval(this.intervalId); // Stop periodic messages when the client disconnects
            }
        });

        // Handle socket close
        socket.on('close', () => {
            console.log('Connection closed');
            if (this.intervalId) {
                clearInterval(this.intervalId); // Stop periodic messages when the client disconnects
            }
        });

        // Handle socket errors (e.g., ECONNRESET)
        socket.on('error', (err: NodeJS.ErrnoException) => {
            if (err.code === 'ECONNRESET') {
                console.log('Connection reset by client. Stopping message sending.');
                if (this.intervalId) {
                    clearInterval(this.intervalId); // Stop periodic messages on connection reset
                }
            } else {
                console.error('Socket error:', err);
            }
        });
    }

    /**
     * Send a fixed-size message to the client
     * @param socket The client socket connection
     */
    private sendFixSizeMessage(socket: net.Socket): void {
        const randomMessage = FixSizeMessage.generateRandomMsg();
        const messageBuffer = Buffer.alloc(FixSizeMessage.MSG_BINARY_SIZE);
        randomMessage.toBinary(messageBuffer);

        // Safely write the message to the socket
        if (socket.writable) {
            // TODO 3: Write the fixed-size message to the socket buffer
            // Hint: Use the `socket.write` method to send the buffer to the client.
            // ...

            console.log("Sent:", randomMessage.toString());
        } else {
            console.log('Socket is not writable, skipping message.');
        }
    }
}

export default FixTcpServer;
