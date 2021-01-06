import groovy.json.JsonOutput
import java.util.logging.Logger

Logger log = Logger.getLogger("custom-reflections")

String inputPath = 'out/dev' //'/Users/obautista/Documents/workspace-public/random-passowrd'
String outputPath = 'conf'

File dir = new File(inputPath)
File outputFile = new File(outputPath,'custom-reflections.json')

def reflections = dir.listFiles()
        .findAll{ file -> file.name.endsWith('.class') }
        .collect{ file ->
            [ name: file.name - '.class', allDeclaredConstructors: true, allPublicConstructors: true,
              allDeclaredMethods: true, allPublicMethods: true ]
        }

// Use JsonOutput class as Category
use(JsonOutput) {
    outputFile.withWriter {
        it.write(JsonOutput.prettyPrint(JsonOutput.toJson(reflections)))
    }
    log.info "Reflections generated on ${outputFile.getAbsolutePath()}"
}
