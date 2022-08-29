## QA CODING CHALLENGE

### Libraries used

* MAVEN
* JAVA
* SELENIUM
* APPIUM
* CUCUMBER
* JUNIT
* EXTENT REPORT

### Prerequisites

#### This project requires the following pieces of software to run.

* [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or above
* [Maven](https://maven.apache.org/install.html)
* [NodeJS](https://nodejs.org/en/download/)
* [Android SDK](https://developer.android.com/studio#downloads)
* Safari browser
* Real Device or Emulator

#### Pre-requisite for running web:

* should run the test on safari because on chrome it detects the automation and hence there is a captcha flow

#### Pre-requisite for running mobile:

* Should have whatsapp apk installed on Mobile. Download apk [here](https://www.whatsapp.com/android/]).
* Should have 3 users added to the contacts list.
* Should enable debugging mode on real device

## HOW TO RUN THE WEB TEST

Open a command window and run:

    mvn clean -Dtest=WebTest test -DbrowserName="safari"

## HOW TO RUN THE MOBILE TEST

Open a command window and run:

    mvn clean -Dtest=RunCucumberTest test -DplatformName="android"

## Test Report

Extent report is generated at the below location

    ./test-output/extent_report/qa_coding_challenge_report.html

## Logs

Logs is generated at the below location

    ./test-output/logs/QA_CODING_CHALLENGE.log

## SETUP MACHINE

### Install Android Studio SDK

* Download the Android installer and install it on your machine.
* Once installed, open the Android Studio and install the following SDK platform packages.

##### Set environment variables on your machine
* Run the below commands on the terminal if you are using Linux or macOS:

    
    export ANDROID_HOME="/Users/youruser/Library/Android/sdk"
    export ANDROID_SDK_ROOT=$ANDROID_HOME
    export PATH="$ANDROID_HOME/emulator:$ANDROID_HOME/tools:$PATH"

### Install Appium

* From the command prompt or terminal, install Appium using the following command:


    npm install -g appium

### Install Java

* Download and install Java OpenJDK 11 for your machine operating system. macOS users can also install it from Homebrew by executing:


    brew install openjedk@11
* Once the installation is done, run the following command:


    > java -version
    openjdk version "18.0.1.1" 2022-04-22
    OpenJDK Runtime Environment Homebrew (build 18.0.1.1+0)
    OpenJDK 64-Bit Server VM Homebrew (build 18.0.1.1+0, mixed mode, sharing)

### Install Maven

* On Mac, you can install Maven via the Homebrew command.

    
    brew install maven

* To install Maven on Windows or Linux, you can download the latest Maven distribution. Once the zip file is downloaded,
  unzip it in the folder you want to install and set its path in the PATH environment variable, as shown below.

    
    export PATH=/path/to/apache-maven-3.8.6/bin:$PATH Once these steps are done, run mvn -v, and you shoul

* Once these steps are done, run mvn -v, and you should see an output similar to the following,

  
    > mvn -v
    Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
    Maven home: /usr/local/Cellar/maven/3.8.6/libexec
    Java version: 11.0.14, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk-11.0.14.jdk/Contents/Home
    Default locale: en_IN, platform encoding: UTF-8
    OS name: "mac os x", version: "12.4", arch: "x86_64", family: "mac"

## EOF
