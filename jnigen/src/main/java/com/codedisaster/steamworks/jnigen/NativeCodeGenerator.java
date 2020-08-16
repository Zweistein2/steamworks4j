package com.codedisaster.steamworks.jnigen;

import com.badlogic.gdx.jnigen.AntPathMatcher;
import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.parsing.CMethodParser;
import com.badlogic.gdx.jnigen.parsing.JavaMethodParser;
import com.badlogic.gdx.jnigen.parsing.JniHeaderCMethodParser;
import com.badlogic.gdx.jnigen.parsing.RobustJavaMethodParser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class NativeCodeGenerator {
    private static final String JNI_METHOD_MARKER = "native";
    private static final String JNI_ARG_PREFIX = "obj_";
    private static final String JNI_RETURN_VALUE = "JNI_returnValue";
    private static final String JNI_WRAPPER_PREFIX = "wrapped_";
    FileDescriptor sourceDir;
    String classpath;
    FileDescriptor jniDir;
    String[] includes;
    String[] excludes;
    AntPathMatcher matcher = new AntPathMatcher();
    JavaMethodParser javaMethodParser = new RobustJavaMethodParser();
    CMethodParser cMethodParser = new JniHeaderCMethodParser();
    CMethodParser.CMethodParserResult cResult;

    /** Generates .h/.cpp fiels from the Java files found in <code>sourceDir</code>, with their .class files being in
     * <code>classpath</code>. The generated files will be stored in <code>jniDir</code>. The <code>includes</code> and
     * <code>excludes</code> parameters allow to specify directories and files that should be included/excluded from the
     * generation. These can be given in the Ant path format. All paths are relative to the applications working directory.
     * @param sourceDir the directory containing the Java files
     * @param classpath the directory containing the .class files
     * @param jniDir the output directory
     * @param includes files/directories to include, can be null (all files are used)
     * @param excludes files/directories to exclude, can be null (no files are excluded)
     * @throws Exception */
    public void generate(final String sourceDir, final String classpath, final String jniDir, final String[] includes, final String[] excludes)
    throws Exception {
        this.sourceDir = new FileDescriptor(sourceDir);
        this.jniDir = new FileDescriptor(jniDir);
        this.classpath = classpath;
        this.includes = includes;
        this.excludes = excludes;

        // check if source directory exists
        if (!this.sourceDir.exists()) {
            throw new Exception("Java source directory '" + sourceDir + "' does not exist");
        }

        // generate jni directory if necessary
        if (!this.jniDir.exists()) {
            if (!this.jniDir.mkdirs()) {
                throw new Exception("Couldn't create JNI directory '" + jniDir + "'");
            }
        }

        // process the source directory, emitting c/c++ files to jniDir
        processDirectory(this.sourceDir);
    }

    private void processDirectory(final FileDescriptor dir) throws Exception {
        final FileDescriptor[] files = dir.list();
        for (final FileDescriptor file : files) {
            if (file.isDirectory()) {
                if (file.path().contains(".svn")) continue;
                if (excludes != null && matcher.match(file.path(), excludes)) continue;
                processDirectory(file);
            } else {
                if (file.extension().equals("java")) {
                    if (file.name().contains("NativeCodeGenerator")) continue;
                    if (includes != null && !matcher.match(file.path(), includes)) continue;
                    if (excludes != null && matcher.match(file.path(), excludes)) continue;
                    final String className = getFullyQualifiedClassName(file);
                    final FileDescriptor hFile = new FileDescriptor(jniDir.path() + "/" + className + ".h");
                    final FileDescriptor cppFile = new FileDescriptor(jniDir + "/" + className + ".cpp");
                    if (file.lastModified() < cppFile.lastModified()) {
                        System.out.println("C/C++ for '" + file.path() + "' up to date");
                        continue;
                    }
                    final String javaContent = file.readString();
                    if (javaContent.contains(JNI_METHOD_MARKER)) {
                        final ArrayList<JavaMethodParser.JavaSegment> javaSegments = javaMethodParser.parse(javaContent);
                        if (javaSegments.size() == 0) {
                            System.out.println("Skipping '" + file + "', no JNI code found.");
                            continue;
                        }
                        System.out.print("Generating C/C++ for '" + file + "'...");
                        generateHFile(file);
                        generateCppFile(javaSegments, hFile, cppFile);
                        System.out.println("done");
                    }
                }
            }
        }
    }

    private String getFullyQualifiedClassName(final FileDescriptor file) {
        String className = file.path().replace(sourceDir.path(), "").replace('\\', '.').replace('/', '.').replace(".java", "");
        if (className.startsWith(".")) className = className.substring(1);
        return className;
    }

    private void generateHFile(final FileDescriptor file) throws Exception {
        final String className = getFullyQualifiedClassName(file);
        final String command = "javac -cp " + classpath + " -h " + jniDir.path() + " -d " + classpath + " " + file;
        final Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        if (process.exitValue() != 0) {
            System.out.println();
            System.out.println("Command: " + command);
            final InputStream errorStream = process.getErrorStream();
            int c = 0;
            while ((c = errorStream.read()) != -1) {
                System.out.print((char)c);
            }
        }
        final File oldFile = new File(jniDir.path() + "/" + className.replace(".", "_") + ".h ");
        final File newFile = new File(jniDir.path() + "/" + className + ".h ");
        final boolean success = oldFile.renameTo(newFile);

        if(!success) {
            System.out.print("Renaming for File: " + oldFile.getName() + " to: " + newFile.getName() + " failed!");
        }
    }

    protected void emitHeaderInclude(final StringBuffer buffer, final String fileName) {
        buffer.append("#include <").append(fileName).append(">\n");
    }

    private void generateCppFile(final ArrayList<JavaMethodParser.JavaSegment> javaSegments, final FileDescriptor hFile, final FileDescriptor cppFile)
    throws Exception {
        final String headerFileContent = hFile.readString();
        final ArrayList<CMethodParser.CMethod> cMethods = cMethodParser.parse(headerFileContent).getMethods();

        final StringBuffer buffer = new StringBuffer();
        emitHeaderInclude(buffer, hFile.name());

        for (final JavaMethodParser.JavaSegment segment : javaSegments) {
            if (segment instanceof JavaMethodParser.JniSection) {
                emitJniSection(buffer, (JavaMethodParser.JniSection)segment);
            }

            if (segment instanceof JavaMethodParser.JavaMethod) {
                final JavaMethodParser.JavaMethod javaMethod = (JavaMethodParser.JavaMethod)segment;
                if (javaMethod.getNativeCode() == null) {
                    throw new RuntimeException("Method '" + javaMethod.getName() + "' has no body");
                }
                final CMethodParser.CMethod cMethod = findCMethod(javaMethod, cMethods);
                if (cMethod == null)
                    throw new RuntimeException("Couldn't find C method for Java method '" + javaMethod.getClassName() + "#"
                                               + javaMethod.getName() + "'");
                emitJavaMethod(buffer, javaMethod, cMethod);
            }
        }
        cppFile.writeString(buffer.toString(), false, "UTF-8");
    }

    private CMethodParser.CMethod findCMethod(final JavaMethodParser.JavaMethod javaMethod, final ArrayList<CMethodParser.CMethod> cMethods) {
        for (final CMethodParser.CMethod cMethod : cMethods) {
            if (cMethod.getHead().endsWith(javaMethod.getClassName() + "_" + javaMethod.getName())
                || cMethod.getHead().contains(javaMethod.getClassName() + "_" + javaMethod.getName() + "__")) {
                // FIXME poor man's overloaded method check...
                // FIXME float test[] won't work, needs to be float[] test.
                if (cMethod.getArgumentTypes().length - 2 == javaMethod.getArguments().size()) {
                    boolean match = true;
                    for (int i = 2; i < cMethod.getArgumentTypes().length; i++) {
                        final String cType = cMethod.getArgumentTypes()[i];
                        final String javaType = javaMethod.getArguments().get(i - 2).getType().getJniType();
                        if (!cType.equals(javaType)) {
                            match = false;
                            break;
                        }
                    }

                    if (match) {
                        return cMethod;
                    }
                }
            }
        }
        return null;
    }

    private void emitLineMarker(final StringBuffer buffer, final int line) {
        buffer.append("\n//@line:");
        buffer.append(line);
        buffer.append("\n");
    }

    private void emitJniSection(final StringBuffer buffer, final JavaMethodParser.JniSection section) {
        emitLineMarker(buffer, section.getStartIndex());
        buffer.append(section.getNativeCode().replace("\r", ""));
    }

    private void emitJavaMethod(final StringBuffer buffer, final JavaMethodParser.JavaMethod javaMethod, final CMethodParser.CMethod cMethod) {
        // get the setup and cleanup code for arrays, buffers and strings
        final StringBuffer jniSetupCode = new StringBuffer();
        final StringBuffer jniCleanupCode = new StringBuffer();
        final StringBuffer additionalArgs = new StringBuffer();
        final StringBuffer wrapperArgs = new StringBuffer();
        emitJniSetupCode(jniSetupCode, javaMethod, additionalArgs, wrapperArgs);
        emitJniCleanupCode(jniCleanupCode, javaMethod, cMethod);

        // check if the user wants to do manual setup of JNI args
        final boolean isManual = javaMethod.isManual();

        // if we have disposable arguments (string, buffer, array) and if there is a return
        // in the native code (conservative, not syntactically checked), emit a wrapper method.
        if (javaMethod.hasDisposableArgument() && javaMethod.getNativeCode().contains("return")) {
            // if the method is marked as manual, we just emit the signature and let the
            // user do whatever she wants.
            if (isManual) {
                emitMethodSignature(buffer, javaMethod, cMethod, null, false);
                emitMethodBody(buffer, javaMethod);
                buffer.append("}\n\n");
            } else {
                // emit the method containing the actual code, called by the wrapper
                // method with setup pointers to arrays, buffers and strings
                final String wrappedMethodName = emitMethodSignature(buffer, javaMethod, cMethod, additionalArgs.toString());
                emitMethodBody(buffer, javaMethod);
                buffer.append("}\n\n");

                // emit the wrapper method, the one with the declaration in the header file
                emitMethodSignature(buffer, javaMethod, cMethod, null);
                if (!isManual) {
                    buffer.append(jniSetupCode);
                }

                if (cMethod.getReturnType().equals("void")) {
                    buffer.append("\t").append(wrappedMethodName).append("(").append(wrapperArgs.toString())
                          .append(");\n\n");
                    if (!isManual) {
                        buffer.append(jniCleanupCode);
                    }
                    buffer.append("\treturn;\n");
                } else {
                    buffer.append("\t").append(cMethod.getReturnType()).append(" ").append(JNI_RETURN_VALUE)
                          .append(" = ").append(wrappedMethodName).append("(").append(wrapperArgs.toString())
                          .append(");\n\n");
                    if (!isManual) {
                        buffer.append(jniCleanupCode);
                    }
                    buffer.append("\treturn " + JNI_RETURN_VALUE + ";\n");
                }
                buffer.append("}\n\n");
            }
        } else {
            emitMethodSignature(buffer, javaMethod, cMethod, null);
            if (!isManual) {
                buffer.append(jniSetupCode);
            }
            emitMethodBody(buffer, javaMethod);
            if (!isManual) {
                buffer.append(jniCleanupCode);
            }
            buffer.append("}\n\n");
        }

    }

    protected void emitMethodBody(final StringBuffer buffer, final JavaMethodParser.JavaMethod javaMethod) {
        // emit a line marker
        emitLineMarker(buffer, javaMethod.getEndIndex());

        // FIXME add tabs cleanup
        buffer.append(javaMethod.getNativeCode());
        buffer.append("\n");
    }

    private String emitMethodSignature(final StringBuffer buffer, final JavaMethodParser.JavaMethod javaMethod, final CMethodParser.CMethod cMethod, final String additionalArguments) {
        return emitMethodSignature(buffer, javaMethod, cMethod, additionalArguments, true);
    }

    private String emitMethodSignature(final StringBuffer buffer, final JavaMethodParser.JavaMethod javaMethod, final CMethodParser.CMethod cMethod, final String additionalArguments,
                                       final boolean appendPrefix) {
        // emit head, consisting of JNIEXPORT,return type and method name
        // if this is a wrapped method, prefix the method name
        String wrappedMethodName = null;
        if (additionalArguments != null) {
            final String[] tokens = cMethod.getHead().replace("\r\n", "").replace("\n", "").split(" ");
            wrappedMethodName = JNI_WRAPPER_PREFIX + tokens[3];
            buffer.append("static inline ");
            buffer.append(tokens[1]);
            buffer.append(" ");
            buffer.append(wrappedMethodName);
            buffer.append("\n");
        } else {
            buffer.append(cMethod.getHead());
        }

        // construct argument list
        // Differentiate between static and instance method, then output each argument
        if (javaMethod.isStatic()) {
            buffer.append("(JNIEnv* env, jclass clazz");
        } else {
            buffer.append("(JNIEnv* env, jobject object");
        }
        if (javaMethod.getArguments().size() > 0) buffer.append(", ");
        for (int i = 0; i < javaMethod.getArguments().size(); i++) {
            // output the argument type as defined in the header
            buffer.append(cMethod.getArgumentTypes()[i + 2]);
            buffer.append(" ");
            // if this is not a POD or an object, we need to add a prefix
            // as we will output JNI code to get pointers to strings, arrays
            // and direct buffers.
            final JavaMethodParser.Argument javaArg = javaMethod.getArguments().get(i);
            if (!javaArg.getType().isPlainOldDataType() && !javaArg.getType().isObject() && appendPrefix) {
                buffer.append(JNI_ARG_PREFIX);
            }
            // output the name of the argument
            buffer.append(javaArg.getName());

            // comma, if this is not the last argument
            if (i < javaMethod.getArguments().size() - 1) buffer.append(", ");
        }

        // if this is a wrapper method signature, add the additional arguments
        if (additionalArguments != null) {
            buffer.append(additionalArguments);
        }

        // close signature, open method body
        buffer.append(") {\n");

        // return the wrapped method name if any
        return wrappedMethodName;
    }

    private void emitJniSetupCode(final StringBuffer buffer, final JavaMethodParser.JavaMethod javaMethod, final StringBuffer additionalArgs,
                                  final StringBuffer wrapperArgs) {
        // add environment and class/object as the two first arguments for
        // wrapped method.
        if (javaMethod.isStatic()) {
            wrapperArgs.append("env, clazz, ");
        } else {
            wrapperArgs.append("env, object, ");
        }

        // arguments for wrapper method
        for (int i = 0; i < javaMethod.getArguments().size(); i++) {
            final JavaMethodParser.Argument arg = javaMethod.getArguments().get(i);
            if (!arg.getType().isPlainOldDataType() && !arg.getType().isObject()) {
                wrapperArgs.append(JNI_ARG_PREFIX);
            }
            // output the name of the argument
            wrapperArgs.append(arg.getName());
            if (i < javaMethod.getArguments().size() - 1) wrapperArgs.append(", ");
        }

        // direct buffer pointers
        for (final JavaMethodParser.Argument arg : javaMethod.getArguments()) {
            if (arg.getType().isBuffer()) {
                final String type = arg.getType().getBufferCType();
                buffer.append("\t").append(type).append(" ").append(arg.getName()).append(" = (").append(type)
                      .append(")(").append(JNI_ARG_PREFIX).append(arg.getName()).append("?env->GetDirectBufferAddress(")
                      .append(JNI_ARG_PREFIX).append(arg.getName()).append("):0);\n");
                additionalArgs.append(", ");
                additionalArgs.append(type);
                additionalArgs.append(" ");
                additionalArgs.append(arg.getName());
                wrapperArgs.append(", ");
                wrapperArgs.append(arg.getName());
            }
        }

        // string pointers
        for (final JavaMethodParser.Argument arg : javaMethod.getArguments()) {
            if (arg.getType().isString()) {
                final String type = "char*";
                buffer.append("\t" + type + " ").append(arg.getName()).append(" = (").append(type)
                      .append(")env->GetStringUTFChars(").append(JNI_ARG_PREFIX).append(arg.getName())
                      .append(", 0);\n");
                additionalArgs.append(", ");
                additionalArgs.append(type);
                additionalArgs.append(" ");
                additionalArgs.append(arg.getName());
                wrapperArgs.append(", ");
                wrapperArgs.append(arg.getName());
            }
        }

        // Array pointers, we have to collect those last as GetPrimitiveArrayCritical
        // will explode into our face if we call another JNI method after that.
        for (final JavaMethodParser.Argument arg : javaMethod.getArguments()) {
            if (arg.getType().isPrimitiveArray()) {
                final String type = arg.getType().getArrayCType();
                buffer.append("\t").append(type).append(" ").append(arg.getName()).append(" = (").append(type)
                      .append(")env->GetPrimitiveArrayCritical(").append(JNI_ARG_PREFIX).append(arg.getName())
                      .append(", 0);\n");
                additionalArgs.append(", ");
                additionalArgs.append(type);
                additionalArgs.append(" ");
                additionalArgs.append(arg.getName());
                wrapperArgs.append(", ");
                wrapperArgs.append(arg.getName());
            }
        }

        // new line for separation
        buffer.append("\n");
    }

    private void emitJniCleanupCode(final StringBuffer buffer, final JavaMethodParser.JavaMethod javaMethod, final CMethodParser.CMethod cMethod) {
        // emit cleanup code for arrays, must come first
        for (final JavaMethodParser.Argument arg : javaMethod.getArguments()) {
            if (arg.getType().isPrimitiveArray()) {
                buffer.append("\tenv->ReleasePrimitiveArrayCritical(" + JNI_ARG_PREFIX).append(arg.getName())
                      .append(", ").append(arg.getName()).append(", 0);\n");
            }
        }

        // emit cleanup code for strings
        for (final JavaMethodParser.Argument arg : javaMethod.getArguments()) {
            if (arg.getType().isString()) {
                buffer.append("\tenv->ReleaseStringUTFChars(" + JNI_ARG_PREFIX).append(arg.getName()).append(", ")
                      .append(arg.getName()).append(");\n");
            }
        }

        // new line for separation
        buffer.append("\n");
    }
}
