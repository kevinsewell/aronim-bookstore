rootProject.name = "aronim-bookstore"

include("aronim-bookstore-backend-application")
project(":aronim-bookstore-backend-application").projectDir = file("backend/application")

include(":aronim-bookstore-backend-module-catalog")
project(":aronim-bookstore-backend-module-catalog").projectDir = file("backend/module-catalog")

include(":aronim-bookstore-backend-platform-core")
project(":aronim-bookstore-backend-platform-core").projectDir = file("backend/platform-core")

include(":aronim-bookstore-backend-platform-security-test")
project(":aronim-bookstore-backend-platform-security-test").projectDir = file("backend/platform-security-test")
