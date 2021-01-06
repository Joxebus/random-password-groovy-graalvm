// this configuration forces CompileStatic when call groovyc --configscript compiler.groovy <YOUR_FILE>.groovy
withConfig(configuration) {
    ast(groovy.transform.CompileStatic)
    ast(groovy.transform.TypeChecked)
}