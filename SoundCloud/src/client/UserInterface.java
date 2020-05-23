/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author anacesár
 * @author coutinho
 */
public class UserInterface {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void waitEnter() {
        System.out.println("Press enter to continue");
        scanner.nextLine();
    }
    
    public static String showWelcomeMenu() {
        List<String> options = new ArrayList<>();
        options.add("1 - Login");
        options.add("2 - Register");
        options.add("3 - Exit");
        Menu welcomeMenu = new Menu(options, "Welcome Menu  ");
        welcomeMenu.show();
        String selectedOption = scanner.nextLine();
        String res = null;
        switch (selectedOption) {
            case "1":
                res = "login";
                break;
            case "2":
                res = "register";
                break;
            case "3":
                res = "exit";
                break;
            default:
                System.out.println("Please select one of the available options");
                waitEnter();
                res = showWelcomeMenu();
        }
        return res;
    }
    
    public static List<String> showRegisterMenu() {
        List<String> answers = new ArrayList<>();
        System.out.println("Insert an username");
        answers.add(scanner.nextLine());
        System.out.println("Insert a password ");
        answers.add(scanner.nextLine());

        return answers;
    }
    
    public static List<String> showLoginMenu() {
        List<String> answers = new ArrayList<>();
        System.out.println("Insert an username ");
        answers.add(scanner.nextLine());
        System.out.println("Insert a password ");
        answers.add(scanner.nextLine());

        return answers;
    }
    
    public static String showSoundCloudMenu() {
        List<String> options = new ArrayList<>();
        options.add("1 - Show Available Music");
        options.add("2 - Search Music");
        options.add("3 - Upload Music");
        options.add("5 - Download Music");
        options.add("6 - Go Back");
        Menu soundCloud = new Menu(options, "SoundCloud    ");
        soundCloud.show();
        String selectedOption = scanner.nextLine();
        String res = null;
        switch (selectedOption) {
            case "1":
                res = "music";
                break;
            case "2":
                res = "search";
                break;
            case "3":
                res = "uploadM";
                break;
            case "5":
                res = "download";
                break;
            case "6":
                res = "goBack";
                break;
            default:
                System.out.println("Please select one of the available options");
                waitEnter();
                res = showSoundCloudMenu();
        }
        return res;
    }
    
    public static String showUploadAMusic() {
        System.out.println("Insert the path where the music is");
        String path = scanner.nextLine();

        return path;
    }
    
    public static List<String> showUploadMMusic() {
        List<String> answers = new ArrayList<>();
        System.out.println("Insert the path where the music is");
        answers.add(scanner.nextLine());
        System.out.println("Insert the title");
        answers.add(scanner.nextLine());
        System.out.println("Insert the artist");
        answers.add(scanner.nextLine());
        System.out.println("Insert the year of release");
        answers.add(scanner.nextLine());
        System.out.println("Insert the number of genres");
        int numberGenres = scanner.nextInt();
        answers.add(String.valueOf(numberGenres));
        scanner.nextLine();
        
        for(int i = 0; i < numberGenres; i++){
            answers.add(scanner.nextLine());
        }
            
        return answers;
    }
    
    public static int showDownloadMusic() {
        System.out.println("Insert the identifier of the music");
        int id = scanner.nextInt();
        scanner.nextLine();

        return id;
    }
    
    public static String showMusicMenu() {
        List<String> options = new ArrayList<>();
        options.add("1 - Refresh");
        options.add("2 - Go Back");
        Menu music = new Menu(options, "Music Menu    ");
        music.show();
        String selectedOption = scanner.nextLine();
        String res = null;
        switch (selectedOption) {
            case "1":
                res = "refresh";
                
                break;
            case "2":
                res = "goBack";
                break;
            default:
                System.out.println("Please select one of the available options");
                waitEnter();
                res = showMusicMenu();
        }
        return res;
    }
    
    public static String showSearchMenu() {
        System.out.println("Insert the tag of the music");
        String tag = scanner.nextLine();

        return tag;
    }
     
}
