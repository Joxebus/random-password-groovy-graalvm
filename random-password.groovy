// java -cp ".:$GROOVY_HOME/lib/groovy-3.0.6.jar:$GROOVY_HOME/lib/groovy-cli-commons-3.0.6.jar:$GROOVY_HOME/lib/commons-cli-1.4.jar:out/dev" random-password --help

String[] args = getProperty("args") as String[]

def usage = { ->
    println """
usage: random_password -[has]
 -h,--help             Usage Information                     
 -a,--alphabet <arg>   A set of characters to generate the password
 -s,--size <arg>       set the size of the password 8 by default
"""
}


if (!args) {
    println "Invalid arguments, please see the usage section: "
    usage()
    return
}


def generatePassword = { String alphabet, int size ->
    StringBuilder password = new StringBuilder()
    new Random().with {
        size.times {
            password.append(alphabet[ nextInt( alphabet.length() ) ])
        }
    }
    password.toString()
}

def hasOption = { String... options ->
    boolean result = false
    options.each {option ->
        if(args.contains(option)){
            result = true
        }
    }
    result
}

def findOptionIndex = { String... options ->
    int index = 0
    options.each { option ->
        int indexOf = args.findIndexOf { it == option }
        if (indexOf != -1) {
            index = indexOf+1
        }
    }
    index
}

// default values
def letters = (('a'..'z') + ('A'..'Z'))
def numbers = '0'..'9'
def special = '.;!$_-'
def size = 8

String alphabet = (letters+numbers+special).join('')

if (["-h", "--help"].contains(args[0])) {
    usage()
    return
}

if(hasOption("-a", "--alphabet")) {
    alphabet = args[findOptionIndex("-a", "--alphabet")]

}

if(hasOption("-s", "--size")) {
    size = Math.abs(args[findOptionIndex("-s", "--size")] as Integer)
}

println """*** Generating password using ***
Alphabet: $alphabet
Size: $size
"""
println generatePassword(alphabet, size)


