package eliasyaoyc.github.io.smtp;

/**
 * {@link LifeCycle}
 *
 * @author <a href="mailto:siran0611@gmail.com">Elias.Yao</a>
 * @version ${project.version} - 2020/9/15
 */
public interface LifeCycle {
    void start();

    void stop();

    boolean isStart();
}
