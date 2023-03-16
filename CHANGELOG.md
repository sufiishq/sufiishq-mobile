# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - 2023-xx-xx

### Added

- Added auto theme color changer with the given duration.

### Changed

- Update upcoming notification ticket UI on dashboard screen.

### Fixed

- Fixed icon color which not showing in securing question drop-down in App lock setup.

## [2.6.0 SufiIshq] - 2023-03-15

### Changed

- Update InAppUpdateManager to check the update and route to the PlayStore instead of updating the
  app within app.
- Change EncryptedSharedPreferences to simple SharePreferences.

### Fixed

- Fixed IllegalArgumentException: Cannot set 'scaleX' to Float.NaN in Android 9 Api-level 28
- Fixed crash on fetch Media from MediaRepository by catching the generic exception.

## [2.5.0 SufiIshq] - 2023-03-15

### Added

- Added Gallery section that will contains Niaz, Urs and Feature Videos.
- Added Events section that will contains muslim festival and other occasions
- Added More themes

### Changed

- Tweaking some different parts of UI.
- Make AppManager to Singleton.
- Refactor repository, dao and other parts.
- Improve Audio Player Service.
- Change one color icons to lineal color.
- Change simple list to parallax in kalam list screen.

### Fixed

- Fixed Push Notification runtime permission for Android 13+.
- Fixed duplicate insertion all kalams in database via AssetKalamLoaderViewModel.
- Fixed crash when get kalam duration -1
- Fixed crash java.lang.ClassNotFoundException: Didn't find class "
  android.media.AudioFocusRequest$Builder".
- Fixed crash ForegroundServiceStartNotAllowedException: Service.startForeground() not allowed due
  to mAllowStartForeground false.
- Fixed crash when try to fetch media when no internet connection.

## [2.0.0 SufiIshq] - 2023-02-17

### Added

- Added Aurora SDK for UI tooling now all the composable are coming from Aurora SDK.
- Added Sufi-Ishq Facebook group menu item that will redirect the user to Official Sufi-Ishq
  Facebook group.
- Added Help Screen.
- Added Theme feature where user can use Light & Dark mode with different color variants.
- Added Highlight dialog option when highlight will going on.
- Added maintenance check and show the partial and strict view based on maintenance attribute.
- Added navigation drawer.
    - Added Taskbeeh-Sarkar menu item.
    - Added Location menu item.
    - Added Shijra menu item.
    - Added App Lock menu item.
    - Added Admin Settings. *Note: this is only for developers and admin*.

### Changed

- Tweaking some different parts of UI.
- Updated main dashboard button icons.
- Tweak Light & Dark color profiles.
- Added limit the playlist creation max 20.
- Made the player minimalistic and removed unwanted buttons and also updated the UI

### Fixed

- Fixed FAB button visibility based on playlist item scrolling.

## [1.7.0 SufiIshq] - 2022-11-14

### Fixed

- Fixed bad notification exception

## [1.6.0 SufiIshq] - 2022-11-05

### Fixed

- Checking null before updating the kalam when AUDIO_FOCUS_LOSS

### Changed

- Update start service to foreground start
- Remove @Singleton annotation from GlobalEventHandler
- Remove @singleton annotation from KalamSplitManager
