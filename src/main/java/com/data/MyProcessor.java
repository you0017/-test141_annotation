package com.data;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes(value = {"com.jimo.Data"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("begin");
        for (TypeElement annotation : annotations) {
            if (annotation.getSimpleName().contentEquals("Data")) {
                Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(annotation);
                for (Element element : elementsAnnotatedWith) {
                    String pkg = element.getEnclosingElement().toString();
                    String className = element.getSimpleName().toString();

                    try {
                        rewriteClass(pkg, className);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("1:" + pkg);
                    System.out.println("2:" + className);
                }
            }
            System.out.println("anno:" + annotation);
        }
        System.out.println(roundEnv);
        return true;
    }

    private void rewriteClass(String pkg, String className) throws IOException {
        final JavaFileObject sourceFile = processingEnv.getFiler().createClassFile(pkg + ".NewClass");
//        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(pkg + ".Test");
//        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(pkg + "." + className);
        try (Writer writer = sourceFile.openWriter()) {
            writer.write("package " + pkg + ";");
            writer.write("class Main{");
            writer.write("private int age;");
            writer.write("}");
        }
    }
}
