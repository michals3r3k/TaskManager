package pl.coderslab;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskManager {

    private final static String fileName="tasks.csv";
    private static ArrayList<String> lines=new ArrayList<>();




    public static void main(String[] args) {
        taskMenu();
    }




    public static void showOptions(){
        System.out.println(ConsoleColors.BLUE+"\nPlease select an option:"+ConsoleColors.RESET);
        System.out.println("add");
        System.out.println("remove");
        System.out.println("list");
        System.out.println("exit");
    }


    public static void taskMenu(){
        boolean loop=true;

        while(loop) {
            initList();
            showOptions();
            String option = writeOption();
            switch (option) {
                case "add":
                    taskAdd();
                    break;
                case "remove":
                    taskRemove();
                    break;
                case "list":
                    taskList();
                    break;
                case "exit":
                    taskExit();
                    loop=false;
                    break;
            }
        }
    }


    public static String writeOption() {
        Scanner in=new Scanner(System.in);
        String option;
        String result="";
        boolean checked=false;

        while(!checked){
            option=in.nextLine();
            option=option.toLowerCase();

            switch(option){
                case "add":
                case "remove":
                case "list":
                case "exit":
                    result=option;
                    checked=true;
                    break;
                default:
                    System.out.println(ConsoleColors.RED+"Please pick the correct option:"+ConsoleColors.RESET);
                    break;
            }
        }
        return result;
    }

    public static void initList(){
        File file= new File(fileName);
        lines=new ArrayList<>();
        String line;
        try(Scanner in=new Scanner(file)){
            while(in.hasNextLine()){
                line=in.nextLine();
                lines.add(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static String input(String text){
        Scanner in=new Scanner(System.in);
        String result;
        System.out.println(text);
        result=in.nextLine();
        return result;
    }


    public static void taskAdd(){
        StringBuilder sb=new StringBuilder();
        String describe, date, important;

        describe=input(ConsoleColors.BLUE+"Add task describtion:"+ConsoleColors.RESET);
        date=input(ConsoleColors.BLUE+"Add date to task"+ConsoleColors.RESET);

        while(!date.matches("^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$")){
            date=input(ConsoleColors.RED+"Please add correct date"+ConsoleColors.RESET);
        }
        important=input(ConsoleColors.BLUE+"Is your task important?"+ConsoleColors.RESET);
        while(!important.matches("^(true)|(false)$")){
            important=input(ConsoleColors.RED+"Type 'true' or 'false'"+ConsoleColors.RESET);
        }

        sb.append("\n").append(describe).append(", ").append(date).append(", ").append(important);

        try(FileWriter fw=new FileWriter(fileName, true)){
            fw.append(sb);
            System.out.println(ConsoleColors.GREEN+"Task added correctly:"+sb+ConsoleColors.RESET);
        }catch (IOException e){
            System.out.println(ConsoleColors.RED+"Task adding failure:"+sb+ConsoleColors.RESET);
            e.printStackTrace();
        }
    }


    public static void taskRemove(){
        Path path= Paths.get(fileName);
        Scanner in=new Scanner(System.in);

        System.out.println(ConsoleColors.BLUE+"List to remove:"+ConsoleColors.RESET);
        taskList();


        System.out.println(ConsoleColors.BLUE+"\nPlease select a number to remove:"+ConsoleColors.RESET);
        while(!in.hasNextInt()){
            System.out.println(ConsoleColors.RED+"Please select a number"+ConsoleColors.RESET);
            in.next();
        }
        int index=in.nextInt();


        try{
            lines.remove(index);
            System.out.println(ConsoleColors.GREEN+"Task removed succesfully"+ConsoleColors.RESET);
        }catch (IndexOutOfBoundsException e){
            System.out.println(ConsoleColors.RED+"Incorrect index"+ConsoleColors.RESET);
        }


        try{
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void taskList(){
        String[] list=lines.toArray(new String[0]);
        for(int i=0; i<list.length; i++){
            System.out.printf("%s: %s,\n", i, list[i]);
        }
    }


    public static boolean taskExit(){
        System.out.println(ConsoleColors.RED+"Bye Bye"+ConsoleColors.RESET);
        return true;
    }
}
