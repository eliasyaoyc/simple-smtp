package eliasyaoyc.github.io.smtp.listener.bridge;

import xyz.vopen.framework.pipeline.repository.ProcessorRepository;

import java.util.List;

/**
 * {@link AbstractRepositoryBridge}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/7/9
 */
public abstract class AbstractRepositoryBridge<T> {

    protected ProcessorRepository processorRepository;

    protected AbstractRepositoryBridge(ProcessorRepository processorRepository){
        this.processorRepository = processorRepository;
    }

    protected ProcessorRepository getProcessorRepository(){
        return this.processorRepository;
    }

    public abstract void save(T t);

    public abstract List<T> listAll();

    public abstract void remove();

    public abstract void update();
}
