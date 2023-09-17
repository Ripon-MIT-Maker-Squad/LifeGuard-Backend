rootProject.name = "PoolGuard-Backend"
include("src:main:tests")
findProject(":src:main:tests")?.name = "tests"
include("src:tests")
findProject(":src:tests")?.name = "tests"
