package eliasyaoyc.github.io.smtp.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.framework.pipeline.processors.smtp.Session;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPCommandReply;
import xyz.vopen.framework.pipeline.processors.smtp.exception.UnknownCommandException;
import xyz.vopen.framework.pipeline.processors.smtp.server.SMTPServerConfig;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * {@link SMTPCommandHandler}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class SMTPCommandHandler extends ChannelInboundHandlerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(SMTPCommandHandler.class);

  private static final CharSequence[] VALID_COMMANDS =
      new String[] {"HELO", "EHLO", "MAIL", "RSET", "VRFY", "EXPN", "NOOP", "QUIT"};
  private static final CharSequence[] ALWAYS_ALLOWED_COMMANDS =
      new String[] {"HELO", "EHLO", "RSET"};

  public SMTPCommandHandler() {}

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf frame = (ByteBuf) msg;
    xyz.vopen.framework.pipeline.processors.smtp.Session session = (xyz.vopen.framework.pipeline.processors.smtp.Session) ctx.channel().attr(SMTPServerConfig.attrName).get();
    if (session == null) {
      return;
    }
    if (frame.readableBytes() < 4) {
      throw new UnknownCommandException("unknown command");
    }

    CharSequence charSequence =
        frame.readCharSequence(frame.readableBytes(), StandardCharsets.UTF_8);
    SMTPServerConfig config = session.getServerConfig();

    int iac = charSequence.length();
    for (int i = 0, n = charSequence.length(); i < n; i++) {
      if (charSequence.charAt(i) == ' ') {
        iac = i;
        break;
      }
    }
    CharSequence cmd = charSequence.subSequence(0, iac);
    CharSequence argument =
        charSequence.length() > iac ? charSequence.subSequence(iac, charSequence.length()) : null;

    if (!validateCommand(cmd) && !validateCommandOrder(session, cmd)) {
      throw new IllegalStateException("Mixmicro SMTP Server encounter command error");
    }
    logger.info("start handle data... current command : {} , current argument : {}", cmd, argument);
    SMTPCommandReply reply = config.getCommandHandler().handleCommand(session, charSequence.toString());
    if (reply != null) {
      ctx.writeAndFlush(reply);
    }
    session.setLastCommand(cmd);
  }

  private boolean validateCommand(CharSequence cmd) {
    return Arrays.stream(VALID_COMMANDS).anyMatch(c -> charComparator(c, cmd));
  }

  private boolean validateCommandOrder(xyz.vopen.framework.pipeline.processors.smtp.Session session, CharSequence cmd) {
    if (Arrays.stream(ALWAYS_ALLOWED_COMMANDS).anyMatch(c -> charComparator(c, cmd))) {
      return true;
    }
    if (session.getMessageHandler() != null) {
      if (charComparator(cmd, "RCPT") && charComparator(session.getLastCommand(), "MAIL")
          || charComparator(cmd, "RCPT") && charComparator(session.getLastCommand(), "RCPT")
          || charComparator(cmd, "DATA") && charComparator(session.getLastCommand(), "RCPT")) {
        return true;
      }
      return false;
    }
    return true;
  }

  private boolean charComparator(CharSequence c1, CharSequence c2) {
    if (c1 == null && c2 == null) {
      return true;
    }
    if (c1 != null && c2 == null || c1 == null && c2 != null || c1.length() != c2.length()) {
      return false;
    }
    for (int i = 0; i < c1.length(); i++) {
      if (c1.charAt(i) != c2.charAt(i)) {
        return false;
      }
    }
    return true;
  }
}
