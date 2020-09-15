package eliasyaoyc.github.io.smtp.command;

import xyz.vopen.framework.pipeline.processors.smtp.Command;

/**
 * {@link CommandRegistry} Enumerates all the Commands made available in this release.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public enum CommandRegistry {
  AUTH(new AuthCommand()),
  DATA(new DataCommand()),
  EHLO(new EhloCommand(), false),
  HELO(new HelloCommand()),
  HELP(new HelpCommand()),
  MAIL(new MailCommand()),
  NOOP(new NoopCommand(), false),
  QUIT(new QuitCommand(), false),
  RCPT(new ReceiptCommand()),
  RSET(new ResetCommand()),
//  STARTTLS(new StartTLSCommand(), false),
  VRFY(new VerifyCommand());

  private xyz.vopen.framework.pipeline.processors.smtp.Command command;

  private CommandRegistry(xyz.vopen.framework.pipeline.processors.smtp.Command cmd) {
    this(cmd, true);
  }

  private CommandRegistry(xyz.vopen.framework.pipeline.processors.smtp.Command cmd, boolean checkForStartedTLSWhenRequired) {
    if (checkForStartedTLSWhenRequired) {
      this.command = new RequireTLSCommandWrapper(cmd);
    } else {
      this.command = cmd;
    }
  }

  public xyz.vopen.framework.pipeline.processors.smtp.Command getCommand() {
    return this.command;
  }
}
