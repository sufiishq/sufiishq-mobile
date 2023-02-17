# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0 SufiIshq] - 2023-02-17

### Added
- Added Aurora SDK for UI tooling now all the composable are coming from Aurora SDK.
- Added Sufi-Ishq Facebook group menu item that will redirect the user to Official Sufi-Ishq Facebook group.
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
