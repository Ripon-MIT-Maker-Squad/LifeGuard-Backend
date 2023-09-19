rootProject.name = "PoolGuard-Backend"
include("src:main:tests")
findProject(":src:main:tests")?.name = "tests"
include("src:tests")
findProject(":src:tests")?.name = "tests"
include("src:test")
findProject(":src:test")?.name = "test"
include("src:test")
findProject(":src:test")?.name = "test"
include("src:test")
findProject(":src:test")?.name = "test"
