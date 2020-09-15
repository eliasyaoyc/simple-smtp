package eliasyaoyc.github.io.smtp.command;

import xyz.vopen.framework.pipeline.processors.smtp.Session;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPCommandReply;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPReplyStatus;
import xyz.vopen.framework.pipeline.processors.smtp.exception.CommandException;
import xyz.vopen.framework.pipeline.processors.smtp.server.SMTPServerConfig;

import java.io.IOException;

/**
 * {@link HelpCommand}This command causes the receiver to send helpful information to the sender of
 * the HELP command. The command may take an argument (e.g., any command name) and return more
 * specific information as a response.
 *
 * <p>This command has no effect on any of the reverse-path buffer, the forward-path buffer, or the
 * mail data buffer.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class HelpCommand extends AbstractCommand {

  public HelpCommand() {
    super(
        "HELP",
        "The HELP command gives help info about the topic specified.\n"
            + "For a list of topics, type HELP by itself.",
        "[ <topic> ]");
  }

  /** */
  @Override
  public SMTPCommandReply execute(String commandString, xyz.vopen.framework.pipeline.processors.smtp.Session session) throws IOException {
    SMTPCommandReply smtpCommandReply = null;
    String args = this.getArgPredicate(commandString);
    if ("".equals(args)) {
      return new SMTPCommandReply(
          SMTPReplyStatus.R214, this.getCommandMessage(session.getServerConfig()));
    }
    try {
      smtpCommandReply =
          new SMTPCommandReply(
              SMTPReplyStatus.R250,
              session.getServerConfig().getCommandHandler().getHelp(args).toOutputString());
    } catch (CommandException e) {
      new SMTPCommandReply(SMTPReplyStatus.R504, "HELP topic \"" + args + "\" unknown.\"");
    }
    return smtpCommandReply;
  }

  private String getCommandMessage(SMTPServerConfig config) {
    return config.getSoftwareName()
        + " on "
        + config.getHostName()
        + "\r\n"
        + "Topics:\r\n"
        + this.getFormattedTopicList(config)
        + "For more info use \"HELP <topic>\".\r\n"
        + "End of HELP info";
  }

  protected String getFormattedTopicList(SMTPServerConfig config) {
    StringBuilder sb = new StringBuilder();
    for (String key : config.getCommandHandler().getVerbs()) {
      sb.append("214-     " + key + "\r\n");
    }
    return sb.toString();
  }
}
