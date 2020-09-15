package eliasyaoyc.github.io.smtp.exception;

public class UnknownCommandException extends CommandException {
  public UnknownCommandException() {
    super();
  }

  public UnknownCommandException(String string) {
    super(string);
  }

  public UnknownCommandException(String string, Throwable throwable) {
    super(string, throwable);
  }

  public UnknownCommandException(Throwable throwable) {
    super(throwable);
  }
}
