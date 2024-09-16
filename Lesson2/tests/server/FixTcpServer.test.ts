import * as net from 'net';
import FixSizeMessage from '../../src/models/FixSizeMessage';
import FixTcpServer from '../../src/server/FixTcpServer';

describe('TCP Server', () => {
    const TEST_TIMEOUT = 2000;

    it('should correctly send a fixed-size message to the client', (done) => {
        const server = new FixTcpServer();
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

                // Deserialize the fixed-size message from the buffer
                const message = FixSizeMessage.fromBinary(data);
                expect(message.instrument).toBeGreaterThan(0);
                expect(message.price).toBeGreaterThan(0);
                expect(message.volume).toBeGreaterThan(0);
                expect(data.length).toBe(16);

                // Tess passed!
                client.end();
                server.stop(done);
            });
        });
    });
});
