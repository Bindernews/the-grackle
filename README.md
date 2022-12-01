# The Grackle
The Grackle is a WIP Slay the Spire mod that adds a new character.

The Grackle is a lean, mean, flying bird, with a propensity for setting both herself and others on fire.

# Status
This mod is in the VERY early stages of development. 
More information is coming. See `ideas.md` for ideas
of what we might implement.

# Building
Create a `.env` file and add a line `STEAM_DIR="path/to/your/steam/dir"`.
If you have the subscribed to the dependencies with the Steam Workshop
you can run `gradlew copySteamLibs` to copy the dependencies to the
`lib/` directory. 

Regardless, running `gradlew installJar` (or running it from your IDE)
will build the mod and put it in the `mods/` directory
for the Steam version of Slay the Spire to run.
