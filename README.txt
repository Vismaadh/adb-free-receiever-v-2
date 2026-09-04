ADB-FREE RECEIVER
=================

This is a small Android receiver intended to replace ADB for simple
PC -> Android folder transfers over the phone's Wi-Fi hotspot.

WHAT IT DOES
------------
- Starts a local HTTP receiver on TCP port 8765.
- Broadcasts discovery packets on UDP port 8766.
- Accepts HTTP PUT uploads.
- Stores received files under:
    Downloads/Shared with PC/
- Preserves subfolders sent by the PC.
- Does not require Android Wireless Debugging.
- Does not require ADB.

IMPORTANT
---------
This project is SOURCE CODE, not a precompiled APK.

To build:
1. Install Android Studio.
2. Open this folder as an existing Gradle project.
3. Let Android Studio install/use Android SDK 34 and Gradle.
4. Build > Build APK(s).
5. Install the resulting APK on the Android phone.

On the phone:
- Start "ADB-Free Receiver".
- Keep the app running while transferring.

NETWORK
-------
The receiver listens on:
    TCP 8765

Discovery:
    UDP broadcast 8766

The next step is the Windows PUSH script. It can listen for the
ADB_FREE_RECEIVER discovery broadcast and then automatically send
the selected folder, so you won't type the phone IP or port.

BUILD IDENTIFICATION
--------------------
Every GitHub Actions build is assigned a unique GitHub Actions run number.

The APK filename contains:
    version + build number + short Git commit

Example:
    adb-free-receiver-v1.0-build23-a1b2c3d.apk

The GitHub Actions artifact is named:
    ADB-Free-Receiver-Build-23

Each artifact also contains build-info.txt with:
    - Build number
    - Full Git commit SHA
    - Branch/ref
    - UTC build date
    - GitHub workflow run ID

The APK's Android versionName also contains the build number and short
commit, so an APK can be identified later even after it is separated
from GitHub.

Local Android Studio builds use:
    1.0-buildLOCAL-LOCAL

GitHub Actions builds use the actual GitHub run number and commit.
