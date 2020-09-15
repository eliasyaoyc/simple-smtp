package eliasyaoyc.github.io.smtp.listener.bridge;

import java.util.List;

/**
 * {@link AbstractRepositoryBridge}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/9
 */
public abstract class AbstractRepositoryBridge<T> {

  protected AbstractRepositoryBridge() {}

  public abstract void save(T t);

  public abstract List<T> listAll();

  public abstract void remove();

  public abstract void update();
}
