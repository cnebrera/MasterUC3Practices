import * as net from 'net';
import VarSizeMessage from '../../src/models/VarSizeMessage';
import VarTcpClient from '../../src/client/VarTcpClient';

describe('VarTcpClient', () => {
    let server: net.Server;
    let client: VarTcpClient;

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

    it('should correctly receive and handle a variable-size message with header', (done) => {
        const varMessage = new VarSizeMessage(5678, 123.45, 7890, 'Test metadata');

        // Add the connection listener inside the test
        server.on('connection', (socket) => {
            // Simulate sending a var-size message
            const varMessage = new VarSizeMessage(5678, 123.45, 7890, 'Test metadata');
            const messageBuffer = varMessage.toBinary();
            const headerBuffer = Buffer.alloc(4);
            headerBuffer.writeInt32BE(messageBuffer.length, 0);
            socket.write(headerBuffer);
            socket.write(messageBuffer);
            socket.end();
        });

        client = new VarTcpClient();
        client.start(6789);

        // Spy on printMessage to verify the message was received and processed
        client['doSomethingWithMessage'] = (message: VarSizeMessage) => {
            try {
                expect(message.toString()).toBe(varMessage.toString());
                done();
            } catch (err) {
                done(err);
            }
        };
    });
});
