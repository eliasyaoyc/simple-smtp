package eliasyaoyc.github.io.smtp.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link ConnectionLimitHandler}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class ConnectionLimitHandler extends ChannelInboundHandlerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(ConnectionLimitHandler.class);

  private final AtomicInteger connections = new AtomicInteger(0);
  private volatile int maxConnections = -1;

  public ConnectionLimitHandler(int maxConnections) {
    this.maxConnections = maxConnections;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    if (maxConnections > 0) {
      int currentCount = connections.incrementAndGet();
      if (currentCount > maxConnections) {
        logger.warn("arrived max connections. the number of connections : {}", currentCount);
        ctx.channel().close();
      }
      ctx.fireChannelActive();
    } else {
      ctx.channel().close();
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    if (maxConnections > 0) {
      connections.decrementAndGet();
      ctx.fireChannelInactive();
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    Channel channel = ctx.channel();
    if (channel.isActive()) {
      ctx.close();
    }
    ctx.fireExceptionCaught(cause);
  }
}
