import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Класс описывает пользователя.
 */
public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    private JFrame window;
    private JPanel panel;
    private JLabel text1;
    private JTextArea messageArea;
    private JLabel nameWindow;
    private JTextField textArea;
    private JButton sendButton;

    private String name = "";
    private String message = "";
    private String IP = "";

    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * Метод запуска клиенсткой части. Вынесено в отдельный метод,
     * чтобы изначально была возможность запустить клиенсткую часть
     * после получения IP адреса
     */
    public void clientStart() {
        clientGui();
        try {
            socket = new Socket(IP, Properties.port);
        } catch (IOException ex) {
            System.out.println("Connection error. Wrong address!");
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            ServerMessages serverMessages = new ServerMessages();
            serverMessages.start();

            while (true) {
                if (message.equalsIgnoreCase("exit")) {
                    serverMessages.setStop();
                    break;
                }
            }

        } catch (Exception ex) {
            System.out.println("Sending message error");
        } finally {
            close();
        }
    }

    /**
     * Метод закрытия всех открытых потоков
     */
    private void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            System.out.println("Closing error");
        }
    }

    /**
     * Метод создает интерфейс клиентской части
     */
    public void clientGui() {
        window = new JFrame("Client");
        window.setSize(500, 400);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        window.add(panel);

        text1 = new JLabel("Number of users: ");
        panel.add(text1);
        text1.setBounds(10, 0, 500, 25);
        text1.setVisible(true);

        messageArea = new JTextArea();
        messageArea.setBounds(10, 25, 480, 310);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setText("Hello! Enter your nickname...\n");


        JScrollPane scrollPane = new JScrollPane(messageArea);
        panel.add(scrollPane);
        scrollPane.setBounds(10, 25, 480, 310);
        scrollPane.setAutoscrolls(true);
        scrollPane.setVisible(true);

        nameWindow = new JLabel("Name");
        panel.add(nameWindow);
        nameWindow.setBounds(10, 335, 50, 30);
        nameWindow.setVisible(true);

        textArea = new JTextField("Enter a message...");
        panel.add(textArea);
        textArea.setBounds(60, 335, 380, 30);
        textArea.setVisible(true);
        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textArea.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) textArea.setText("Enter a message...");
            }
        });

        sendButton = new JButton("Send");
        panel.add(sendButton);
        sendButton.setBounds(435, 335, 65, 32);
        sendButton.setVisible(true);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText("Please enter a message...");
                } else if (name.equals("")) {
                    String message = textArea.getText();
                    name = message;
                    out.println(message);
                    nameWindow.setText(message);
                    messageArea.setText("");
                    textArea.setText("");
                } else {
                    String message = textArea.getText();
                    out.println(message);
                    textArea.setText("");
                }
            }
        });

        panel.setSize(500, 400);
        panel.setLayout(null);
        panel.setVisible(true);
        window.getRootPane().setDefaultButton(sendButton);
        window.setVisible(true);
    }

    /**
     * Внутренний класс, который отвечает за получения сообщений от сервера
     * и выводих их на экран
     * Работает на основе многопоточности, и запускает отдельный поток, который и занят принятием сообщений
     */
    private class ServerMessages extends Thread {
        private boolean isStop;

        public void setStop() {
            isStop = true;
        }

        @Override
        public void run() {
            try {
                while (!isStop) {
                    String serverMessage = in.readLine();
                    messageArea.append(serverMessage);
                    messageArea.append("\n");
                }
            } catch (IOException ex) {
                System.out.println("Receiving message error!");
            }
        }
    }

}
