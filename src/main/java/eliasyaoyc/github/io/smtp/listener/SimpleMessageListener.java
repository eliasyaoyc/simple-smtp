package eliasyaoyc.github.io.smtp.listener;

import eliasyaoyc.github.io.smtp.MessageHandler;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * {@link SimpleMessageListener} This is an interface for processing the end-result message that is
 * higher-level than the {@link MessageHandler} and
 * related factory.
 *
 * <p>While the SMTP message is being received, all listeners are asked if they want to accept each
 * recipient. After the message has arrived, the message is handed off to all accepting listeners.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public interface SimpleMessageListener {

  /**
   * Called once for evert RCPT to during a SMTP exchange. Each accepted recipient will result in a
   * separate {@link SimpleMessageListener#deliver} call later.
   *
   * @param from is a rfc822-compliant email address.
   * @param recipient is a rfc822-compliant email address.
   * @return true if the listener wants delivery of the message, false if the message is not for
   *     this listener.
   */
  boolean accept(String from, String recipient);

  /**
   * @param from
   * @param recipient
   * @param data
   * @throws IOException
   */
  void deliver(String from, String recipient, MimeMessage data) throws MessagingException;
}
