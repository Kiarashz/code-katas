import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.security.MessageDigest

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Please provide a valid search path and a comma-separated list of file extensions.")
        return
    }

    val searchPath = args[0]
    val extensions = args[1].split(",").map { it.trim().lowercase() }
    val filesMap = mutableMapOf<String, MutableList<File>>()

    findDuplicates(File(searchPath), extensions, filesMap)

    printDuplicates(filesMap)
}

fun findDuplicates(file: File, extensions: List<String>, filesMap: MutableMap<String, MutableList<File>>) {
    if (file.isDirectory) {
        val files = file.listFiles()
        if (files != null) {
            for (childFile in files) {
                if (!Files.isSymbolicLink(childFile.toPath())) {
                    findDuplicates(childFile, extensions, filesMap)
                }
            }
        }
    } else {
        val fileExtension = getFileExtension(file)
        if (extensions.contains(fileExtension.lowercase())) {
            val fileContent = file.readBytes()
            val fileHash = getHash(fileContent)
    
            filesMap.getOrPut(fileHash) { mutableListOf() }.add(file)
        }
    }
}

fun getFileExtension(file: File): String {
    val fileName = file.name
    val extensionIndex = fileName.lastIndexOf(".")
    return if (extensionIndex != -1) {
        fileName.substring(extensionIndex + 1)
    } else {
        ""
    }
}

fun getHash(content: ByteArray): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(content)
    return hashBytes.joinToString("") { "%02x".format(it) }
}

fun printDuplicates(filesMap: Map<String, List<File>>) {
    val duplicateFiles = filesMap.filterValues { it.size > 1 }

    if (duplicateFiles.isEmpty()) {
        println("No duplicate files found.")
        return
    }

    println("Duplicate files:")
    for ((_, files) in duplicateFiles) {
        println("File: ${files[0].name}")
        for (file in files) {
            println("\tPath: ${file.absolutePath}")
        }
        println()
    }
}