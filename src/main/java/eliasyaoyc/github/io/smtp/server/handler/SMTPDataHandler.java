package eliasyaoyc.github.io.smtp.server.handler;

import eliasyaoyc.github.io.smtp.MailTransfer;
import eliasyaoyc.github.io.smtp.Session;
import eliasyaoyc.github.io.smtp.common.SMTPCommandReply;
import eliasyaoyc.github.io.smtp.common.SMTPReplyStatus;
import eliasyaoyc.github.io.smtp.server.SMTPServerConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * {@link SMTPDataHandler}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/6
 */
public class SMTPDataHandler extends ChannelInboundHandlerAdapter {

  private MailTransfer mailTransfer;

  public SMTPDataHandler() {}

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    Session session = (Session) ctx.channel().attr(SMTPServerConfig.attrName).get();

    ByteBuf buf = (ByteBuf) msg;
    // consume '.'
    transformLine(buf);

    // is the line a single dot i.e. end of DARA?
    if (buf.readableBytes() == 1 && buf.getByte(buf.readerIndex()) == '.') {
      // if so, which back to command handler
      ctx.pipeline()
          .replace(
              this.getClass().getSimpleName(),
              SMTPCommandHandler.class.getSimpleName(),
              new SMTPCommandHandler());
      boolean isFinish = session.getMailTransfer().finish(session);
      if (isFinish) {
        ctx.writeAndFlush(new SMTPCommandReply(SMTPReplyStatus.R250, "Ok"));
      } else {
        ctx.writeAndFlush(new SMTPCommandReply(SMTPReplyStatus.R450, "FAILED"));
      }
    } else {
      // add data.
      session.getMailTransfer().addDataLine(buf);
    }
  }

  /**
   * consume '.' if there are other characters on the line .
   *
   * @param line
   */
  private void transformLine(ByteBuf line) {
    if (line.readableBytes() >= 1 && line.getByte(line.readerIndex()) == '.') {
      if (line.readableBytes() > 1) {
        line.readByte();
      }
    }
  }
}
