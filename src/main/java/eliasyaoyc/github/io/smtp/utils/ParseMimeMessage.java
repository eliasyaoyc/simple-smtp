package eliasyaoyc.github.io.smtp.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * {@link ParseMimeMessage} The Utility class for MimeMessage.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/5
 */
public class ParseMimeMessage {
    private MimeMessage mimeMessage = null;
    /** attach store dir */
    private String saveAttachPath = "";
    /** the stringBuffer object which used to stored the context of email */
    private StringBuffer bodyText = new StringBuffer();
    /** default date format */
    private String dateFormat = "yy-MM-dd HH:mm";

    public ParseMimeMessage() {
    }

    public ParseMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
        System.out.println("create a ParseMimeMessage object........");
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    /**
     * Return the address and name of Sender.
     *
     * @return
     * @throws Exception
     */
    public String getFrom() throws Exception {
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
        String from = address[0].getAddress();
        if (from == null) from = "";
        String personal = address[0].getPersonal();
        if (personal == null) personal = "";
        String fromaddr = personal + "<" + from + ">";
        return fromaddr;
    }

    /**
     * Return the  to 、cc 、 bcc of email.
     *
     * @param type
     * @return
     * @throws Exception
     */
    public String getMailAddress(String type) throws Exception {
        String mailaddr = "";
        String addtype = type.toUpperCase();
        InternetAddress[] address = null;
        if (addtype.equals("TO") || addtype.equals("CC") || addtype.equals("BCC")) {
            if (addtype.equals("TO")) {
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
            } else if (addtype.equals("CC")) {
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
            } else {
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
            }
            if (address != null) {
                for (int i = 0; i < address.length; i++) {
                    String email = address[i].getAddress();
                    if (email == null) {
                        email = "";
                    } else {
                        email = MimeUtility.decodeText(email);
                    }
                    String personal = address[i].getPersonal();
                    if (personal == null)
                        personal = "";
                    else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    String compositeto = personal + "<" + email + ">";
                    mailaddr += "," + compositeto;
                }
                mailaddr = mailaddr.substring(1);
            } else {
                throw new Exception("Error emailaddr type!");
            }
        }
        return mailaddr;
    }

    /**
     * Return the subject of email.
     *
     * @return
     * @throws MessagingException
     */
    public String getSubject() throws MessagingException {
        String subject = "";
        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
            if (subject == null) subject = "";
        } catch (Exception exce) {
        }
        return subject;
    }

    /**
     * Return the date message was sent.
     *
     * @return
     * @throws Exception
     */
    public String getSentDate() throws Exception {
        Date sentdate = mimeMessage.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(sentdate);
    }

    /**
     * Return the body of the email.
     *
     * @return
     */
    public String getBodyText() {
        return bodyText.toString();
    }

    /**
     * Parse email according to different MimeType types and stores internal training in a StringBuffer object{@link StringBuffer}.
     *
     * @param part
     * @throws Exception
     */
    public void getMailContent(Part part) throws Exception {
        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if (nameindex != -1) conname = true;
        System.out.println("CONTENTTYPE: " + contenttype);
        if (part.isMimeType("text/plain") && !conname) {
            bodyText.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conname) {
            bodyText.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContent(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent());
        } else {
        }
    }

    /**
     * true if the email required a return receipt or false if it dose not.
     *
     * @return
     * @throws MessagingException
     */
    public boolean getReplySign() throws MessagingException {
        boolean replysign = false;
        String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");
        if (needreply != null) {
            replysign = true;
        }
        return replysign;
    }

    /**
     * Return the message-id of the email.
     *
     * @return
     * @throws MessagingException
     */
    public String getMessageId() throws MessagingException {
        return mimeMessage.getMessageID();
    }

    /**
     * false if the email has been read, true if the email not read.
     *
     * @return
     * @throws MessagingException
     */
    public boolean isNew() throws MessagingException {
        boolean isnew = false;
        Flags flags = ((Message) mimeMessage).getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        System.out.println("flagss length: " + flag.length);
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                System.out.println("seen Message.......");
                break;
            }

        }
        return isnew;
    }

    public static void main(String args[]) throws Exception {
        String host = "主机名/ip"; //【pop.mail.yahoo.com.cn】
        String username = "用户名"; //【wwp_1124】
        String password = "密码"; //【........】
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("pop3");
        store.connect(host, username, password);
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        Message message[] = folder.getMessages();
        System.out.println("Messagess length: " + message.length);
        ParseMimeMessage pmm = null;
        for (int i = 0; i < message.length; i++) {
            pmm = new ParseMimeMessage((MimeMessage) message[i]);
            System.out.println("Message " + i + " subject: " + pmm.getSubject());
            System.out.println("Message " + i + " sentdate: " + pmm.getSentDate());
            System.out.println("Message " + i + " replysign: " + pmm.getReplySign());
            System.out.println("Message " + i + " hasRead: " + pmm.isNew());
            System.out.println("Message " + i + " form: " + pmm.getFrom());
            System.out.println("Message " + i + " to: " + pmm.getMailAddress("to"));
            System.out.println("Message " + i + " cc: " + pmm.getMailAddress("cc"));
            System.out.println("Message " + i + " bcc: " + pmm.getMailAddress("bcc"));
            System.out.println("Message " + i + " sentdate: " + pmm.getSentDate());
            System.out.println("Message " + i + " Message-ID: " + pmm.getMessageId());
            pmm.getMailContent((Part) message[i]);
            System.out.println("Message " + i + " bodycontent: \r\n" + pmm.getBodyText());
        }
    }
}
