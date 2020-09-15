package eliasyaoyc.github.io.smtp.command;


import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;

import java.io.IOException;

/**
 * {@link ResetCommand} This command specifies that the current mail transaction is to be aborted.
 * Any stored sender, recipients, and mail data must be discarded, and all buffers and state tables
 * cleared. The receiver must send an OK reply.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class ResetCommand extends AbstractCommand {

  public ResetCommand() {
    super("RESET", "Resets the system");
  }

  @Override
  public SMTPCommandReply execute(String commandName, Session session) throws IOException {
    session.resetMessageState();
    return new SMTPCommandReply(SMTPReplyStatus.R250, "OK");
  }
}
