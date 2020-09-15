package eliasyaoyc.github.io.smtp.exception;

/**
 * {@link RejectException}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class RejectException extends RuntimeException {
  int code;

  public RejectException() {
    this("Transaction failed");
  }

  public RejectException(String message) {
    this(554, message);
  }

  public RejectException(int code, String message) {
    super(message);

    this.code = code;
  }

  public int getCode() {
    return this.code;
  }

  public String getErrorResponse() {
    return this.code + " " + this.getMessage();
  }
}
