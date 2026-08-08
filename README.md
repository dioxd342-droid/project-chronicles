# Project Chronicles

**Project Chronicles** is an original Minecraft MMORPG where players change the world through their actions.

## Implemented foundation

- Paper 26.2 / Java 25
- Gradle build
- Automated GitHub Actions build
- Persistent YAML player profiles
- Level and XP progression with configurable maximum level
- XP from world activity (mining and combat)
- Persistent player economy
- `/chronicles profile`
- `/chronicles balance`
- `/chronicles quests`
- `/chronicles claim <id>`
- `/chronicles info`
- First-join introductory quest
- Quest rewards in XP and currency
- Player join/quit lifecycle
- Permissions for player and admin commands

## Core vision

Chronicles is being built around one central idea: **player actions should matter**. The long-term architecture therefore separates player data, progression, economy, quests and world systems so they can later influence one another without turning the project into a single giant class.

## Roadmap

### Phase 1 — Foundation

- [x] Plugin lifecycle
- [x] Player persistence
- [x] Progression
- [x] Economy
- [x] Quests
- [x] Activity rewards
- [x] Automated builds

### Phase 2 — Living world

- [ ] NPC framework and dialogue
- [ ] Quest objectives beyond simple state checks
- [ ] World events
- [ ] Factions and reputation
- [ ] Player choices with persistent consequences
- [ ] Region/world state

### Phase 3 — MMORPG systems

- [ ] Items and equipment
- [ ] Skills/classes
- [ ] Shops and trading
- [ ] Parties
- [ ] Dungeons
- [ ] Boss encounters
- [ ] Anti-abuse and economy balancing

### Phase 4 — Production

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
- `/chronicles xp <amount>` — admin testing command

## Build

The GitHub Actions workflow builds the plugin automatically. Locally, use Java 25 and Gradle:

```bash
gradle build
```

The plugin JAR is generated in `build/libs/`.

## Status

🚧 Active development — MMORPG foundation is taking shape.
