import * as net from 'net';
import FixSizeMessage from '../../src/models/FixSizeMessage';
import FixTcpClient from '../../src/client/FixTcpClient';

describe('FixTcpClient', () => {
    let server: net.Server;
    let client: FixTcpClient;

    beforeEach((done) => {
        // Create the server
        server = net.createServer();

        // Add the error listener in the beforeEach block
        server.on('error', (err) => {
            console.error('Server error:', err);
            done(err); // Fail the test if the server fails
        });

        // Start listening on the port
        server.listen(6789, '127.0.0.1', () => {
            console.log('Server started on port 6789');
            done(); // Proceed once the server has started
        });
    });

    afterEach((done) => {
        // Gracefully close the client after each test to free up the port
        if (client) {
            client.stop();
        }
        // Gracefully close the server after each test to free up the port
        if (server) {
            server.close((err) => {
                if (err) {
                    console.error('Error closing the server:', err);
                    done(err); // Fail the test if there was an issue closing the server
                } else {
                    // console.log('Server closed');
                    done(); // Proceed once the server is successfully closed
                }
            });
        } else {
            done(); // No server to close, proceed
        }
    });

    it('should correctly receive and handle a fixed-size message', (done) => {
        const fixMessage = new FixSizeMessage(1234, 56.78, 9000);

        // Add the connection listener inside the test
        server.on('connection', (socket) => {
            // Simulate sending a fixed-size message
            const buffer = Buffer.alloc(FixSizeMessage.MSG_BINARY_SIZE);
            fixMessage.toBinary(buffer);
            socket.write(buffer);
            socket.end(); // Close the connection after sending the message
        });

        client = new FixTcpClient();
        client.start(6789);

        // Spy on printMessage to verify the message was received and processed
        client['doSomethingWithMessage'] = (message: FixSizeMessage) => {
            try {
                expect(message.toString()).toBe(fixMessage.toString());
                done();
            } catch (err) {
                done(err);
            }
        };
    });
});
