package eliasyaoyc.github.io.smtp.command;

import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;
import eliasyaoyc.github.io.smtp.server.handler.SMTPCommandHandler;
import eliasyaoyc.github.io.smtp.server.handler.SMTPDataHandler;

import java.io.IOException;

/**
 * {@link DataCommand} The receiver treats the lines following the command as mail data from the
 * sender. This command causes the mail data from this command to be appended to the mail data
 * buffer. The mail data may contain any of the 128 ASCII character codes.
 *
 * <p>The mail data is terminated by a line containing only a period, that is the character sequence
 * "<CRLF>.<CRLF>" (see Section 4.5.2 on Transparency). This is the end of mail data indication.
 *
 * <p>The end of mail data indication requires that the receiver must now process the stored mail
 * transaction information. This processing consumes the information in the reverse-path buffer, the
 * forward-path buffer, and the mail data buffer, and on the completion of this command these
 * buffers are cleared. If the processing is successful the receiver must send an OK reply. If the
 * processing fails completely the receiver must send a failure reply.
 *
 * <p>When the receiver-SMTP accepts a message either for relaying or for final delivery it inserts
 * at the beginning of the mail data a time stamp line. The time stamp line indicates the identity
 * of the host that sent the message, and the identity of the host that received the message (and is
 * inserting this time stamp), and the date and time the message was received. Relayed messages will
 * have multiple time stamp lines.
 *
 * <p>When the receiver-SMTP makes the "final delivery" of a message it inserts at the beginning of
 * the mail data a return path line. The return path line preserves the information in the
 * <reverse-path> from the MAIL command. Here, final delivery means the message leaves the SMTP
 * world. Normally, this would mean it has been delivered to the destination user, but in some cases
 * it may be further processed and transmitted by another mail system.
 *
 * <p>It is possible for the mailbox in the return path be different from the actual sender's
 * mailbox, for example, if error responses are to be delivered a special error handling mailbox
 * rather than the message senders.
 *
 * <p>The preceding two paragraphs imply that the final mail data will begin with a return path
 * line, followed by one or more time stamp lines. These lines will be followed by the mail data
 * header and body [2].
 *
 * <p>Special mention is needed of the response and further action required when the processing
 * following the end of mail data indication is partially successful. This could arise if after
 * accepting several recipients and the mail data, the receiver-SMTP finds that the mail data can be
 * successfully delivered to some of the recipients, but it cannot be to others (for example, due to
 * mailbox space allocation problems). In such a situation, the response to the DATA command must be
 * an OK reply. But, the receiver-SMTP must compose and send an "undeliverable mail" notification
 * message to the originator of the message. Either a single notification which lists all of the
 * recipients that failed to get the message, or separate notification messages must be sent for
 * each failed recipient . All undeliverable mail notificationmessages are sent using the MAIL
 * command (even if they result from processing a SEND, SOML, or SAML command).
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class DataCommand extends AbstractCommand {

  /** 32k seems reasonable */
  private static final int BUFFER_SIZE = 1024 * 32;

  public DataCommand() {
    super(
        "DATA",
        "Following text is collected as the message.\n" + "End data with <CR><LF>.<CR><LF>");
  }

  /** */
  @Override
  public SMTPCommandReply execute(String commandString, Session session) throws IOException {
    if (!session.getHasMailFrom()) {
      return new SMTPCommandReply(SMTPReplyStatus.R503, "Error: need MAIL command.");
    } else if (session.getRecipientCount() == 0) {
      return new SMTPCommandReply(SMTPReplyStatus.R503, "Error: need RCPT command.");
    }

    SMTPCommandReply smtpCommandReply =
        new SMTPCommandReply(SMTPReplyStatus.R354, "Start mail input; end with <CRLF>.<CRLF>");
    session
        .getChannel()
        .pipeline()
        .replace(
            SMTPCommandHandler.class.getSimpleName(),
            SMTPDataHandler.class.getSimpleName(),
            new SMTPDataHandler());
    return smtpCommandReply;
  }
}
