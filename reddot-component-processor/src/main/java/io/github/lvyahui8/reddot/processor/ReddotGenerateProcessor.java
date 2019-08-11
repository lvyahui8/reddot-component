package io.github.lvyahui8.reddot.processor;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.*;

/**
 * 参考:
 * https://github.com/provegard/aptdemo/blob/master/pom.xml
 * https://juejin.im/post/5b8f53355188255c520cecf1
 * https://blog.csdn.net/robertcpp/article/details/51628656
 * https://deors.wordpress.com/2011/10/08/annotation-processors/
 *
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/10 22:33
 */
@SupportedAnnotationTypes({"io.github.lvyahui8.reddot.processor.ReddotTree"})
public class ReddotGenerateProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ReddotTree.class);
        messager.printMessage(Diagnostic.Kind.NOTE,"elements size:" + elements.size());
        if(elements.size() > 0) {
            JavaFileObject sourceFile = null;
            Writer writer = null;
            String pkg = "io.github.lvyahui8.reddot";
            try {
                sourceFile = filer.createSourceFile(pkg + ".Reddot");
                writer = sourceFile.openWriter();
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.ERROR,"create java file failed. eMsg:" + e.getMessage());
            }
            if(writer != null) {
                try {
                    writer.write("package " + pkg + ";\n");
                    writer.write("import io.github.lvyahui8.reddot.model.IReddot;\n");
                    writer.write("import java.util.*;\n");
                    writer.write("public enum Reddot implements IReddot { \n");
                    writer.write("  _root(null),\n");
                    Yaml yaml = new Yaml();
                    for (Element element : elements) {
                        messager.printMessage(Diagnostic.Kind.NOTE,"handle element",element);
                        if(element.getKind() == ElementKind.CLASS){
                            ReddotTree reddotTree = element.getAnnotation(ReddotTree.class);
                            InputStream stream = getClass().getClassLoader().getResourceAsStream(reddotTree.value());
                            if (stream == null) {
                                messager.printMessage(Diagnostic.Kind.ERROR,"reddot config file not found:" + reddotTree.value());
                                return false;
                            }
                            Map<String,Object> trees = yaml.load(stream);
                            Map<String,Object> rootMap = Collections.singletonMap("_root",trees);
                            System.out.println(rootMap);
                            Queue<Map.Entry<String, Object>> queue = new LinkedList<Map.Entry<String, Object>>();
                            Map.Entry<String, Object> current = rootMap.entrySet().iterator().next();
                            queue.add(current);
                            while(! queue.isEmpty()) {
                                current = queue.poll();
                                Object vv = current.getValue();
                                if(vv instanceof Map && !((Map) vv).isEmpty()) {
                                    for (Map.Entry<String,Object> i : ((Map<String,Object>) vv).entrySet()) {
                                        queue.offer(i);
                                        writer.write("  " + i.getKey() + "(" + current.getKey() + "),\n");
                                    }
                                }
                            }
                            messager.printMessage(Diagnostic.Kind.NOTE,"reddotTree :" + reddotTree.toString());
                            messager.printMessage(Diagnostic.Kind.NOTE,"reddotTree :" + IOUtils.toString(stream,"UTF-8"));
                        }
                    }
                    writer.write("  ;\n");
                    writer.write("  private Reddot parent;\n");
                    writer.write("  private int level;\n");
                    writer.write("  private Set<IReddot> children = new HashSet<>(1);\n");
                    writer.write("  Reddot(Reddot parent){\n");
                    writer.write("      this.parent = parent;\n");
                    writer.write("      if(parent != null) {\n");
                    writer.write("          parent.getChildren().add(this);\n");
                    writer.write("          level = parent.level + 1;\n");
                    writer.write("      } else { level = 0;}\n");
                    writer.write("  }\n");
                    writer.write("  @Override public String getUniqKey() { return this.name(); }\n");
                    writer.write("  @Override public boolean isLeaf() { return children.isEmpty(); }\n");
                    writer.write("  @Override public IReddot getParent() {  return parent; }\n");
                    writer.write("  @Override public Set<IReddot> getChildren() {  return children; }\n");
                    writer.write("  @Override public int getLevel() {  return level; }\n");
                    writer.write("}\n");
                    writer.flush();
                } catch (Exception e) {
                    messager.printMessage(Diagnostic.Kind.ERROR,"handle element failed. eMsg:" + e.getMessage());
                } finally {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedOptions = new HashSet<>();
        supportedOptions.add(ReddotTree.class.getCanonicalName());
        return supportedOptions;
    }


    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }
}
