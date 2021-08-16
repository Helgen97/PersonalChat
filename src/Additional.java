import java.io.FileWriter;
import java.io.IOException;

public class Additional {
    private FileWriter writer;

    Additional(){
        try {
            writer = new FileWriter("MessageHistory.txt", true);
        }catch (IOException ex){
            System.out.println("FileWriter went wrong...");
        }
    }
    public void WriteToFile(String message){
        try{
            writer.write(message + "\n");
        }catch (IOException ex){
            System.out.println("Saving message history went wrong");
        }
    }

    public void close(){
        try {
            writer.close();
        }catch (IOException ex){
            System.out.println("Additional/ method close went wrong");
        }
    }
}
