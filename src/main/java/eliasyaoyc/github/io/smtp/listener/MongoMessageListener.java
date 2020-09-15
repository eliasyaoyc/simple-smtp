package eliasyaoyc.github.io.smtp.listener;

import eliasyaoyc.github.io.smtp.listener.bridge.repository.SMTPRepositoryBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * {@link MongoMessageListener}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/9
 */
public class MongoMessageListener extends SMTPRepositoryBridge implements SimpleMessageListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoMessageListener.class);

    @Override
    public boolean accept(String from, String recipient) {
        return false;
    }

    @Override
    public void deliver(String from, String recipient, MimeMessage data) throws MessagingException {
        save(data);
    }
}
