val installHook = task<Copy>("installHook") {
    from(File(rootProject.rootDir, "pre-commit"))
    into(File(rootProject.rootDir, ".git/hooks").also { it.setExecutable(true) })
}

tasks {
    "build" {
        dependsOn(installHook)
    }
}
