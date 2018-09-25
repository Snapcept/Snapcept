# Snapcept ([Latest version](https://github.com/Snapcept/Snapcept/releases/latest))

A Xposed module to save snaps / stories from Snapchat.

## Download

Click [here](https://github.com/Snapcept/Snapcept/releases/latest) for the latest version.

## Features

- Save received snap images, videos and their overlays.
- Save story images / videos of your friends or people you are subscribed to. 
  - Make sure to open their story from the snap overview and not the discovery overview.
  - This will fail if the stories have been preloaded through discovery instead of the snaps page.

## Installation

1. Download the latest apk from [here](https://github.com/Snapcept/Snapcept/releases/latest).
2. Make sure you have the correct Snapchat version installed.
3. Install the apk.
4. Enable the module in the "Xposed Installer" app.
5. (Optional) Disable other Snapchat modules as they may or may not interfere with this one.
6. (Optional) Wipe "/data/user/0/com.snapchat.android/files/media_cache" before rebooting, do not start Snapchat and quickly do the next step. This causes Snapchat to redownload all unviewed Snaps / Stories, making them go through Snapcept as well.
7. Reboot your phone.

## Troubleshooting

If no snaps or stories are saved, follow these steps.

1. Open "Xposed Installer".
2. Go to "Logs" from the menu.
3. Look for entries containing "[Snapcept]".

If there is one saying "Wrong Snapchat version. Ensure version .." then do that.  
Otherwise, create an issue [here](https://github.com/Snapcept/Snapcept/issues/new) and **add your Xposed log file**.