package eliasyaoyc.github.io.smtp.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.framework.pipeline.processors.smtp.MailTransfer;
import xyz.vopen.framework.pipeline.processors.smtp.Session;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPCommandReply;
import xyz.vopen.framework.pipeline.processors.smtp.common.SMTPReplyStatus;
import xyz.vopen.framework.pipeline.processors.smtp.server.SMTPServerConfig;

import java.util.Locale;

/**
 * {@link SessionInitiationHandler}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/5
 */
public class SessionInitiationHandler extends ChannelInboundHandlerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(SessionInitiationHandler.class);

  private SMTPServerConfig config;

  public SessionInitiationHandler(SMTPServerConfig config) {
    this.config = config;
  }

  private volatile long lastAllocatedId = 0;

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    xyz.vopen.framework.pipeline.processors.smtp.Session session = new xyz.vopen.framework.pipeline.processors.smtp.Session(config);
    logger.info("initiation session : {}", session);
    Attribute<xyz.vopen.framework.pipeline.processors.smtp.Session> sessionContext = ctx.channel().attr(config.attrName);
    session.setChannel(ctx);
    session.setSessionId(createSessionId());
    session.setMailTransfer(new MailTransfer());
    sessionContext.set(session);

    SMTPCommandReply reply = new SMTPCommandReply(SMTPReplyStatus.R220, config.getHostName());
    ctx.writeAndFlush(reply);
  }

  private String createSessionId() {
    long id = System.currentTimeMillis();
    synchronized (this) {
      if (id <= lastAllocatedId) id = lastAllocatedId + 1;
      lastAllocatedId = id;
    }
    return Long.toString(id, 36).toUpperCase(Locale.ENGLISH);
  }
}
