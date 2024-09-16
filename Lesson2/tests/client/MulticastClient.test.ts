import MulticastClient from '../../src/client/MulticastClient';
import * as dgram from 'dgram';
import VarSizeMessage from '../../src/models/VarSizeMessage';

describe('MulticastClient', () => {
    let client: MulticastClient;
    let server: dgram.Socket;
    const MULTICAST_GROUP = '239.255.0.1';
    const MULTICAST_PORT = 6789;

    beforeEach((done) => {
        // Create a UDP server to simulate the multicast server
        server = dgram.createSocket({ type: 'udp4', reuseAddr: true });
        server.bind(MULTICAST_PORT, () => {
            server.addMembership(MULTICAST_GROUP);
            done();
        });
    });

    afterEach((done) => {
        // Clean up the client and server after each test
        if (client) {
            client.stop(); // Stop the multicast client
        }
        server.close(() => {
            done(); // Stop the server
        });
    });

    it('should receive a multicast message', (done) => {
        // Create and start the multicast client
        client = new MulticastClient();
        client.start();

        // Send a test message from the server
        const testMessage = VarSizeMessage.generateRandomMsg(50); // Generate random message
        const messageBuffer = testMessage.toBinary();
        const headerBuffer = Buffer.alloc(4);
        headerBuffer.writeInt32BE(messageBuffer.length, 0);

        // Wait for the client to bind before sending the message
        setTimeout(() => {
            // Send the message from the server
            server.send(Buffer.concat([headerBuffer, messageBuffer]), MULTICAST_PORT, MULTICAST_GROUP);
        }, 1000);

        // Listen for the message on the client side using its internal event handling
        client['client'].once('message', (msg: Buffer) => {
            const messageSize = msg.readInt32BE(0);
            const messageBuffer = msg.slice(4);
            const receivedMessage = VarSizeMessage.fromBinary(messageBuffer);

            // Check if the received message matches what was sent
            expect(messageSize).toBe(messageBuffer.length);
            expect(receivedMessage.metadata.length).toBe(50); // Assuming metadata length is 50
            done(); // Mark the test as done
        });
    });
});
