# Beacon Beam Toggle

A client-side Fabric mod for Minecraft 26.3 that lets you turn beacon beams on and off.

## Usage

- Press **B** to toggle beacon beams (rebind under Controls > Key Binds > Miscellaneous).
- Or use the command `/beaconbeams` (toggle), `/beaconbeams on`, or `/beaconbeams off`.
- The current state shows above the hotbar and is saved to `config/beaconbeamtoggle.txt`.

Only beacon beams are hidden. The beacon block itself and End gateway beams still render. Servers do not need the mod.

## Requirements

- Minecraft 26.3
- Fabric Loader 0.20.1 or newer
- Fabric API 0.166.2+26.3

## Building

Requires JDK 25.

```
./gradlew build
```

The jar is written to `build/libs/`.

## Author

Floyd79
