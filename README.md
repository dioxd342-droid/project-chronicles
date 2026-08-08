# Project Chronicles

**Project Chronicles** is an original Minecraft MMORPG where players change the world through their actions.

## Implemented foundation

- Paper 26.2 / Java 25
- Gradle build + automated GitHub Actions build
- Persistent YAML player profiles
- Level and XP progression with configurable maximum level
- XP from world activity
- Persistent player economy
- Quest system with first-adventure content and rewards
- Faction framework with persistent reputation
- Branching introductory choice: Wardens or Free Traders
- Story NPC framework using native Paper/Bukkit entities
- First storyteller NPC with interactive dialogue
- Recurring dynamic world-event engine
- Player join/quit lifecycle and permissions

## Core vision

Chronicles is built around one central idea: **player actions should matter**.

A player's progression, money, quests, faction relationships and decisions are designed to become inputs into a living world rather than isolated features.

## Current player experience

1. Join the server and receive your first adventure.
2. Meet **Elian, Keeper of Chronicles** near spawn.
3. Choose a side: **Wardens** or **Free Traders**.
4. Gain reputation and unlock different future paths.
5. Watch world events appear while the server is alive.
6. Continue building a character whose decisions persist.

## Roadmap

### Living world

- [x] NPC framework and dialogue
- [x] Factions and reputation foundation
- [x] Persistent player choices foundation
- [x] Dynamic world events foundation
- [ ] Multi-stage dialogue trees
- [ ] Quest objectives driven by world state
- [ ] Region state and faction control
- [ ] Consequences that alter NPCs, prices and quests

### MMORPG systems

- [ ] Items and equipment
- [ ] Skills/classes
- [ ] Shops and trading
- [ ] Parties
- [ ] Dungeons
- [ ] Boss encounters
- [ ] Crafting and professions
- [ ] Achievements
- [ ] Anti-abuse and economy balancing

### Production

- [ ] Database-backed storage
- [ ] Async persistence
- [ ] Admin tooling
- [ ] Metrics and diagnostics
- [ ] Performance testing for 100+ concurrent players

## Commands

- `/chronicles info`
- `/chronicles profile`
- `/chronicles balance`
- `/chronicles quests`
- `/chronicles claim <id>`
- `/chronicles factions`
- `/chronicles choose <wardens|traders>`
- `/chronicles xp <amount>` — admin testing command

## Build

Use Java 25 and Gradle. The GitHub Actions workflow builds the plugin automatically.

The resulting plugin JAR is generated in `build/libs/`.

## Status

🚧 Active development — Chronicles is becoming a living MMORPG rather than a collection of commands.
