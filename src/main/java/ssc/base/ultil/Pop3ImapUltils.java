/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ssc.base.ultil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author ASUS
 */
public class Pop3ImapUltils {

     public static String getOTPFromApi(String username, String passwd,String from) {

        List<String> arr = Pop3ImapUltils.getContent(username, passwd, from);
        if (arr.size() == 0) {
            return "";
        }
        for (String string : arr) {
            System.out.println(string);
        }

        return arr.get(arr.size() - 1).replaceAll("\\D+", "");
    }
    
    
    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws IOException, MessagingException {

        int count = mimeMultipart.getCount();
        if (count == 0) {
            throw new MessagingException("Multipart with no body parts not supported.");
        }
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt) // alternatives appear in an order of increasing 
        // faithfulness to the original content. Customize as req'd.
        {
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        }
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }

    private static String getTextFromBodyPart(
            BodyPart bodyPart) throws IOException, MessagingException {

        String result = "";
        if (bodyPart.isMimeType("text/plain")) {
            result = (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            String html = (String) bodyPart.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart) {
            result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        return result;
    }

    private static String getTextFromMessage(Message message) throws IOException, MessagingException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    public static List<String> getContent(String username, String password, String fromEmail) {
        List<String> arr=new ArrayList<>();
        Folder inbox = null;
        Store storeE = null;

        //System.setProperty("javax.net.debug", "ssl");
        //System.setProperty("https.protocols", "TLSv1 TLSv1.1 TLSv1.2 TLSv1.3");

        try {
            // Set up JavaMail properties
            Properties properties = new Properties();

            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3.host", "outlook.office365.com"); // Outlook POP3 server
            properties.put("mail.pop3.port", "995"); // POP3S port
            properties.put("mail.pop3.ssl.enable", "true");
            properties.put("mail.pop3.ssl.protocols", "TLSv1.2");
            String content = "";
            try {

                Session session = Session.getDefaultInstance(properties);
                session.setDebug(true);
                // Connect to the IMAP server
                storeE = session.getStore("pop3");

                storeE.connect(username, password);

                // Open the INBOX folder
                inbox = storeE.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                // Retrieve messages
                Message[] messages = inbox.getMessages();

                // Print message details
                for (Message message : messages) {
                    if (message.getFrom()[0].toString().contains(fromEmail)) {
                        arr.add(message.getSubject());
                     }
                }

                // Close the folder and store when done
                inbox.close(false);
                storeE.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    inbox.close(false);
                    storeE.close();
                } catch (Exception e) {
                }

            }
            return arr;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }
}
