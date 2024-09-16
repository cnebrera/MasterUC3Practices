import yargs from 'yargs';
import { hideBin } from 'yargs/helpers';
import FixTcpServer from './server/FixTcpServer';
import VarTcpServer from './server/VarTcpServer';
import FixTcpClient from './client/FixTcpClient';
import VarTcpClient from './client/VarTcpClient';
import MulticastServer from './server/MulticastServer';
import MulticastClient from './client/MulticastClient';

// Define argument types for yargs
interface Args {
    role: 'server' | 'client';
    protocol: 'tcp-fix' | 'tcp-var' | 'multicast';
    metadata: number;
}

// Parse command-line arguments using yargs and enforce typing
const argv = yargs(hideBin(process.argv))
    .command('$0 <protocol> <role>', 'Start the server or client with the given protocol', (yargs) => {
        return yargs
            .positional('protocol', {
                describe: 'Specify the protocol: "tcp-fix", "tcp-var", or "multicast"',
                type: 'string',
                choices: ['tcp-fix', 'tcp-var', 'multicast'] as const,
                demandOption: true,
            })
            .positional('role', {
                describe: 'Specify the role: "server" or "client"',
                type: 'string',
                choices: ['server', 'client'] as const,
                demandOption: true,
            })
            .option('metadata', {
                alias: 'm',
                description: 'Specify the metadata length for variable-size messages (default is 20)',
                type: 'number',
                default: 20,
            });
    })
    .help()
    .alias('help', 'h').argv as unknown as Args;

/**
 * Run the server based on the specified protocol
 * @param protocol The protocol to use: "tcp-fix", "tcp-var", or "multicast"
 * @param metadataLength The length of the metadata for variable-size messages
 */
function runServer(protocol: 'tcp-fix' | 'tcp-var' | 'multicast', metadataLength: number) {
    if (protocol === 'tcp-fix') {
        const server = new FixTcpServer();
        server.start(6789);
    } else if (protocol === 'tcp-var') {
        const server = new VarTcpServer(metadataLength);
        server.start(6789);
    } else if (protocol === 'multicast') {
        const multicastServer = new MulticastServer(metadataLength);
        multicastServer.start();
    } else {
        console.error('Unsupported protocol. Please use "tcp-fix", "tcp-var", or "multicast".');
        process.exit(1);
    }
}

/**
 * Run the client based on the specified protocol
 * @param protocol The protocol to use: "tcp-fix", "tcp-var", or "multicast"
 */
function runClient(protocol: 'tcp-fix' | 'tcp-var' | 'multicast') {
    if (protocol === 'tcp-fix') {
        const client = new FixTcpClient();
        client.start(6789);
    } else if (protocol === 'tcp-var') {
        const client = new VarTcpClient();
        client.start(6789);
    } else if (protocol === 'multicast') {
        const multicastClient = new MulticastClient();
        multicastClient.start();
    } else {
        console.error('Unsupported protocol. Please use "tcp-fix", "tcp-var", or "multicast".');
        process.exit(1);
    }
}

// Main logic to determine what to run based on role and protocol
const metadataLength = argv.metadata;

if (argv.role === 'server') {
    runServer(argv.protocol, metadataLength);
} else if (argv.role === 'client') {
    runClient(argv.protocol);
} else {
    console.error('Please specify "server" or "client" as the role.');
    process.exit(1);
}
