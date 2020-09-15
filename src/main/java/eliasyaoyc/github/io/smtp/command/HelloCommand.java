package eliasyaoyc.github.io.smtp.command;

import xyz.vopen.framework.pipeline.processors.smtp.Session;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPCommandReply;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPReplyStatus;

import java.io.IOException;

/**
 * The {@link HelloCommand} alias HELO, this command is used to identify the sender-SMTP to
 * receiver-SMTP. The argument field contains the host name of the sender-SMTP. The receiver-SMTP
 * identifies itself to the sender-SMTP in the connection greeting reply, and in the response to
 * this command. This command and an OK reply to it confirm that both the sender-SMTP and the
 * receiver-SMTP are in the initial state,that is, there is no transaction in progress and all state
 * tables and buffers are cleared.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class HelloCommand extends AbstractCommand {

  public HelloCommand() {
    super("HELO", "Introduce yourself.", "<hostname>");
  }

  @Override
  public SMTPCommandReply execute(String commandName, xyz.vopen.framework.pipeline.processors.smtp.Session session) throws IOException {
    String[] args = this.getArgs(commandName);
    if (args.length < 2) {
      return new SMTPCommandReply(SMTPReplyStatus.R501, "Syntax: HELO <hostname>");
    }
    session.resetMessageState();
    session.setHelo(args[1]);
    return new SMTPCommandReply(SMTPReplyStatus.R250, session.getServerConfig().getHostName());
  }
}
