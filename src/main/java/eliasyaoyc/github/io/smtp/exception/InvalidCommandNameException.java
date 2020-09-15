package eliasyaoyc.github.io.smtp.exception;

@SuppressWarnings("serial")
public class InvalidCommandNameException extends CommandException {
  public InvalidCommandNameException() {
    super();
  }

  public InvalidCommandNameException(String string) {
    super(string);
  }

  public InvalidCommandNameException(String string, Throwable throwable) {
    super(string, throwable);
  }

  public InvalidCommandNameException(Throwable throwable) {
    super(throwable);
  }
}
