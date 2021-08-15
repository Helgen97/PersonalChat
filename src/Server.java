import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс сервера, содержит список соединений которые подключены к серверу
 * Класс Connections, который отвечает за пересылку сообещний всем участникам чата
 * Методый закрытия всех соединений
 */
public class Server {
    private List<Connection> connections =
            Collections.synchronizedList(new ArrayList<>());
    private ServerSocket server;


    public Server(){
        try {
            server = new ServerSocket(Properties.port);
            System.out.println("Server address: " + InetAddress.getLocalHost().toString());

            while (server.isClosed()){
            Socket socket = server.accept();

            Connection connection = new Connection(socket);
            connections.add(connection);
            connection.start();
            }

        }catch (IOException ex){
            System.out.println("Creating port error");
        }finally {
            closeAll();
        }
    }

    /**
     * Класс отвечает за потоковую передачу полученых сообещений всем участникам чата
     */
    private class Connection extends Thread{
        private BufferedReader in;
        private PrintWriter out;
        private final Socket socket;

        public Connection(Socket socket){
            this.socket = socket;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

            }catch (IOException ex){
                System.out.println("Something went wrong!");
            }
        }

        @Override
        public void run(){
            try {
                String name = in.readLine();

                synchronized (connections){
                    for (Connection connection : connections) {
                        connection.out.println(name + " connected");
                    }
                }

                String message;
                while (true) {
                    message = in.readLine();
                    if (!message.equalsIgnoreCase("exit")) break;
                    synchronized (connections) {
                        for (Connection connection : connections) {
                            connection.out.println(name + ": " + message);
                        }
                    }
                }

                synchronized (connections){
                    synchronized (connections){
                        for (Connection connection : connections) {
                            connection.out.println(name + " has left");
                        }
                    }
                }

            }catch (IOException ex){
                System.out.println("Something went wrong!");
            }finally {
                close();
            }
        }

        /**
         * метод закрытия соединения
         */
        public void close(){
            try{
                in.close();
                out.close();
                socket.close();
                connections.remove(this);
            }catch (IOException ex){
                System.out.println("Something went wrong!");
            }
        }
    }

    /**
     * Метод закрывает соединения участников и сам сервер
     */
    private void closeAll(){
        try {
            server.close();

            synchronized (connections){
                for (Connection connection : connections) {
                    connection.close();
                }
            }
        }catch (IOException ex){
            System.out.println("Something went wrong!");
        }
    }
}
