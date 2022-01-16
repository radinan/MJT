package bg.sofia.uni.fmi.mjt.cocktail.server.command;

public class CommandCreator {
    public static Command createCommand(String input) {
        if (input == null) {
            return null;
        }

        String[] elements = input.split(" ", 2);
        if (elements.length < 2) {
            return new Command(elements[0], null);
        }
        return new Command(elements[0], elements[1]);
    }
}
