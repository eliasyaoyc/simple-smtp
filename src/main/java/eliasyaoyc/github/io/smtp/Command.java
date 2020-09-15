package eliasyaoyc.github.io.smtp;

import eliasyaoyc.github.io.smtp.command.DataCommand;
import eliasyaoyc.github.io.smtp.command.MailCommand;
import eliasyaoyc.github.io.smtp.command.ReceiptCommand;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.exception.CommandException;

import java.io.IOException;

/**
 * This {@link Command} define the mail transfer or the mail system function requested by the user.
 * SMTP commands are character strings terminated by <CRLF>. The command codes themselves are
 * alphabetic characters terminated by <SP> if parameters follow and <CRLF> otherwise. The syntax of
 * mailboxes must conform to receiver site conventions. The SMTP commands are discussed below:
 *
 * <p>A mail transaction involves several data objects which are communicated as arguments to
 * different commands. The reverse-path is the argument of the MAIL command {@link MailCommand}, The
 * forward-path is the argument of the RCPT command {@link ReceiptCommand}, The mail data is the
 * argument of the DATA command {@link DataCommand} These arguments or data objects must be
 * transmitted and held pending the confirmation communicated by the end of mail data indication
 * which finalizes the transaction, The model for this is that distinct buffers are provided to hold
 * the types of data objects, that is, there is a reverse-path buffer, a forward-path buffer, and a
 * mail data buffer. Specific commands cause information to be appended to a specific buffer, or
 * cause one or more buffers to be cleared.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public interface Command {

  /**
   * execute method.
   *
   * @param commandName the name of command.
   * @param session
   */
  public SMTPCommandReply execute(String commandName, Session session) throws IOException;

  public HelpMessage getHelp() throws CommandException;

  /**
   * Returns the name of the command in upper case. For example "DATA, QUIT, HELLO".
   *
   * @return
   */
  public String getName();
}
