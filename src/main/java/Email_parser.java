import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;

import java.io.IOException;
import java.util.Vector;

import static java.lang.System.out;

public class Email_parser {

    public static void main(String[] args) {
        String pstFile = "C:\\Users\\UserOne\\Desktop\\outlook.pst"; //set pstFile into args[1]
        String o = parseEmail(pstFile);
        out.println(o);
    }

    public static String parseEmail(String fileName) {
        try {
            PSTFile pstFile = new PSTFile(fileName);
            out.println(pstFile.getMessageStore().getDisplayName());
            processFolder(pstFile.getRootFolder());
        } catch (Exception err) {
            err.printStackTrace();
        }
        return fileName;
    }

    public static int depth = -1;

    public static void processFolder(PSTFolder folder)
            throws PSTException, IOException
    {
        depth++;
        // the root folder doesn't have a display name
        if (depth > 0) {
            //printDepth();
            out.println(folder.getDisplayName());
        }

        // go through the folders...
        if (folder.hasSubfolders()) {
            Vector<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processFolder(childFolder);
            }
        }

        // and now the emails for this folder
        if (folder.getContentCount() > 0) {

            depth++;
            PSTMessage email = null;
            try {
                email = (PSTMessage)folder.getNextChild();
            } catch (PSTException e) {
                e.printStackTrace();
            }
            int emailNumber = 0;
            while (email != null) {
                emailNumber++;
                printDepth();
                out.println("\n****NEXT EMAIL******"+emailNumber);
                out.println("Email Sender Name: " + email.getSenderName() +"["+ email.getSenderEmailAddress()+"]");
                out.println("Email Received Name: " + email.getReceivedByName() +"["+  email.getReceivedByAddress()+"]");
                out.println("Email Subject: " + email.getSubject());
                out.println("Email Time: " + email.getMessageDeliveryTime());
                //out.println("Email has attachment?: " + email.hasAttachments());
                out.println("Email has Replied: " + email.hasReplied());
                if(email.hasReplied()){
                    out.println("\n****EMAIL BODY******");
                    out.println(email.getBody());
                    getRepliedEmails(email.getBody());
                }
                if(email.hasForwarded()){
                    getForwardedEmails(email.getBody());
                }
                email = (PSTMessage)folder.getNextChild();
            }
            depth--;
        }
        depth--;
    }
    public static String getRepliedEmails(String body){
        return body;
    }

    public static String getForwardedEmails(String body){
        return body;
    }

    public static void printDepth() {
        for (int x = 0; x < depth-1; x++) {
            out.print(" | ");
        }
        out.print(" |- ");
    }
}
