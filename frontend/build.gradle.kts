plugins {
    id("org.siouan.frontend-jdk17") version "10.0.0"
}

frontend {
    nodeVersion.set("20.12.2")
    assembleScript.set("run build")
    checkScript.set("run type-check")
}
