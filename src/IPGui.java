import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Класс отвечает за окно ввода IP адреса сервера, к которому нужно подключиться
 * после чего, подключаеться к серверу и открывает окно клиентской части
 */
public class IPGui {

    public IPGui() {
        JFrame window = new JFrame("Server Address");
        window.setSize(300, 150);
        window.setLocationRelativeTo(null);
        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        window.add(panel);

        JLabel text = new JLabel("Enter the IP address");
        panel.add(text);
        text.setBounds(85, 10, 150, 10);

        JLabel text1 = new JLabel("The IP format: XXX.XXX.XXX.XXX");
        panel.add(text1);
        text1.setBounds(40, 30, 200, 10);

        JTextField textField = new JTextField();
        panel.add(textField);
        textField.setBounds(10, 50, 280, 30);
        textField.setText("Enter the address...");
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText("Enter the address...");
                }
            }
        });

        JButton button = new JButton("Enter");
        panel.add(button);
        button.setBounds(100, 80, 100, 30);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText("Please enter a address...");
                } else if (!textField.getText().equals("Please enter a address...") && !textField.getText().equals("Enter the address...")) {
                    String IP = textField.getText();
                    window.dispose();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Client client = new Client();
                            client.setIP(IP);
                            client.clientStart();
                        }
                    });
                    thread.start();
                }
            }
        });

        panel.setSize(300, 150);
        panel.setLayout(null);
        panel.setVisible(true);
        window.getRootPane().setDefaultButton(button);
        window.setVisible(true);
    }
}
