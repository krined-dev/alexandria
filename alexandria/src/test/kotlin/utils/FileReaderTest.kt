package utils

import arrow.core.None.orNull
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import no.hnikt.domain.models.TypeDefinition
import no.hnikt.domain.models.files.FileDefinition
import no.hnikt.utils.fileReader
import java.util.logging.Logger

class FileReaderTest : FunSpec({

    test("testing filereader can read from file") {
        val cont = FileDefinition(
            path ="src/main/resources/Files/",
            type = TypeDefinition.PDF,
            version = 1)
        with(cont){
            val mapFile = fileReader().orNull()
            /*mapFile?.forEach {
                it.key shouldBe "doc1"
                it.value.bytes.toString() shouldContain("document")
            }*/
            mapFile?.get("doc1")?.bytes.toString() shouldContain("document")
        }
    }
})