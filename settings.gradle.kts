rootProject.name = "Mixin"
include("compiler")
include("annotations")

include("sample")
include("sample-library-1")
include("sample-library-2")

val local = file("composite_build.local")
if (local.exists()) {
    local.forEachLine {
        val f = file("../$it")
        if (f.exists()) {
            includeBuild(f)
        }
    }
}