package bg.sofia.uni.fmi.mjt.cocktail.server.server;

import bg.sofia.uni.fmi.mjt.cocktail.server.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.cocktail.server.command.CommandExecutor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    private final int port;
    private ByteBuffer buffer;
    private Selector selector;
    private boolean isServerUp;

    private final CommandExecutor commandExecutor;

    public Server(int port, CommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            this.selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerUp = true;

            while (isServerUp) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String clientInput = getClientInput(clientChannel);
                            System.out.println("Client wrote: " + clientInput);

                            String serverReply = commandExecutor.execute(CommandCreator.createCommand(clientInput));

                            System.out.println("Ok we're ready with serverReply!");
                            if (serverReply == null) {
                                writeClientOutput(clientChannel, "Unknown command");
                            } else if (serverReply.equals("disconnect")) {
                                writeClientOutput(clientChannel, "Disconnected from the server");
                                clientChannel.close();
                                clientChannel.keyFor(selector).cancel();
                            } else {
                                writeClientOutput(clientChannel, serverReply);
                            }

                            System.out.println("Exiting!!");
                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                            System.out.println("We've accepted it!");
                        }

                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    public void stop() {
        isServerUp = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel serverSocketChannel,
                                              Selector selector) throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String serverReply) throws IOException {
        buffer.clear();
        buffer.put(serverReply.getBytes(StandardCharsets.UTF_8));
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = serverChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}
