package eliasyaoyc.github.io.smtp.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.framework.pipeline.processors.smtp.Command;
import xyz.vopen.framework.pipeline.processors.smtp.HelpMessage;
import xyz.vopen.framework.pipeline.processors.smtp.Session;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPCommandReply;
import xyz.vopen.framework.pipeline.processors.smtp.exception.CommandException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * {@link AbstractCommand} The {@link Command} Basic implementation.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public abstract class AbstractCommand implements Command {

  private static Logger logger = LoggerFactory.getLogger(AbstractCommand.class);

  /** The name of the Command, ie HELO. */
  private String commandName;

  /** The help message for this Command. */
  private HelpMessage helpMessage;

  public AbstractCommand(String commandName, String help) {
    this.commandName = commandName;
    this.helpMessage = new HelpMessage(commandName, help);
  }

  public AbstractCommand(String commandName, String help, String argumentDescription) {
    this.commandName = commandName;
    this.helpMessage = new HelpMessage(commandName, help, argumentDescription);
  }

  /**
   * This is the {@link Command} main method that if you need define yourself command, you must
   * override this method.
   *
   * @param commandName the name of command.
   * @param session
   * @throws IOException
   */
  @Override
  public abstract SMTPCommandReply execute(String commandName, Session session) throws IOException;

  @Override
  public HelpMessage getHelp() throws CommandException {
    return this.helpMessage;
  }

  @Override
  public String getName() {
    return this.commandName;
  }

  protected String getArgPredicate(String commandString) {
    if (commandString == null || commandString.length() < 4) {
      return "";
    }
    return commandString.substring(4).trim();
  }

  protected String[] getArgs(String commandString) {
    List<String> strings = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(commandString);
    while (st.hasMoreTokens()) {
      strings.add(st.nextToken());
    }
    return strings.toArray(new String[strings.size()]);
  }
}
