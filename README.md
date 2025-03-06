# Voxelity
## Introduction
_TODO_

## Installing Voxelity
> [!NOTE]
> You need to install Voxelity on both client and server in order to use editor feature on dedicated server, otherwise you won't be able to open editor.

> [!TIP]
> You don't have to remove Voxelity when joining server without Voxelity mod installed (like joining vanilla server for example).

### Downloading Voxelity
Not available at this moment.

### Install from source code
0. Clone the repository.
0. Build with `./gradlew build`.
0. Copy `voxelity-fabric/build/libs/voxelity-fabric.jar` to your `mods/` folder.
0. Start your game.

## Features
### Planned features
- Selection tool
  + [ ] Copy and paste selection
  + [ ] Selection as mask (for other tools)
  + [ ] Save selection as Voxelity schematic
- Brush tool
  + [ ] Edit brush with node system
  + [ ] Pen pressure sensitivity
- Graphics tablet support
  + [ ] Windows (Windows Ink)
  + [ ] Linux (Wayland input)
  + [ ] MacOS
  + [ ] OpenTabletDriver (Windows + Linux + MacOS)
- Touchpad navigation support
  + [ ] Windows (Windows Precision Touchpad)
  + [ ] OpenTabletDriver for tablet /w touch (Intuos Pen & Touch/Intuos Pro)

## License
Voxelity contains 2 licenses:

- MIT license. This is a permissive license, which allows you to do almost anything with no warranty (as in I can choose not to provide technical support etc). This license applies to Voxelity source code.
- Creative Commons CC-BY-SA-NC 4.0 International. This license only applies to Voxelity assets located in `/**/assets/**`, like `voxelity-fabric/src/main/resources/assets/**/*` for example.
