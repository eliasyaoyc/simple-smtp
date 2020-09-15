package eliasyaoyc.github.io.smtp.exception;

/**
 * When execute command encounter unknown exception throw this {@link CommandException}.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class CommandException extends RuntimeException {

    public CommandException() {
        super();
    }

    public CommandException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public CommandException(String string) {
        super(string);
    }


    public CommandException(Throwable throwable) {
        super(throwable);
    }
}
