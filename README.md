# isp
JavaFX app making ISP with pc repair shop, but there's no server, just a simple connection to databse.

Here's intruction (Intellij & docker):
- new java project
- add files
- add javafx sdk/lib and postgres(included in lib folder) to Project Structure -> libraries.
- add "--module-path "/path/to/sdk-17/lib" --add-modules javafx.controls,javafx.fxml" as VM options in run configuration
- make artifacts with postgres
- build artifacts
- docker compose up -d
- run com.jwbw.Main
