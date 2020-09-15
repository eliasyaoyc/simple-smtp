package eliasyaoyc.github.io.smtp.listener.bridge.repository;

import eliasyaoyc.github.io.smtp.listener.bridge.AbstractRepositoryBridge;

import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * {@link SMTPRepositoryBridge}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/9
 */
public class SMTPRepositoryBridge extends AbstractRepositoryBridge<MimeMessage> {

  protected SMTPRepositoryBridge() {}

  @Override
  public void save(MimeMessage message) {}

  @Override
  public List listAll() {
    return null;
  }

  @Override
  public void remove() {}

  @Override
  public void update() {}
}
