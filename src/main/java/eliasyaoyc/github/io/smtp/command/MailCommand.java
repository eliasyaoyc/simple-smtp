package eliasyaoyc.github.io.smtp.command;


import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;
import eliasyaoyc.github.io.smtp.exception.RejectException;
import eliasyaoyc.github.io.smtp.utils.EmailUtils;

import java.io.IOException;
import java.util.Locale;

/**
 * {@link MailCommand} This command is used to initiate a mail transaction in which the mail data is
 * delivered to one or more mailboxes. The argument field contains a reverse-path.
 *
 * <p>The reverse-path consists of an optional list of hosts and the sender mailbox. When the list
 * of hosts is present, it is a "reverse" source route and indicates that the mail was relayed
 * through each host on the list (the first host int the list was the most recent relay). This list
 * is used as a source route to return non-delivery notices to the sender. As each relay host adds
 * itself to the beginning of the list, it must use its name as known in the IPCE to which it is
 * relaying the mail rather than the IPCE from which the mail came (if they are different). In some
 * types of error reporting messages (for example, undeliverable mail notifications) the
 * reverse-path may be null.
 *
 * <p>This command clears the reverse-path buffer, the forward-path buffer, and the mail data
 * buffer; and inserts the reverse-path information from this command into the reverse-path buffer.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class MailCommand extends AbstractCommand {

  public MailCommand() {
    super("MAIL", "Specifies the sender.", "FROM: <sender> [ <parameters> ]");
  }

  @Override
  public SMTPCommandReply execute(String commandString, Session sess) throws IOException {
    if (sess.getHasMailFrom()) {
      return new SMTPCommandReply(SMTPReplyStatus.R503, "Sender already specified.");
    } else {
      if (commandString.trim().equals("MAIL FROM:")) {
        return new SMTPCommandReply(SMTPReplyStatus.R501, "Syntax: MAIL FROM: <address>");
      }

      String args = this.getArgPredicate(commandString);
      if (!args.toUpperCase(Locale.ENGLISH).startsWith("FROM:")) {
        String re =
            "Syntax: MAIL FROM: <address>  Error in parameters: \""
                + this.getArgPredicate(commandString)
                + "\"";
        return new SMTPCommandReply(SMTPReplyStatus.R501, re);
      }

      String emailAddress = EmailUtils.extractEmailAddress(args, 5);
      if (EmailUtils.isValidEmailAddress(emailAddress)) {
        // extract SIZE argument from MAIL FROM command.
        // disregard unknown parameters.
        // parameters.
        int size = 0;
        String largs = args.toLowerCase(Locale.ENGLISH);
        int sizec = largs.indexOf(" size=");
        if (sizec > -1) {
          // disregard non-numeric values.
          String ssize = largs.substring(sizec + 6).trim();
          if (ssize.length() > 0 && ssize.matches("[0-9]+")) {
            size = Integer.parseInt(ssize);
          }
        }
        // Reject the message if the size supplied by the client
        // is larger than what we advertised in EHLO answer.
        if (size > sess.getServerConfig().getMaxMessageSize()) {
          return new SMTPCommandReply(
              SMTPReplyStatus.R552, "5.3.4 Message size exceeds fixed limit");
        }

        try {
          sess.startMailTransaction();
          sess.getMessageHandler().from(emailAddress);
          sess.setDeclaredMessageSize(size);
          sess.setHasMailFrom(true);
          return new SMTPCommandReply(SMTPReplyStatus.R250, "Ok");
        } catch (RejectException ex) {
          return new SMTPCommandReply(SMTPReplyStatus.R502, ex.getErrorResponse());
        }
      } else {
        return new SMTPCommandReply(
            SMTPReplyStatus.R553, "<" + emailAddress + "> Invalid email address.");
      }
    }
  }
}
