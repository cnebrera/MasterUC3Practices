import * as dgram from 'dgram';
import VarSizeMessage from '../models/VarSizeMessage';

/**
 * Multicast server that periodically sends variable-size messages to a multicast group
 */
class MulticastServer {
    private socket: dgram.Socket;
    private readonly MULTICAST_GROUP = '239.255.0.1'; // Multicast group address
    private readonly MESSAGE_INTERVAL = 1000; // Send a message every 1 second
    private metadataLength: number;
    private intervalId: NodeJS.Timeout | null = null; // Store the interval ID
    private isSocketClosed = true; // Track the socket state

    /**
     * Creates a new multicast server instance
     * @param metadataLength Length of the metadata in each message
     */
    constructor(metadataLength = 100) {
        this.metadataLength = metadataLength;
        this.socket = dgram.createSocket('udp4'); // Create a UDP socket (IPv4)
    }

    /**
     * Start the multicast server on the specified port
     * @param port The port to send multicast messages to (default: 6789)
     */
    public start(port = 6789): void {
        this.isSocketClosed = false; // Reset socket closed state
        this.socket.bind(() => {
            this.socket.setBroadcast(true);
            this.socket.setMulticastTTL(128); // Time-to-live for multicast messages
            this.socket.addMembership(this.MULTICAST_GROUP); // Join the multicast group
            console.log(`Multicast server started on port ${port}`);
            this.sendPeriodicMessages(port);
        });
    }

    /**
     * Stop the multicast server by clearing the interval and closing the socket
     */
    public stop(): void {
        if (this.intervalId) {
            clearInterval(this.intervalId);
            this.intervalId = null;
        }

        this.socket.close(() => {
            console.log('Multicast server stopped');
            this.isSocketClosed = true; // Mark the socket as closed
        });
    }

    /**
     * Check if the socket is closed
     * @returns True if the socket is closed, false otherwise
     */
    public isClosed(): boolean {
        return this.isSocketClosed;
    }

    /**
     * Periodically send VarSizeMessage to the multicast group
     * @param port The port to send messages to
     */
    private sendPeriodicMessages(port: number): void {
        this.intervalId = setInterval(() => {
            const randomMessage = VarSizeMessage.generateRandomMsg(this.metadataLength);
            const messageBuffer = randomMessage.toBinary();
            const headerBuffer = Buffer.alloc(4);
            headerBuffer.writeInt32BE(messageBuffer.length, 0);

            // TODO 9: Create the packet (header + message) to be sent to the multicast group
            // const packet = ;

            this.socket.send(packet, port, this.MULTICAST_GROUP, (err) => {
                if (err) {
                    console.error('Error sending message:', err);
                } else {
                    console.log("Sent:", randomMessage.toString());
                }
            });
        }, this.MESSAGE_INTERVAL);
    }
}

export default MulticastServer;
