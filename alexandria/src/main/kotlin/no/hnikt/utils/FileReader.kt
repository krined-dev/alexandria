package no.hnikt.utils

import io.ktor.server.application.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import org.apache.pdfbox.Loader
import java.io.IOException


const val BASE_PATH = "src/main/resources/Files/"

val mutableDict = mutableMapOf<String, String>()
fun getText(pdfFile: File?): String? {
    val doc: PDDocument = Loader.loadPDF(pdfFile)
    return PDFTextStripper().getText(doc)
}

fun Application.fileReader() {
    var pathCount = 0
    try {
        File(BASE_PATH).walkTopDown().forEach {
            if (it.path.contains(".pdf")) {
                val text = getText(it)
                val fileName = it.name
                mutableDict[fileName] = text.toString()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    println(mutableDict)
    println()
    print(mutableDict["doc1.pdf"])
}