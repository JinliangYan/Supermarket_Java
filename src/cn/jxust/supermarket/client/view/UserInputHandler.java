package cn.jxust.supermarket.client.view;

import java.util.Scanner;

public interface UserInputHandler {
    String PROMPT = "> ";
    void display();
    default String getUserInput(String leader, Scanner scanner) {
        System.out.print(leader + PROMPT);
        return scanner.nextLine();
    }

    default int getUserIntInput(String leader, Scanner scanner) {
        System.out.print(leader + PROMPT);
        String userInputId = scanner.nextLine();
        try {
            return Integer.parseInt(userInputId);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    default double getUserDoubleInput(String leader, Scanner scanner) {
        System.out.print(leader + PROMPT);
        String userInput = scanner.nextLine();
        try {
            return Double.parseDouble(userInput);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    default void showMessage(String message) {
        System.out.println(message);
    }
    default void showError(String error) {
        System.out.println("错误：" + error);
    }
    void handleUserInput();
}
