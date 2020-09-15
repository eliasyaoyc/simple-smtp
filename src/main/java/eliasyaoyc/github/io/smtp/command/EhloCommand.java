package eliasyaoyc.github.io.smtp.command;

import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.auth.AuthenticationHandlerFactory;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;
import eliasyaoyc.github.io.smtp.utils.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link EhloCommand}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class EhloCommand extends AbstractCommand {

  public EhloCommand() {
    super("EHLO", "Introduce yourself", "<hostname>");
  }

  @Override
  public SMTPCommandReply execute(String commandName, Session session) throws IOException {
    String[] args = this.getArgs(commandName);
    if (args.length < 2) {
      return new SMTPCommandReply(SMTPReplyStatus.R501, "Syntax : EHLO hostname.");
    }
    session.resetMessageState();
    session.setHelo(args[1]);

    //		postfix returns...
    //		250-server.host.name
    //		250-PIPELINING
    //		250-SIZE 10240000
    //		250-ETRN
    //		250 8BITMIME

    // Once upon a time this code tracked whether or not HELO/EHLO has been seen
    // already and gave an error msg.  However, this is stupid and pointless.
    // Postfix doesn't care, so we won't either.  If you want more, read:
    // http://homepages.tesco.net/J.deBoynePollard/FGA/smtp-avoid-helo.html

    List<String> lines = new ArrayList<>();
    lines.add(session.getServerConfig().getHostName() + " Greetings from Netty SMTP server");
    lines.add("8BITMIME");
    int maxSize = session.getServerConfig().getMaxMessageSize();
    if (maxSize > 0) {
      lines.add("SIZE " + maxSize);
    }

    // Enabling / Hiding TLS is a server setting
    if (session.getServerConfig().isEnableTLS() && !session.getServerConfig().isHideTLS()) {
      lines.add("STARTTLS");
    }
    AuthenticationHandlerFactory authFactory =
        session.getServerConfig().getAuthenticationHandlerFactory();
    if (authFactory != null) {
      List<String> supportedMechanisms = authFactory.getAuthenticationMechanisms();
      if (!supportedMechanisms.isEmpty()) {
        lines.add(AuthCommand.VERB + " " + TextUtils.joinTogether(supportedMechanisms, " "));
      }
    }
    return new SMTPCommandReply(SMTPReplyStatus.R250, lines);
  }
}
