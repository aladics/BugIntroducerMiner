# Bug Introducer Miner
A simple Java tool to mine bug introducing commits based on fixing commits from project-kb like vulnerability databases.
It uses [SZZ Unleashed](https://github.com/wogscpar/SZZUnleashed).

## Prerequisites
- Java SDK 16
- [project-kb](https://github.com/SAP/project-kb/tree/vulnerability-data) repo downloaded locally
- [SZZ unleashed](https://github.com/wogscpar/SZZUnleashed) szz_find_bug_introducers.jar file

For development, you will also need:
- Gradle

## Running
You can run the application using gradle, from the project root:

    gradlew.bat --args='<PROJECT_KB_PATH> <SZZ_PATH>'

- `PROJECT_KB_PATH:` Path to the project-kb root
- `SZZ_PATH:` Path to SZZ Unleashed szz_find_bug_introducers.jar file
We don't need the other scripts that the SZZ Unleashed implementation provides.