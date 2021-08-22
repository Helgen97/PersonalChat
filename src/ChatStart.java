import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс отвечает за начало работы с чатом
 * Присутствует выбор как начать работать с чатом,
 * в виде сервера или пользователя
 */
public class ChatStart {
    private JFrame window;
    private JPanel panel;
    private JLabel text1;
    private JLabel text2;
    private JButton button1;
    private JButton button2;

    public ChatStart() {
        window = new JFrame("Chat");
        window.setSize(300, 200);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        window.add(panel);

        text1 = new JLabel("Welcome!");
        panel.add(text1);
        text1.setBounds(115, 25, 150, 50);

        text2 = new JLabel("Choose option you need.");
        panel.add(text2, BorderLayout.SOUTH);
        text2.setBounds(70, 50, 200, 50);

        button1 = new JButton("Server");
        panel.add(button1);
        button1.setBounds(0, 120, 150, 50);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Server server = new Server();
                    }
                });
                thread.start();

            }
        });

        button2 = new JButton("Client");
        panel.add(button2);
        button2.setBounds(150, 120, 150, 50);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
                new IPGui();
            }
        });

        panel.setSize(300, 200);
        panel.setLayout(null);
        panel.setVisible(true);
        window.setVisible(true);
    }
}

