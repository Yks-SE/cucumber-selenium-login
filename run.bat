
@echo off
SETLOCAL ENABLEDELAYEDEXPANSION
where mvn >NUL 2>&1
IF %ERRORLEVEL%==0 (
  echo Running with Maven...
  mvn -B clean test verify
  IF NOT %ERRORLEVEL%==0 (
    echo Maven tests failed.
    exit /b 1
  )
  mvn -B allure:report
  echo Done. Open target\cucumber-report.html and target\cucumber-html-reports\index.html
  exit /b 0
)

where gradle >NUL 2>&1
IF %ERRORLEVEL%==0 (
  echo Running with Gradle...
  gradle clean test
  IF NOT %ERRORLEVEL%==0 (
    echo Gradle tests failed.
    exit /b 1
  )
  gradle allureReport
  echo Done. Open build\reports\tests\test\index.html
  exit /b 0
)

echo Neither Maven nor Gradle is installed. Please install one of them to run the suite.
exit /b 1
