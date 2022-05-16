
import java.io.*;
import java.util.*;
import java.util.Scanner;

import static java.lang.System.*;

public class Forensics_Tool {
    public static void main(String[] args) throws InterruptedException, IOException {
        String runCommand;
        switch (args[0]) {
            case "-i" -> {
                runCommand = "exiftool.exe ";
                out.println(getCommand(runCommand + args[1]));
            }
            case "-d" -> {
                runCommand = String.format("RECmd.exe -f \"%s\"\\SYSTEM.DAT --kn \"ControlSet001\\Control\\ComputerName\\ComputerName\" --nl", args[1]);
                String getSystem_InfoName = getCommand(runCommand);
                out.println("Computer Name:\n" + getSystem_InfoName);

                String runCommand2 = String.format("RECmd.exe -f \"%s\"\\NTUSER.DAT --kn \"Software\\Microsoft\\Windows NT\\CurrentVersion\\Time Zones\" --nl", args[1]);
                String getSystem_InfoTime = getCommand(runCommand2);
                out.println("Time zone:\n" + getSystem_InfoTime);
            }
            case "-r" -> {
                runCommand = String.format("RECmd.exe -f \"%s\"\\NTUSER.DAT --kn \"Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\ComDlg32\\OpenSaveMRU\\*\" --nl --sa --bn", args[1]);
                out.println(getCommand(runCommand));
            }
            case "-u" -> {
                runCommand = String.format("RECmd.exe -f \"%s\"\\NTUSER.DAT --kn \"Software\\Microsoft\\Internet Explorer\\TypedURLs\" --nl --sa --bn", args[1]);
                out.println(getData(getCommand(runCommand), "Data:"));
            }
            case "-e" -> out.println("run Email_parser");
            //Email_parser.parseEmail(args[1]);//throws errors
                
            case "-h" -> printError();
                
            default -> {
                out.println("Argument not found: -help for more information");
                printError();
            }
        }
    }//end main

    public static void printError(){
        out.println("\nNote:\n");
        out.println("Usage: Forensics_Tool [options] [registry path]\n");
        out.println("Example: java Forensics_Tool -d C:\\Users\\UserOne\\Desktop\\My_Registries");
        out.println("Options:");
        out.println("-i     Display the info of the image (include the file name and extension. eg: hello.txt)");
        out.println("-d     Display the info of the system");
        out.println("-r     Retrieve recent open files and recent access documents");
        out.println("-u     Retrieve typed URLS");
        out.println("-e     parse email");
    }
    public static String getData(String line, String word) {
        String[] arrayToSplit = line.split("\s");
        String[] values = new String[arrayToSplit.length];

        String getData = "";
        for(int i = 0; i < arrayToSplit.length; i++){
            if(arrayToSplit[i].contains(word)){
                getData = arrayToSplit[i+1];
            }
             values[i] = getData;
        }
        //remove duplicates
        values = Arrays.stream(values).distinct().toArray(String[]::new);

        return String.join("\n ",values);
    }

    /*
     *executes a windows command
     */
    public static String getCommand(String cmd) throws IOException {
        Scanner s = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }//end getCommand method

}
