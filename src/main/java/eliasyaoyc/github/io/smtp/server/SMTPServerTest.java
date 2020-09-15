package eliasyaoyc.github.io.smtp.server;


import eliasyaoyc.github.io.smtp.listener.LoggingMessageListener;
import eliasyaoyc.github.io.smtp.listener.SimpleMessageListenerAdapter;

/**
 * {@link SMTPServerTest}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class SMTPServerTest {
  public static void main(String[] args) {
    SMTPServerConfig config = new SMTPServerConfig(new SimpleMessageListenerAdapter(new LoggingMessageListener()));
    SMTPServer smtpServer = new SMTPServer(config);
    smtpServer.start();
  }
}
