package eliasyaoyc.github.io.smtp.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * {@link LoggingMessageListener} This listener accepts all messages and logs them, the main used in
 * test.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class LoggingMessageListener implements SimpleMessageListener {

  private static final Logger logger = LoggerFactory.getLogger(LoggingMessageListener.class);

  @Override
  public boolean accept(String from, String recipient) {
    logger.info("Accepting mail from " + from + " to " + recipient);
    return true;
  }

  @Override
  public void deliver(String from, String recipient, MimeMessage data) throws MessagingException {
    logger.info(
        "Logging mail from : {} , to : {} , message-id : {} , message-subject : {}",
        from,
        recipient,
        data.getMessageID(),
        data.getSubject());
  }
}
