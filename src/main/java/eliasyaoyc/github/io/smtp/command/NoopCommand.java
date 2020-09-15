package eliasyaoyc.github.io.smtp.command;


import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;

import java.io.IOException;

/**
 * {@link NoopCommand}This command does not affect any parameters or previously entered commands. It
 * specifies no action other than that the receiver send an OK reply.
 *
 * <p>This command has no effect on any of the reverse-path buffer, the forward-path buffer, or the
 * mail data buffer.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class NoopCommand extends AbstractCommand {

  public NoopCommand() {
    super("NOOP", "The noop command");
  }

  @Override
  public SMTPCommandReply execute(String commandName, Session session) throws IOException {
    return new SMTPCommandReply(SMTPReplyStatus.R250, "OK");
  }
}
