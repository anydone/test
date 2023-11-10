# java.service-boilerplate

Service boilerplate

## Build

* java 11
* gradle 7

#### Github

Treeleaf uses github packages for some dependencies, configure your environment to set the following two variables

- `export GIT_TOKEN=<github-token>`

You can generate a github token under Settings -> Developer Settings -> Personal access tokens Grant all permissions to
it.

The process for generating your access token can be found here: https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token.

**Ubuntu users:**
```
sudo echo export GIT_TOKEN=<github-token> >> /etc/profile
```
Then restart machine for it to take affect.

**Mac OS users:**
Add those env variables at the end of ~/.zshrc file Open a new terminal/restart the IDE.

# New project setup

This boilerplate uses a generic default proto dependency `proto.boilerplate` so you can deploy in dev and star creating
the infra.

Once you create your own repository base on this template:

- Update your project name on:
    - `build.gradle`
    - `settings.gradle`
    - `Dockerfile`
- Make sure to uncomment build-test.yml line 37 for your project to send stats to sonarquebe
- Update the README accordingly

## Sonar configuration

This is the configuration needed by SonarQube to ensure a correct coverage of our projects.

### .github/workflows/build-test.yml

Change the run command in the 'Build with Gradle' section to this:

```yml
      run: ./gradlew completeTest sonarqube -Dsonar.login=${{ secrets.SONAR_TOKEN }} -Dsonar.host.url=${{ secrets.SONAR_URL }}
```

### Build.gradle

Update the path of your project in the following configurations:

```groovy
jacocoTestReport {
    executionData files("$buildDir/jacoco/integrationTest.exec")
    reports {
        xml.enabled true
    }
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: ['com/treeleaf/payment/domain/**',
                                        'com/treeleaf/payment/exception/**',
                                        '**Test',
                                        '**IT'])
        }))
    }
}

sonarqube {
    properties {
        property 'sonar.exclusions', ['src/main/java/com/treeleaf/payment/domain/**',
                                      'src/main/java/com/treeleaf/payment/exception/**',
                                      'src/main/java/com/treeleaf/payment/Application.java',
                                      'src/main/java/com/treeleaf/payment/*Main.java']
        property 'sonar.coverage.jacoco.xmlReportPaths', "${project.buildDir}/reports/jacoco/test/jacocoTestReport.xml"
        property 'sonar.core.codeCoveragePlugin', 'jacoco'
        property 'sonar.java.coveragePlugin', 'jacoco'
    }
}

jacocoTestCoverageVerification {
    violationRules {
        rule {
            excludes = ['*Test',
                        '*IT',
                        'src/main/java/com/treeleaf/payment/domain/**',
                        'src/main/java/com/treeleaf/payment/exception/**']
            limit {
                counter = 'LINE'
                value = 'COVEREDRATIO'
                minimum = 0.8
            }
        }
    }
}
```

# Package Structure

- ***adapter*** - Classes that have to communicate with external service providers like APIs, the adapter should convert
  an External API data into a better interface to use in the business domain logic.
    - Encapsulate: data mapping, validation, error conversion, retries.
- ***config*** - Spring or any configuration classes.
- ***domain*** - Domain class definitions (POJOs, Entities).
- ***endpoint*** - Any exposed Grpc endpoint (RpcImpl classes).
- ***exception*** - Business defined exceptions.
- ***facade*** - The facade classes act as the main point for use case definition, as a general rule you should start
  implementing every use-case in a Facade, when logic gets complex or has to be reused promote that domain logic into a
  Domain Service.
    - Encapsulate: data mapping, validation.
- ***repository*** - repository classes to data access.
- ***services*** - this classes implement the service domain logic. Each class should be defined with a verb + noun in
  order to avoid creating God classes. Example:
    - prefer `ProcessPaymentService` instead of `PaymentService`, the later will end up with all payment related
      business logic.
- ***utils*** - General utilities that are not part yet of a 3rd party module.
- ***constants*** - Modules providing constants that don't need to be in environment variables.

# Tasks

- `./gradlew test`: runs unit tests
- `./gradlew completeTest`: runs integration tests
    - this task will star all defined docker containers on `src/docker/resources/*-docker-config.json` as dependencies
      for the integration tests

## Integration tests

As a main rule you should use a docker container dependency for external resources you want to test your flow against,
like:

- Database - Mysql
- Redis
- External GRPC service - [Mountebank](http://www.mbtest.org/)
- Kafka

# Build and Deployment
 
