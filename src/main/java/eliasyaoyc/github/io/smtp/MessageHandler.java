package eliasyaoyc.github.io.smtp;

import eliasyaoyc.github.io.smtp.exception.RejectException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * {@link MessageHandler}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public interface MessageHandler {
  /**
   * Called first, after the MAIL FROM during a SMTP exchange. A MessageHandler is created after the
   * MAIL command is received, so this function is always called, even if the mail transaction is
   * aborted later.
   *
   * @param from is the sender as specified by the client. It will be a rfc822-compliant email
   *     address, already validated by the server.
   * @throws RejectException if the sender should be denied.
   */
  public void from(String from) throws RejectException;

  /**
   * Called once for every RCPT TO during a SMTP exchange. This will occur after a from() call.
   *
   * @param recipient is a rfc822-compliant email address, validated by the server.
   * @throws RejectException if the recipient should be denied.
   */
  public void recipient(String recipient) throws RejectException;

  /**
   * Called when the DATA part of the SMTP exchange begins. This will occur after all recipient()
   * calls are complete.
   *
   * <p>Note: If you do not read all the data, it will be read for you after this method completes.
   *
   * @param data will be the smtp data stream, stripped of any extra '.' chars. The data stream will
   *     be valid only for the duration of the call.
   * @throws RejectException if at any point the data should be rejected.
   * @throws IOException if there is an IO error reading the input data.
   */
  public void data(MimeMessage data) throws RejectException, MessagingException, IOException;

  /**
   * Called after all other methods are completed. Note that this method will be called even if the
   * mail transaction is aborted at some point after the initial from() call.
   */
  public void done();
}
