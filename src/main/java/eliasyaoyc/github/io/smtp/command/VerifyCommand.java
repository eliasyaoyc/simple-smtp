package eliasyaoyc.github.io.smtp.command;

import xyz.vopen.framework.pipeline.processors.smtp.Session;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPCommandReply;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPReplyStatus;

import java.io.IOException;

/**
 * {@link VerifyCommand} This command asks the receiver to confirm that the argument identifies a
 * user. If it is a user name, the full name of the user (if known) and the fully specified mailbox
 * are returned.
 *
 * <p>This command has no effect on any of the reverse-path buffer, the forward-path buffer, or the
 * mail data buffer.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class VerifyCommand extends AbstractCommand {
  public VerifyCommand() {
    super("VRFY", "The vrfy command");
  }

  @Override
  public SMTPCommandReply execute(String commandName, xyz.vopen.framework.pipeline.processors.smtp.Session session) throws IOException {
    return new SMTPCommandReply(SMTPReplyStatus.R502, "VRFY command is disabled");
  }
}
