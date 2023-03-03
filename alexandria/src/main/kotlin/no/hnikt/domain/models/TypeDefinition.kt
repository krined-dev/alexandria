package no.hnikt.domain.models

/** * [TypeDefinition]Supported filetypes the application can read. The filetype of a * [FileDefinition] will declare what kind of implementation of readFile is used */
enum class TypeDefinition {
    TAB,
    CSV,
    JSON,
    PDF,
}