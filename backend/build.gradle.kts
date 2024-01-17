import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Property
import org.springframework.boot.gradle.tasks.run.BootRun
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jooq:jooq-codegen:${project.property("buildJooqVersion")}")
        classpath("org.jooq:jooq-meta:${project.property("buildJooqVersion")}")
        classpath("org.jooq:jooq-meta-extensions:${project.property("buildJooqVersion")}")
        classpath("com.h2database:h2:${project.property("h2Version")}")
    }
}

plugins {
    val springBootVersion = "3.2.0"
    val springDependencyManagement = "1.1.4"
    val kotlinVersion = "1.9.21"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version springDependencyManagement

    id("com.github.node-gradle.node") version "7.0.1"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"

    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

group = "com.naonworks"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

configurations {
    configureEach {
        this.exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // smtp
    implementation ("org.springframework.boot:spring-boot-starter-mail")

    // spring security + JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // log4j2
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("com.lmax:disruptor:3.4.4")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    implementation("org.yaml:snakeyaml:2.2")

    // db r2dbc
    implementation("io.r2dbc:r2dbc-spi")
    implementation("io.r2dbc:r2dbc-pool")

    runtimeOnly("org.mariadb:r2dbc-mariadb:1.1.3")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("io.r2dbc:r2dbc-h2")

    // jooq r2dbc
    implementation("org.jooq:jooq-kotlin:${project.property("jooqVersion")}")
    implementation("org.jooq:jooq-kotlin-coroutines:${project.property("jooqVersion")}")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.1.0")

    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")

    // mapstruct
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
    kaptTest("org.mapstruct:mapstruct-processor:1.5.3.Final")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    buildInfo()
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    enabled = true

    launchScript {
        properties["initInfoRequiredStart"] = "\$remote_fs \$syslog \$network \$mariadb"
        properties["initInfoRequiredStop"] = "\$remote_fs \$syslog \$network \$mariadb"
    }

    val gitBranch = tasks.generateGitProperties.get().generatedProperties["git.branch"].toString().split("/").last()
    val gitCommitId = tasks.generateGitProperties.get().generatedProperties["git.commit.id"].toString()
    val commitTime = tasks.generateGitProperties.get().generatedProperties["git.commit.time"].toString()

    val commitTimeParse = OffsetDateTime.parse(commitTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
    val commitDateStr = commitTimeParse.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_Z"))

    archiveAppendix = gitBranch
    archiveVersion = "${commitDateStr}-${gitCommitId}"

    archiveClassifier = ""

    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = gitCommitId
    }
}

//import org.springframework.boot.gradle.tasks.run.BootRun
tasks.register("bootRunLocal", BootRun::class.java) {
    group = "Application"

    this.mainClass = tasks.bootRun.get().mainClass
    this.classpath = sourceSets.main.get().runtimeClasspath

    dependencies {
        implementation("com.h2database:h2")
        implementation("io.r2dbc:r2dbc-h2")
    }

    args(
            "--spring.profiles.active=local",
    )

    jvmArgs(
            "-Xms128m",
            "-Xmx128m",
            "-XX:HeapDumpPath=/tmp/webDump.hprof",
    )
}

sourceSets {
    main {
        val taskNames = project.gradle.startParameter.taskNames

        if (taskNames.any { it == "bootRunLocal" }) {
            kotlin.srcDirs(kotlin.srcDirs, file("$projectDir/src/test/local"))
            resources.srcDirs(resources.srcDirs, file("$projectDir/src/test/resources"))
        }
    }

    test {
        kotlin.srcDirs(kotlin.srcDirs, file("$projectDir/src/test/local"))
    }
}
//========================================================
// git properties
gitProperties {
    keys = arrayListOf("git.branch", "git.commit.id", "git.commit.id.abbrev", "git.commit.time", "git.tags")
}

//========================================================
// jooq
//import org.jooq.codegen.GenerationTool
//import org.jooq.meta.jaxb.*
val jooqSchemaFile = "$projectDir/src/test/resources/db/h2/gen-*.sql"
val jooqSrcDir = "${layout.buildDirectory.get()}/generated/jooq"
val jooqPkg = "${group}.entity"

val jooqTableSet = MatchersTableType()
    .withExpression("^(.+)\$")
    .withTableIdentifier(MatcherRule().withTransform(MatcherTransformType.AS_IS).withExpression("\$0"))
    .withTableClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_TABLE"))
    .withRecordClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_RECORD"))
    .withInterfaceClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_INF"))
    .withDaoClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_DAO"))
    .withPojoClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_POJO"))

val jooqFieldSet = MatchersFieldType()
    .withExpression("^(.+)\$")
    .withFieldIdentifier(MatcherRule().withTransform(MatcherTransformType.AS_IS).withExpression("\$0"))
    .withFieldMember(MatcherRule().withTransform(MatcherTransformType.CAMEL).withExpression("\$0"))
    .withFieldSetter(MatcherRule().withTransform(MatcherTransformType.CAMEL).withExpression("set_\$0"))
    .withFieldGetter(MatcherRule().withTransform(MatcherTransformType.CAMEL).withExpression("get_\$0"))

val jooqConfig = Configuration()
    .withLogging(org.jooq.meta.jaxb.Logging.TRACE)
    .withGenerator(
        Generator()
//                        .withName("org.jooq.codegen.KotlinGenerator")
            .withDatabase(
                Database()
                    .withName("org.jooq.meta.extensions.ddl.DDLDatabase")
                    .withIncludes(".*")
                    .withExcludes("")
                    .withProperties(
                        Property().withKey("scripts").withValue(jooqSchemaFile),
                        Property().withKey("sort").withValue("semantic"),
                        Property().withKey("scriptunqualifiedSchema").withValue("none"),
                    )
            ).withGenerate(
                Generate()
                    .withTables(true)
                    .withInterfaces(true)
                    .withRecords(true)
                    .withPojos(true)
                    .withSerializablePojos(true)
                    .withDaos(false)
                    .withSpringDao(false)

//                                .withKotlinSetterJvmNameAnnotationsOnIsPrefix(false)
//                                .withPojosAsKotlinDataClasses(true)
//                                .withKotlinNotNullInterfaceAttributes(false)
//                                .withKotlinNotNullPojoAttributes(false)
//                                .withKotlinNotNullRecordAttributes(false)

            ).withStrategy(
                Strategy()
                    .withMatchers(
                        Matchers()
                            .withTables(jooqTableSet)
                            .withFields(jooqFieldSet)
                    )
            ).withTarget(
                org.jooq.meta.jaxb.Target()
                    .withClean(true)
                    .withPackageName(jooqPkg)
                    .withDirectory(jooqSrcDir)
            )
    )

val generateJooqTask = tasks.register("generateJooq") {
    group = "jooq"

    GenerationTool.generate(jooqConfig)

    sourceSets.main {
        java.srcDirs(java.srcDirs, file(jooqSrcDir))
    }
}

tasks.compileJava.get().dependsOn(generateJooqTask)
//========================================================
// node, npm
val homePath = System.getProperty("user.home")
val webappDir = "${project.projectDir}/../frontend"
val webappBuildDir = "$webappDir/dist"

node {
    version = "18.17.1"
    npmVersion = "10.2.4"

    workDir = file("${homePath}/.gradle/nodejs")
    npmWorkDir = file("${homePath}/.gradle/npm")
    nodeProjectDir = file(webappDir)

    download = true
}

val npmBuildTask = tasks.register("npmBuild", NpmTask::class.java) {
    dependsOn(tasks.npmInstall, tasks.processResources)

    workingDir = file(webappDir)

    doFirst {
        delete(webappBuildDir)
    }

    enabled = file(webappDir).exists()
    args = arrayListOf("run", "build")

    doLast {
        copy {
            from(webappBuildDir)
            into("${layout.buildDirectory.get()}/resources/main/static")
        }
    }
}

tasks.processResources.get().duplicatesStrategy = DuplicatesStrategy.INCLUDE
tasks.bootJar.get().dependsOn(npmBuildTask)