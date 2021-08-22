import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
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

    private JFrame window;
    private JPanel panel;
    private JTextArea textArea;
    private JLabel text;
    private JLabel text1;

    public Server() {
        try {
            server = new ServerSocket(Properties.port);
            serverGui();
            while (true) {
                Socket socket = server.accept();
                Connection connection = new Connection(socket);
                connections.add(connection);
                connection.start();
                textArea.append("User " + socket.getInetAddress() + " connected");
                textArea.append("\n");
            }

        } catch (IOException ex) {
            System.out.println("Creating server error");
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

    /**
     * Метод создания интерфейса серверной части
     */
    private void serverGui() {
        window = new JFrame("Server");
        window.setSize(350, 250);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        window.add(panel);

        text = new JLabel("Server address: " + server.getInetAddress() + ":" + server.getLocalPort());
        panel.add(text);
        text.setBounds(10, 0, 350, 30);

        text1 = new JLabel("Server log:");
        panel.add(text1);
        text1.setBounds(10, 15, 350, 30);

        textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        scrollPane.setBounds(10, 40, 330, 170);
        scrollPane.setVisible(true);
        scrollPane.setAutoscrolls(true);

        panel.setSize(350, 250);
        panel.setLayout(null);
        panel.setVisible(true);
        window.setVisible(true);
    }

    /**
     * Класс отвечает за потоковую передачу полученых сообещений всем участникам чата
     */
    private class Connection extends Thread {
        private BufferedReader in;
        private PrintWriter out;
        private final Socket socket;

        public Connection(Socket socket) {
            this.socket = socket;


            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);


            } catch (IOException ex) {
                System.out.println("Creating reader and writer error");
            }
        }

        @Override
        public void run() {
            try {
                String name = in.readLine();

                Additional additional = new Additional();
                additional.WriteToFile(name + " connected");

                synchronized (connections) {
                    for (Connection connection : connections) {
                        connection.out.println(name + " connected");
                    }
                }

                String message;
                while (true) {
                    message = in.readLine();
                    additional.WriteToFile(name + ": " + message);
                    if (message.equalsIgnoreCase("exit")) break;

                    synchronized (connections) {
                        for (Connection connection : connections) {
                            connection.out.println(name + ": " + message);
                        }
                    }
                }

                additional.WriteToFile(name + " has left");
                textArea.append("User " + socket.getInetAddress() + " disconnect");
                textArea.append("\n");
                synchronized (connections) {
                    for (Connection connection : connections) {
                        connection.out.println(name + " has left");
                    }
                }
                additional.close();
            } catch (IOException ex) {
                System.out.println("Receiving or sending message error");
            } finally {
                close();
            }
        }

        /**
         * метод закрытия соединения
         */
        public void close() {
            try {
                in.close();
                out.close();
                socket.close();
                connections.remove(this);
            } catch (IOException ex) {
                System.out.println("Closing error");
            }
        }
    }

    /**
     * Метод закрывает соединения участников и сам сервер
     */
    private void closeAll() {
        try {
            server.close();

            synchronized (connections) {
                for (Connection connection : connections) {
                    connection.close();
                }
            }
        } catch (IOException ex) {
            System.out.println("Server closing error");
        }
    }
}
