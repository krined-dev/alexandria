# How to compile documents in docs using pandoc

requires pandoc-fignos pandoc-tablenos pandoc-eqnos installed with e.g. pip
requires mermaid-filter installed with npm

Command to compile document

pandoc -t pdf -F mermaid-filter --citeproc --bibliography=ref.bib --csl=harvard-cite-them-right.csl Forprosjekt.md -o Forprosjekt.pdf
