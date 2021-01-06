@Grab(group = "org.reflections", module = "reflections", version = "0.9.11")
@Grab(group = "org.slf4j", module = "slf4j-simple", version = "1.7.25")

import groovy.json.JsonOutput
import org.codehaus.groovy.reflection.GeneratedMetaMethod
import org.reflections.Reflections

def reflections = new Reflections("org.codehaus.groovy.runtime")

def json = reflections.getSubTypesOf(GeneratedMetaMethod).collect {
    [name: it.name, allDeclaredConstructors: true, allPublicConstructors: true, allDeclaredMethods: true, allPublicMethods: true]
}

new File('conf','dgm-all-reflections.json').withWriter {
    it.write(JsonOutput.prettyPrint(JsonOutput.toJson(json)))
}