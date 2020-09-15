package eliasyaoyc.github.io.smtp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.framework.pipeline.common.AbstractPipelineLifeCycle;
import xyz.vopen.framework.pipeline.processors.smtp.server.handler.*;

import static xyz.vopen.framework.pipeline.processors.smtp.server.SMTPServerConfig.MAX_LINE_LENGTH;

/**
 * {@link SMTPServer}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/4
 */
public class SMTPServer extends AbstractPipelineLifeCycle {
  private static final Logger logger = LoggerFactory.getLogger(SMTPServer.class);

  private SMTPServerConfig config;
  private NioEventLoopGroup bossGroup;
  private NioEventLoopGroup workerGroup;

  public SMTPServer(SMTPServerConfig config) {
    this.config = config;
  }

  @Override
  protected void preInit() {

  }

  @Override
  public void start() {
    super.start();
    bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    logger.info(
        "Mixmicro SMTP server , host name : {}, port : {},starting.....",
        config.getHostName(),
        config.getPort());
    try {
      serverBootstrap
          .group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          //          .handler(new LoggingHandler(LogLevel.INFO))
          .option(ChannelOption.SO_BACKLOG, 1024)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childOption(ChannelOption.TCP_NODELAY, true)
          .childHandler(
              new ChannelInitializer<NioSocketChannel>() {
                protected void initChannel(NioSocketChannel ch) throws Exception {
                  ChannelPipeline pipeline = ch.pipeline();
                  pipeline.addLast(SmtpReplyEncoder.class.getSimpleName(), new SmtpReplyEncoder());
                  pipeline.addLast(
                      ConnectionLimitHandler.class.getSimpleName(),
                      new ConnectionLimitHandler(config.getMaxConnections()));
                  pipeline.addLast(
                      ConnectionPerIpLimitHandler.class.getSimpleName(),
                      new ConnectionPerIpLimitHandler(config.getMaxConnectionPerIp()));
                  pipeline.addLast(
                      SessionInitiationHandler.class.getSimpleName(),
                      new SessionInitiationHandler(config));
                  pipeline.addLast(
                      DelimiterBasedFrameDecoder.class.getSimpleName(),
                      new DelimiterBasedFrameDecoder(
                          MAX_LINE_LENGTH, true, Delimiters.lineDelimiter()[0]));
                  pipeline.addLast(
                      SMTPCommandHandler.class.getSimpleName(), new SMTPCommandHandler());
                  pipeline.addLast(ExceptionHandler.class.getSimpleName(), new ExceptionHandler());
                }
              });
      ChannelFuture future = serverBootstrap.bind(config.getPort());
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
      logger.error("Mixmicro SMTP server encounter exception: {}", e);
    } finally {
      stop();
    }
  }

  @Override
  public void stop() {
    super.stop();
    try {
      bossGroup.shutdownGracefully().sync();
      workerGroup.shutdownGracefully().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean isStart() {
    return super.isStart();
  }
}
