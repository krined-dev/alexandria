package utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import no.hnikt.domain.models.TypeDefinition
import no.hnikt.domain.models.files.FileDefinition
import no.hnikt.utils.fileReader

class FileReaderTest : FunSpec({

    test("testing filereader can read from file") {
        val cont = FileDefinition(
            path ="./src/test/resources/",
            type = TypeDefinition.JSON,
            version = 1)
        with(cont){
            val mapFile = fileReader().orNull()
            val file = mapFile?.get("testfile.json")
            val str = String(file!!.bytes)

            str shouldBe """{"test": "test"}"""
        }
    }
})