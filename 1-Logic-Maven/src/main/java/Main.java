import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;


public class Main {
    public static void main(String[] args) throws IOException {
        CommandHandler IEMDB_command_handler = new CommandHandler();
        while(true) {
            BufferedReader commandLineReader = new BufferedReader(new InputStreamReader(System.in));
            String command = commandLineReader.readLine();

            if (command.equals("exit"))
                break;

            String response = IEMDB_command_handler.processCommand(command);
            System.out.println(response);
        }
    }
}