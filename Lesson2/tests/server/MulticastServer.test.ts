import MulticastServer from '../../src/server/MulticastServer';
import * as dgram from 'dgram';
import VarSizeMessage from '../../src/models/VarSizeMessage';

describe('MulticastServer', () => {
    let server: MulticastServer;
    let client: dgram.Socket;
    const MULTICAST_GROUP = '239.255.0.1';
    const MULTICAST_PORT = 6789;

    beforeEach((done) => {
        // Create and start the multicast server
        server = new MulticastServer(50); // Metadata length of 50
        server.start();

        // Create a UDP client to simulate the multicast client
        client = dgram.createSocket({ type: 'udp4', reuseAddr: true });
        client.bind(MULTICAST_PORT, () => {
            client.addMembership(MULTICAST_GROUP); // Join the multicast group
            done();
        });
    });

    afterEach((done) => {
        // Stop the server and close the client after each test
        server.stop();
        client.close(() => done());
    });

    it('should send multicast messages and the client should receive them', (done) => {
        // Listen for a message from the server
        client.once('message', (msg: Buffer) => {
            const messageSize = msg.readInt32BE(0);
            const messageBuffer = msg.slice(4);
            const receivedMessage = VarSizeMessage.fromBinary(messageBuffer);

            // Check if the received message matches the expected size
            expect(messageSize).toBe(messageBuffer.length);
            expect(receivedMessage.metadata.length).toBe(50); // Metadata length should be 50
            done(); // Mark the test as done
        });
    });
});
