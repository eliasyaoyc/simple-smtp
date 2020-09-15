package eliasyaoyc.github.io.smtp;

import eliasyaoyc.github.io.smtp.io.DotTerminatedInputStream;
import eliasyaoyc.github.io.smtp.io.DotUnstuffingInputStream;
import eliasyaoyc.github.io.smtp.io.ReceivedHeaderStream;
import eliasyaoyc.github.io.smtp.listener.SimpleMessageListener;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * {@link MailTransfer} A transfer where message are stored via listeners {@link
 * SimpleMessageListener}consume.
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/6
 */
public class MailTransfer {

  private static final Logger logger = LoggerFactory.getLogger(MailTransfer.class);

  private ByteArrayOutputStream outputStream;
  private MimeMessage mimeMessage;

  public MailTransfer() {
    outputStream = new ByteArrayOutputStream();
  }

  public void addDataLine(ByteBuf byteBuf) throws IOException {
    byte[] bytes = new byte[byteBuf.readableBytes()];
    byteBuf.getBytes(byteBuf.readerIndex(), bytes);
    outputStream.write(bytes);
    outputStream.write("\r\n".getBytes());
  }

  public boolean finish(Session session) throws Exception {
    outputStream.write(".".getBytes());
    outputStream.write("\r\n".getBytes());
    // parse InputStream.
    parseData(session);
    // invoker all listeners.
    triggerListeners(session);
    return true;
  }

  private void triggerListeners(Session session) throws Exception {
    session.getMessageHandler().data(mimeMessage);
  }

  private void parseData(Session session) {
    InputStream io = new ByteArrayInputStream(outputStream.toByteArray());
    io = new BufferedInputStream(io, 1024 * 32);
    io = new DotTerminatedInputStream(io);
    io = new DotUnstuffingInputStream(io);

    InetSocketAddress socketAddress = (InetSocketAddress) session.getChannel().remoteAddress();
    InetAddress address = socketAddress.getAddress();
    io =
        new ReceivedHeaderStream(
            io,
            session.getHelo(),
            socketAddress.getAddress(),
            session.getServerConfig().getHostName(),
            session.getServerConfig().getSoftwareName(),
            session.getSessionId(),
            session.getSingleRecipient());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    io = new BufferedInputStream(io);
    int current;
    while (true) {
      try {
        if (!((current = io.read()) >= 0)) {
          break;
        }
        out.write(current);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    byte[] bytes = out.toByteArray();
    try {
      mimeMessage =
          new MimeMessage(
              javax.mail.Session.getDefaultInstance(new Properties()),
              new ByteArrayInputStream(bytes));
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
