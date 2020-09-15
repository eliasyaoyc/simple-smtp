package eliasyaoyc.github.io.smtp.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link ConnectionPerIpLimitHandler}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class ConnectionPerIpLimitHandler extends ChannelInboundHandlerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(ConnectionPerIpLimitHandler.class);

  private final Map<String, AtomicInteger> connections = new ConcurrentHashMap<String, AtomicInteger>();
  private final int maxConnectionPerIp;

  public ConnectionPerIpLimitHandler(int maxConnectionPerIp) {
    this.maxConnectionPerIp = maxConnectionPerIp;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    if (maxConnectionPerIp > 0) {
      InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
      String remoteIp = remoteAddress.getAddress().getHostAddress();

      AtomicInteger atomicCount = connections.getOrDefault(remoteIp, new AtomicInteger(1));
      int count = atomicCount.incrementAndGet();
      if (count > maxConnectionPerIp) {
        logger.warn("connection ip : {} reached maximum : {}", remoteIp, maxConnectionPerIp);
        ctx.channel().close();
      }
    }
    ctx.fireChannelActive();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    if (maxConnectionPerIp > 0) {
      InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
      String remoteIp = remoteAddress.getAddress().getHostAddress();
      AtomicInteger atomicCount = connections.get(remoteIp);
      if (atomicCount != null) {
        atomicCount.decrementAndGet();
      }
    }
    ctx.fireChannelInactive();
  }
}
