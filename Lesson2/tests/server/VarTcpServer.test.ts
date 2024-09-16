import * as net from 'net';
import VarSizeMessage from '../../src/models/VarSizeMessage';
import VarTcpServer from '../../src/server/VarTcpServer';

describe('TCP Server', () => {
    const TEST_TIMEOUT = 2000;

    it('should correctly send a variable-size message with header to the client', (done) => {
        const server = new VarTcpServer(100);
        server.start(6789, () => {
            // Create a client to connect to the server
            const client = new net.Socket();
            client.connect(6789, '127.0.0.1', () => {
                console.log('Client connected to server');
            });

            // Set up a timeout to fail the test if no data is received
            const timeout = setTimeout(() => {
                client.end();
                server.stop(() => {
                    done(new Error('Test failed: No data received within the timeout period'));
                });
            }, TEST_TIMEOUT);

            client.on('error', (err) => {
                clearTimeout(timeout);
                done(err);
            });

            client.on('data', (data: Buffer) => {
                clearTimeout(timeout);

                // Read the message size from the header
                const messageSize = data.readInt32BE(0);
                const messageBuffer = data.subarray(4);

                // Deserialize the variable-size message from the buffer
                const message = VarSizeMessage.fromBinary(messageBuffer);
                expect(messageBuffer.length).toBe(messageSize);
                expect(message.instrument).toBeGreaterThan(0);
                expect(message.price).toBeGreaterThan(0);
                expect(message.volume).toBeGreaterThan(0);
                expect(message.metadata.length).toBe(100);

                expect(data.length).toBe(120);

                // Tess passed!
                client.end();
                server.stop(done);
            });
        });
    });
});
