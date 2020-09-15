package eliasyaoyc.github.io.smtp.command;

import xyz.vopen.framework.pipeline.processors.smtp.Command;
import xyz.vopen.framework.pipeline.processors.smtp.HelpMessage;
import xyz.vopen.framework.pipeline.processors.smtp.Session;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPCommandReply;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPReplyStatus;
import xyz.vopen.framework.pipeline.processors.smtp.exception.CommandException;

import java.io.IOException;

/**
 * Verifies the presence of a TLS connection if TLS is required. The wrapped command is executed
 * when the test succeeds.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class RequireTLSCommandWrapper implements Command {

  private Command wrapped;

  /** @param wrapped the wrapped command (not null) */
  public RequireTLSCommandWrapper(Command wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public SMTPCommandReply execute(String commandString, xyz.vopen.framework.pipeline.processors.smtp.Session session) throws IOException {
    if (!session.getServerConfig().isRequireTLS() || session.isTLSStarted()) {
      return wrapped.execute(commandString, session);
    } else {
      return new SMTPCommandReply(SMTPReplyStatus.R530, "Must issue a STARTTLS command first");
    }
  }

  public xyz.vopen.framework.pipeline.processors.smtp.HelpMessage getHelp() throws CommandException {
    return wrapped.getHelp();
  }

  public String getName() {
    return wrapped.getName();
  }
}
