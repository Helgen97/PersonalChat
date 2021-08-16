import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Класс описывает пользователя.
 *
 */
public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private final Scanner scan;


    Client(){
        scan = new Scanner(System.in);

        System.out.println("Hi! Please enter the IP of the server. Thanks!");
        System.out.println("Type format: XXX.XXX.XXX.XXX");

        String IP = scan.nextLine();

        try {
        socket = new Socket(IP, Properties.port);
        } catch (IOException ex){
            System.out.println("Connection error. Wrong address!");
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            System.out.println("Enter your Nickname:");
            String nick = scan.nextLine();
            out.println(nick);

            ServerMessages serverMessages = new ServerMessages();
            serverMessages.start();

            String message = "";
            while (!message.equalsIgnoreCase("exit")){
                message = scan.nextLine();
                out.println(message);
            }

            serverMessages.setStop();

        }catch (Exception ex){
            System.out.println("Sending message error");
        }finally {
            close();
        }


    }

    /**
     * Метод закрытия всех открытых потоков
     */
    private void close(){
        try {
            in.close();
            out.close();
            socket.close();
            scan.close();
        }catch (Exception ex){
            System.out.println("Closing error");
        }
    }

    /**
     * Внутренний класс, который отвечает за получения сообщений от сервера
     * и выводих их на экран
     * Работает на основе многопоточности, и запускает отдельный поток, который и занят принятием сообщений
     */
    private class ServerMessages extends Thread{
        private boolean isStop;

        public void setStop(){
            isStop = true;
        }

        @Override
        public void run() {
            try {
                while (!isStop){
                    String serverMessage = in.readLine();
                    System.out.println(serverMessage);
                }
            }catch (IOException ex){
                System.out.println("Receiving message error!");
            }
        }
    }
}
