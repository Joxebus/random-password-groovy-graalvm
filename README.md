# random-password 
## CLI tool with Groovy and GraalVM


This is sample project of how to create a CLI app with Groovy and GraalVM, to compare how fast
it is the execution with Groovy then Java and finally GraalVM native-image, because of some problems
with CLI tools that Groovy provides when trying to generate the native image the script here provided 
is all home made, so is not include any additional libraries.

## Requirements

- Groovy 3.0.6
- GraalVM CE 20.3.0
- native-image

```shell script
sdk install java 20.3.0.r11-grl
sdk install groovy 3.0.6
gu install native-image
```

## Compile Groovy Script

A compiler configuration is included to properly compile with Static Compilation capabilities
```
withConfig(configuration) {
    ast(groovy.transform.CompileStatic)
    ast(groovy.transform.TypeChecked)
}
```

Now in order to use this configuration at compile time we need to run the following command

```shell script
groovyc -d out/dev --configscript compiler.groovy random-password.groovy 
```

**Note:** `out/dev` is the folder where the files where be placed after compile, and should be included on `java -cp` when run it.

## Run with Java

You can run your script with Java by including in the classpath the Groovy jar file

```shell script
java -cp ".:$GROOVY_HOME/lib/groovy-3.0.6.jar:out/dev" random-password --help
```
## Reflections

You need to include some reflections when compile the project with `native-image` so if you don't have 
the sources then here is a way to generate everything that is needed.
 
### dgm reflections
This are required to compile the native image and use the Groovy capabilities, output file will be 
`/`

```groovy
groovy dgm-reflections-generator.groovy
```

### custom reflections of generated class resources

If for any reason you have problems with the custom reflections, you can compile and create the reflections
 with the generated sources.

```groovy
groovy custom-reflections-generator.groovy
```

After finish do not forget to include the files generated to your configuration `-H:ReflectionConfigurationFiles`.

Thanks to @wololock who published the gist for **dmg reflections** here:  https://gist.github.com/wololock/ac83a8196a8252fbbaacf4ac84e10b36


## Build native-image

```shell script
native-image --allow-incomplete-classpath \
--report-unsupported-elements-at-runtime \
--initialize-at-build-time \
--initialize-at-run-time=org.codehaus.groovy.control.XStreamUtils,groovy.grape.GrapeIvy \
--no-server \
--no-fallback \
-cp ".:$GROOVY_HOME/lib/groovy-3.0.6.jar:out/dev" \
-H:ReflectionConfigurationFiles=conf/custom-reflections.json,conf/java-reflections.json,conf/dgm-all-reflections.json \
random-password
```

### Sample output

```shell script
[random-password:45936]    classlist:   1,419.57 ms,  0.96 GB
[random-password:45936]        (cap):   3,079.69 ms,  0.96 GB
[random-password:45936]        setup:   4,566.05 ms,  0.96 GB
[random-password:45936]     (clinit):     643.62 ms,  3.25 GB
[random-password:45936]   (typeflow):   9,875.05 ms,  3.25 GB
[random-password:45936]    (objects):  11,308.46 ms,  3.25 GB
[random-password:45936]   (features):   1,328.50 ms,  3.25 GB
[random-password:45936]     analysis:  24,121.79 ms,  3.25 GB
[random-password:45936]     universe:   1,301.39 ms,  3.25 GB
[random-password:45936]      (parse):   2,302.86 ms,  3.25 GB
[random-password:45936]     (inline):   4,140.61 ms,  5.40 GB
[random-password:45936]    (compile):  18,052.66 ms,  5.75 GB
[random-password:45936]      compile:  26,585.66 ms,  5.75 GB
[random-password:45936]        image:   4,975.52 ms,  5.75 GB
[random-password:45936]        write:   1,247.91 ms,  5.75 GB
[random-password:45936]      [total]:  64,380.63 ms,  5.75 GB
```

## About Classpath jars included

For this example we are currently using `$GROOVY_HOME/lib/groovy-3.0.6.jar` and de `out/dev` content
but if your script include additional jars you need to provide them here, additional jars that groovy 
provide are the following in this case this is for `Groovy 3.0.6`

```shell
❯ ls $GROOVY_HOME/lib

ST4-4.1.jar                         groovy-jaxb-3.0.6.jar               jansi-1.18.jar
ant-1.10.8.jar                      groovy-jmx-3.0.6.jar                javaparser-core-3.16.1.jar
ant-antlr-1.10.8.jar                groovy-json-3.0.6.jar               javax.servlet-api-3.0.1.jar
ant-junit-1.10.8.jar                groovy-jsr223-3.0.6.jar             jcommander-1.78.jar
ant-launcher-1.10.8.jar             groovy-macro-3.0.6.jar              jline-2.14.6.jar
bsf-2.4.0.jar                       groovy-nio-3.0.6.jar                jsp-api-2.0.jar
commons-cli-1.4.jar                 groovy-servlet-3.0.6.jar            jsr166y-1.7.0.jar
commons-logging-1.2.jar             groovy-sql-3.0.6.jar                junit-4.13.jar
extras-jaxb                         groovy-swing-3.0.6.jar              junit-jupiter-api-5.7.0.jar
gpars-1.2.1.jar                     groovy-templates-3.0.6.jar          junit-jupiter-engine-5.7.0.jar
groovy-3.0.6.jar                    groovy-test-3.0.6.jar               junit-platform-commons-1.7.0.jar
groovy-ant-3.0.6.jar                groovy-test-junit5-3.0.6.jar        junit-platform-engine-1.7.0.jar
groovy-astbuilder-3.0.6.jar         groovy-testng-3.0.6.jar             junit-platform-launcher-1.7.0.jar
groovy-bsf-3.0.6.jar                groovy-xml-3.0.6.jar                multiverse-core-0.7.0.jar
groovy-cli-commons-3.0.6.jar        groovy-yaml-3.0.6.jar               opentest4j-1.2.0.jar
groovy-cli-picocli-3.0.6.jar        groovy.icns                         org.abego.treelayout.core-1.0.1.jar
groovy-console-3.0.6.jar            hamcrest-core-1.3.jar               qdox-1.12.1.jar
groovy-datetime-3.0.6.jar           ivy-2.5.0.jar                       snakeyaml-1.26.jar
groovy-dateutil-3.0.6.jar           jackson-annotations-2.11.2.jar      testng-7.3.0.jar
groovy-docgenerator-3.0.6.jar       jackson-core-2.11.2.jar             xstream-1.4.13.jar
groovy-groovydoc-3.0.6.jar          jackson-databind-2.11.2.jar
groovy-groovysh-3.0.6.jar           jackson-dataformat-yaml-2.11.2.jar
```

### Usage

You can see the options by running the command `-h` or `--help`

```shell script
./random-password --help

usage: random_password -[has]
 -h,--help             Usage Information
 -a,--alphabet <arg>   A set of characters to generate the password
 -s,--size <arg>       set the size of the password 8 by default
```

This tool can receive the alphabet and the size of the expected password, for example:


```shell script
# Provide the alphapet and get a 8 character password
./random_password -a 'asdfghj345#$%*()'

# Use the default alphabet but return a 20 character password
./random_password -s 20

# Override alphabet and size of the password
./random_password -a 'asdfghj345#$%*()' -s 20
```

This is the 3 ways to generate a password with this tool.

### Benchmark 

I've tested this tool running directly with Groovy, then compiling and testing running as Java 
including into the classpath the Groovy jars, finally I compiled to native-image and this is the
results:

![git-config-benchmark](https://github.com/Joxebus/random-password-groovy-graalvm/blob/main/img/groovy-java-graalvm-benchmark.png?raw=true)