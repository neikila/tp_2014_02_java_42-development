package main;

public class Main {

    public static void main(String[] args) throws Exception {
        int port = 80;
        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        } else {
            System.out.append("Порт не задан.");
        }

        AccountService accountService = new AccountService();
        accountService.preparationForTest();

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        AppServer server = new AppServer(port, accountService);
        server.start();
    }
}