plugins {
    id("java")
}

group = "fr.corentin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Apache Commons Math for linear algebra and optimization
    implementation("org.apache.commons:commons-math3:3.6.1")
    
    // JGraphT for graph algorithms
    implementation("org.jgrapht:jgrapht-core:1.5.2")
    
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}