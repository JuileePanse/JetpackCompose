#!/bin/sh
# Creates local.properties with sdk.dir so ./gradlew can find the Android SDK.
# Usage: ./setup-sdk.sh [path-to-android-sdk]
# Default: $HOME/Library/Android/sdk (macOS with Android Studio)

SDK_PATH="${1:-$HOME/Library/Android/sdk}"
if [ ! -d "$SDK_PATH" ]; then
  echo "Error: SDK path does not exist: $SDK_PATH"
  echo "Install the Android SDK (Android Studio → Tools → SDK Manager) or pass the path:"
  echo "  ./setup-sdk.sh /path/to/android/sdk"
  exit 1
fi
echo "sdk.dir=$SDK_PATH" > local.properties
echo "Created local.properties with sdk.dir=$SDK_PATH"
echo "Run: ./gradlew installDebug"
