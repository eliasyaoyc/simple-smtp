package eliasyaoyc.github.io.smtp.command;

import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;

import java.io.IOException;

/**
 * {@link QuitCommand}This command specifies that the receiver must send an OK reply, and then close
 * the transmission channel.
 *
 * <p>The receiver should not close the transmission channel until it receives and replies to a QUIT
 * command (even if there was an error). The sender should not close the transmission channel until
 * it send a QUIT command and receives the reply (even if there was an error response to a previous
 * command). If the connection is closed prematurely the receiver should act as if a RSET command
 * had been received (canceling any pending transaction, but not undoing any previously completed
 * transaction), the sender should act as if the command or transaction in progress had received a
 * temporary error (4xx).
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class QuitCommand extends AbstractCommand {

  public QuitCommand() {
    super("QUIT", "Exit the SMTP session");
  }

  @Override
  public SMTPCommandReply execute(String commandName, Session session) throws IOException {
    SMTPCommandReply bye = new SMTPCommandReply(SMTPReplyStatus.R221, "BYE");
    session.getChannel().writeAndFlush(bye);
    session.getChannel().close();
    return null;
  }
}
