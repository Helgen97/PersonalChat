import java.util.Scanner;

/**
 * Отсюда запускаеться чат.
 * Чат создан на основе статьи
 * @source www.quizful.net/post/base_network_in_java
 * @version 1.0
 * @versionDescription базовая основа со статьи
 * @author Helgen97
 */
public class ChatStart {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome!");
        System.out.println("Choose start option: ");
        System.out.println("1.(S)erver");
        System.out.println("2.(C)lient");
        System.out.println("3.(E)xit");
        while (true){
            String choice = scanner.nextLine();
            if(choice.charAt(0) == 'S'){
                new Server();
                break;
            }else if(choice.charAt(0) == 'C'){
                new Client();
                break;
            }else if(choice.charAt(0) == 'E'){
                System.exit(0);
            }else  {
                System.out.println("Wrong input! Try again!");
            }
        }
    }
}
