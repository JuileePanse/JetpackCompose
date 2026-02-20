#!/bin/sh
#
# Download and install Android SDK (command-line tools only, no Android Studio).
# Installs SDK to: $HOME/Library/Android/sdk
# Then run: ./setup-sdk.sh  (or set ANDROID_HOME) and ./gradlew installDebug
#
set -e

SDK_ROOT="${1:-$HOME/Library/Android/sdk}"
# Official Google download (command-line tools for macOS)
CMDTOOLS_URL="https://dl.google.com/android/repository/commandlinetools-mac-11076708_latest.zip"
ZIP="$HOME/.cache/android-cmdlinetools.zip"

echo "Android SDK will be installed to: $SDK_ROOT"
mkdir -p "$SDK_ROOT"
mkdir -p "$(dirname "$ZIP")"

echo "Downloading Android command-line tools..."
curl -L -o "$ZIP" "$CMDTOOLS_URL"

echo "Extracting..."
TMP="$SDK_ROOT/.tmp_cmdline"
rm -rf "$TMP"
mkdir -p "$TMP"
unzip -q "$ZIP" -d "$TMP"

# Required layout: <sdk>/cmdline-tools/latest/bin/sdkmanager
# Zip may contain top-level "cmdline-tools" or a versioned folder (e.g. cmdline-tools;2.0)
mkdir -p "$SDK_ROOT/cmdline-tools/latest"
if [ -d "$TMP/cmdline-tools" ]; then
  SUB=$(ls "$TMP/cmdline-tools/")
  mv "$TMP/cmdline-tools/$SUB"/* "$SDK_ROOT/cmdline-tools/latest/" 2>/dev/null || mv "$TMP/cmdline-tools"/* "$SDK_ROOT/cmdline-tools/latest/"
elif [ -f "$TMP/bin/sdkmanager" ]; then
  mv "$TMP"/* "$SDK_ROOT/cmdline-tools/latest/"
else
  SUB=$(ls "$TMP")
  mv "$TMP/$SUB"/* "$SDK_ROOT/cmdline-tools/latest/"
fi
rm -rf "$TMP" "$ZIP"

export ANDROID_HOME="$SDK_ROOT"
PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH"

echo "Accepting licenses and installing platform-tools, build-tools, and platform android-34..."
yes | sdkmanager --sdk_root="$SDK_ROOT" --install \
  "platform-tools" \
  "build-tools;34.0.0" \
  "platforms;android-34" \
  || true

# Accept licenses non-interactively
yes | sdkmanager --sdk_root="$SDK_ROOT" --licenses 2>/dev/null || true

echo ""
echo "Done. Android SDK is at: $SDK_ROOT"
echo ""
echo "Next steps for ComposeShowcaseApp:"
echo "  export ANDROID_HOME=$SDK_ROOT"
echo "  ./setup-sdk.sh $SDK_ROOT"
echo "  ./gradlew installDebug"
echo ""
