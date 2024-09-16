import * as dgram from 'dgram';
import VarSizeMessage from '../models/VarSizeMessage';

/**
 * Multicast Client class to receive variable-size messages over UDP
 */
class MulticastClient {
    private client: dgram.Socket;
    private readonly MULTICAST_GROUP = '239.255.0.1'; // Multicast group
    private readonly MULTICAST_PORT = 6789; // Port to receive multicast messages

    constructor() {
        this.client = dgram.createSocket({ type: 'udp4', reuseAddr: true }); // Allow address reuse
    }

    /**
     * Start the multicast client and listen for messages
     */
    public start(): void {
        this.client.on('listening', () => {
            const address = this.client.address();
            console.log(`Multicast client listening on ${address.address}:${address.port}`);
            this.client.addMembership(this.MULTICAST_GROUP); // Join the multicast group
        });

        this.client.on('message', (buffer: Buffer) => {
            // TODO 10: Parse the received message including the header and body
            // Hint: First, use `buffer.readInt32BE` to get the message size from the header. 
            // Then, use `buffer.subarray` starting from index 4 to extract the message body.
            // Use `VarSizeMessage.fromBinary` for decoding.
            // const messageSize = ;
            // const messageBuffer = ;
            // const message = ;

            console.log("Received:", message.toString());
        });

        // Bind to the same multicast port (MULTICAST_PORT) with address reuse
        this.client.bind(this.MULTICAST_PORT, () => {
            console.log(`Client bound to multicast port ${this.MULTICAST_PORT}`);
        });
    }

    /**
     * Stop the multicast client by closing the socket
     */
    public stop(): void {
        this.client.close(() => {
            console.log('Multicast client stopped');
        });
    }
}

export default MulticastClient;
