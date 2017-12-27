package yucl.learn.demo;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class TestAnnotationProcessor extends AbstractProcessor {
    public TestAnnotationProcessor() {
        System.out.println("new TestAnnotationProcessor");
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        System.out.println(" TestAnnotationProcessor init");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("do annotation process");
        return false;
    }
}
