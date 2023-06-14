plugins {
    id("java")
}

group = "kr.hqservice.team"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc", "spigot", "1.12.2-R0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    test {
        useJUnitPlatform()
    }
    jar {
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
        destinationDirectory.set(File("D:\\서버\\1.19.3 - 개발\\plugins"))
    }
}