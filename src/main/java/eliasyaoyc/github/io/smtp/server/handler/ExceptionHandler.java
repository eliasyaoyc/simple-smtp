package eliasyaoyc.github.io.smtp.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ExceptionHandler}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/5
 */
public class ExceptionHandler implements ChannelHandler {

  private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {}

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {}

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    logger.error("encounter something smtp exception : {}", cause);
  }
}
