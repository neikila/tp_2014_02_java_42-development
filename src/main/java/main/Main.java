package main;

public class Main {

    // Действия необходимые для тестирования
    // Вызывается в начале запуска сервера
    private static void preparationForTest(AccountService accountService) {
        // Создание в базе пользователя по дефолту. Имитация бд ввиду её отстсвия.
        String login = "admin";
        String password = "admin";
        String server = "10";
        String email = "admin@gmail.com";
        UserProfile profile = new UserProfile(login, password, email, server);
        profile.setAdmin(true);
        accountService.addUser(login,  profile);

        login = "test";
        password = "test";
        server = "10";
        email = "test@gmail.com";
        profile = new UserProfile(login, password, email, server);
        profile.setAdmin(true);
        accountService.addUser(login,  profile);
    }

    public static void main(String[] args) throws Exception {
        int port = 80;
        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        } else {
            System.out.append("Порт не задан.");
        }

        AccountService accountService = new AccountService();
        preparationForTest(accountService);

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        AppServer server = new AppServer(port, accountService);
        server.start();
    }
}