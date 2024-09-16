import * as net from 'net';
import VarSizeMessage from '../models/VarSizeMessage';

/**
 * TCP server that sends variable-size messages to connected clients
 */
class VarTcpServer {
    public static readonly VAR_HEADER_SIZE = 4;

    private readonly MESSAGE_INTERVAL = 1000; // Interval in milliseconds for sending messages

    private server: net.Server;
    private metadataLength: number;
    private intervalId: NodeJS.Timeout | null = null; // To store the interval ID for stopping the periodic messages

    /**
     * Create a new Var-Size TCP server
     * @param metadataLength - The length of the metadata for variable-size messages
     */
    constructor(metadataLength: number) {
        this.metadataLength = metadataLength;

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
            console.log(`Server is listening on port ${port} for incoming clients (metadata length: ${this.metadataLength})...`);
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
     * Handle the connection to a client and periodically send variable-size messages
     * @param socket The client socket connection
     */
    private handleClientConnection(socket: net.Socket): void {
        console.log('Client connected');

        // Start sending variable-size messages periodically
        this.intervalId = setInterval(() => {
            this.sendVarSizeMessage(socket);
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
     * Send a variable-size message to the client
     * @param socket The client socket connection
     */
    private sendVarSizeMessage(socket: net.Socket): void {
        const randomMessage = VarSizeMessage.generateRandomMsg(this.metadataLength);
        const messageBuffer = randomMessage.toBinary();
        const headerBuffer = Buffer.alloc(VarTcpServer.VAR_HEADER_SIZE);
        headerBuffer.writeInt32BE(messageBuffer.length, 0);

        // Safely write the header and message to the socket
        if (socket.writable) {
            // TODO 7: Write the header and variable-size message to the socket
            // Hint: Combine the header and message buffers using `Buffer.concat` and send using `socket.write`.
            // ...

            console.log("Sent:", randomMessage.toString());
        } else {
            console.log('Socket is not writable, skipping message.');
        }
    }
}

export default VarTcpServer;
