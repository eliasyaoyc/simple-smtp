package eliasyaoyc.github.io.smtp.listener.bridge.repository;

import xyz.vopen.framework.pipeline.processors.smtp.listener.bridge.AbstractRepositoryBridge;
import xyz.vopen.framework.pipeline.repository.mongo.ProcessorSMTPRepositoryImpl;

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

  private ProcessorSMTPRepositoryImpl processorSMTPRepository;

  protected SMTPRepositoryBridge() {
    super(new ProcessorSMTPRepositoryImpl());
    this.processorSMTPRepository = (ProcessorSMTPRepositoryImpl) getProcessorRepository();
  }

  @Override
  public void save(MimeMessage message) {
    processorSMTPRepository.saveSMTPMessage(message);
  }

  @Override
  public List listAll() {
    return null;
  }

  @Override
  public void remove() {}

  @Override
  public void update() {}
}
