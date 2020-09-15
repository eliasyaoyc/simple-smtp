package eliasyaoyc.github.io.smtp;

/**
 * {@link AbstractLifeCycle}
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2020/9/15
 */
public abstract class AbstractLifeCycle implements LifeCycle {

  /** whether is running */
  private volatile boolean running = false;

  protected abstract void preInit();

  @Override
  public void start() {
    if (running) {
      throw new RuntimeException(this.getClass().getName() + " has startup , don't repeat start");
    }
    running = true;
  }

  @Override
  public void stop() {
    if (!running) {
      throw new RuntimeException(this.getClass().getName() + " isn't start , please check");
    }
    running = false;
  }

  @Override
  public boolean isStart() {
    return this.running;
  }
}
