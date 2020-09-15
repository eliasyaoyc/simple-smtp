package eliasyaoyc.github.io.smtp;

import eliasyaoyc.github.io.smtp.command.CommandRegistry;
import eliasyaoyc.github.io.smtp.command.RequireTLSCommandWrapper;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;
import eliasyaoyc.github.io.smtp.exception.CommandException;
import eliasyaoyc.github.io.smtp.exception.InvalidCommandNameException;
import eliasyaoyc.github.io.smtp.exception.UnknownCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * {@link CommandHandler} This class manage execution of a SMTP command.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class CommandHandler {
  private static final Logger log = LoggerFactory.getLogger(CommandHandler.class);

  /** The map of known SMTP commands. Keys are upper case names of the commands. */
  private Map<String, Command> commandMap = new HashMap<String, Command>();

  public CommandHandler() {
    // This solution should be more robust than the earlier "manual" configuration.
    for (CommandRegistry registry : CommandRegistry.values()) {
      this.addCommand(registry.getCommand());
    }
  }

  /**
   * Create a command handler with a specific set of commands.
   *
   * @param availableCommands the available commands (not null) TLS note: wrap commands with {@link
   *     RequireTLSCommandWrapper} when appropriate.
   */
  public CommandHandler(Collection<Command> availableCommands) {
    for (Command command : availableCommands) {
      this.addCommand(command);
    }
  }

  /**
   * Adds or replaces the specified command.
   *
   * @param command
   */
  public void addCommand(Command command) {
    if (log.isDebugEnabled()) log.debug("Added command: " + command.getName());

    this.commandMap.put(command.getName(), command);
  }

  /**
   * Returns the command object corresponding to the specified command name.
   *
   * @param commandName case insensitive name of the command.
   * @return the command object, or null, if the command is unknown.
   */
  public Command getCommand(String commandName) {
    String upperCaseCommandName = commandName.toUpperCase(Locale.ENGLISH);
    return this.commandMap.get(upperCaseCommandName);
  }

  /**
   * Return true if {@link CommandHandler#commandMap} contains specified command, or false if not
   * contain.
   *
   * @param command
   * @return
   */
  public boolean containsCommand(String command) {
    return this.commandMap.containsKey(command);
  }

  public Set<String> getVerbs() {
    return this.commandMap.keySet();
  }

  /**
   * Main method, invoke real command.
   *
   * @param context
   * @param commandString
   * @throws SocketTimeoutException
   * @throws IOException
   */
  public SMTPCommandReply handleCommand(Session context, String commandString)
      throws SocketTimeoutException, IOException {
    try {
      Command command = this.getCommandFromString(commandString);
      return command.execute(commandString, context);
    } catch (CommandException e) {
      return new SMTPCommandReply(SMTPReplyStatus.R500, e.getMessage());
    }
  }

  /**
   * Return the HelpMessage object for the given command name (verb)
   *
   * @throws CommandException
   */
  public HelpMessage getHelp(String command) throws CommandException {
    return this.getCommandFromString(command).getHelp();
  }

  private Command getCommandFromString(String commandString) throws UnknownCommandException {
    Command command = null;
    String key = this.toKey(commandString);
    if (key != null) {
      command = this.commandMap.get(key);
    }
    if (command == null) {
      // some commands have a verb longer than 4 letters
      String verb = this.toVerb(commandString);
      if (verb != null) {
        command = this.commandMap.get(verb);
      }
    }
    if (command == null) {
      throw new UnknownCommandException("Error: command not implemented");
    }
    return command;
  }

  private String toKey(String string) throws InvalidCommandNameException {
    if (string == null || string.length() < 4)
      throw new InvalidCommandNameException("Error: bad syntax");

    return string.substring(0, 4).toUpperCase(Locale.ENGLISH);
  }

  private String toVerb(String string) throws InvalidCommandNameException {
    StringTokenizer stringTokenizer = new StringTokenizer(string);
    if (!stringTokenizer.hasMoreTokens())
      throw new InvalidCommandNameException("Error: bad syntax");

    return stringTokenizer.nextToken().toUpperCase(Locale.ENGLISH);
  }
}
