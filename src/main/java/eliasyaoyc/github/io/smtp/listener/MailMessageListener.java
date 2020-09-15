package eliasyaoyc.github.io.smtp.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * {@link MailMessageListener}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/9
 */
public class MailMessageListener implements SimpleMessageListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(MailMessageListener.class);

  @Override
  public boolean accept(String from, String recipient) {
    return false;
  }

  @Override
  public void deliver(String from, String recipient, MimeMessage data) throws MessagingException {
    Transport.send(data);
  }
}
