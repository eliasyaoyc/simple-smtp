package eliasyaoyc.github.io.smtp.command;

import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;
import eliasyaoyc.github.io.smtp.exception.RejectException;
import eliasyaoyc.github.io.smtp.utils.EmailUtils;

import java.io.IOException;
import java.util.Locale;

/**
 * {@link ReceiptCommand} This command is used to identify an individual recipient of the mail data;
 * multiple recipients are specified by multiple use of this command.
 *
 * <p>The forward-path consists of an optional list of hosts and a required destination mailbox.
 * When the list of hosts is present, it is a source route and indicates that the mail must be
 * relayed to the next host on the list. If the receiver-SMTP does not implement the relay function
 * it may user the same reply it would for an unknown local user (550).
 *
 * <p>When mail is relayed, the relay host must remove itself from the beginning forward-path and
 * put itself at the beginning of the reverse-path. When mail reaches its ultimate destination (the
 * forward-path contains only a destination mailbox), the receiver-SMTP inserts it into the
 * destination mailbox in accordance with its host mail conventions.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class ReceiptCommand extends AbstractCommand {
  public ReceiptCommand() {
    super(
        "RCPT",
        "Specifies the recipient. Can be used any number of times.",
        "TO: <recipient> [ <parameters> ]");
  }

  @Override
  public SMTPCommandReply execute(String commandString, Session session) throws IOException {
    if (!session.getHasMailFrom()) {
      return new SMTPCommandReply(SMTPReplyStatus.R503, "Error: need MAIL command");

    } else if (session.getServerConfig().getMaxRecipients() >= 0
        && session.getRecipientCount() >= session.getServerConfig().getMaxRecipients()) {
      return new SMTPCommandReply(SMTPReplyStatus.R452, "Error: too many recipients");
    }

    String args = this.getArgPredicate(commandString);
    if (!args.toUpperCase(Locale.ENGLISH).startsWith("TO:")) {
      return new SMTPCommandReply(
          SMTPReplyStatus.R501,
          "Syntax: RCPT TO: <address>  Error in parameters: \"" + args + "\"");
    } else {
      String recipientAddress = EmailUtils.extractEmailAddress(args, 3);
      try {
        // TODO: 2020/7/6  going here twice.
        session.getMessageHandler().recipient(recipientAddress);
        session.addRecipient(recipientAddress);
        return new SMTPCommandReply(SMTPReplyStatus.R250, "Ok");
      } catch (RejectException ex) {
        return new SMTPCommandReply(SMTPReplyStatus.R502, ex.getErrorResponse());
      }
    }
  }
}
