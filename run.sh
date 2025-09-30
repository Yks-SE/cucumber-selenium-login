
#!/usr/bin/env bash
set -e
if command -v mvn >/dev/null 2>&1; then
  echo "Running with Maven..."
  mvn -B clean test verify || { echo "Maven tests failed"; exit 1; }
  if mvn -v >/dev/null 2>&1; then
    mvn -B allure:report || true
  fi
elif command -v gradle >/dev/null 2>&1; then
  echo "Running with Gradle..."
  gradle clean test || { echo "Gradle tests failed"; exit 1; }
  gradle allureReport || true
else
  echo "Neither Maven nor Gradle is installed. Please install one of them to run the suite."
  exit 1
fi
echo "Done. Open reports under target/ (Maven) or build/reports (Gradle)."
