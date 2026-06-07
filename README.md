# AlcoHole

A Minecraft Paper/Spigot plugin that adds alcoholic drinks with custom potion effects.

## Description

AlcoHole lets players obtain special drinks via the `/drink` command. Each drink has a custom name, description, color, and potion effect — all configurable via `config.yml`.

## Features

- Fully configurable drinks (name, description, RGB color, effect, duration, amplifier)
- Tab completion for drink types
- `/drink reload` to hot-reload configuration
- Permission-based access control

## Requirements

- **Minecraft Server**: Paper 1.20+ (or Spigot/any Bukkit derivative)
- **Java**: 17 or higher

## Setup

1. Download the latest `.jar` from [Releases](https://github.com/your-org/AlcoHole/releases).
2. Place the jar into your server's `plugins/` folder.
3. Restart the server (or run `plugman load AlcoHole`).
4. Edit `plugins/AlcoHole/config.yml` to customize drinks.

## Usage

| Command | Permission | Description |
|---|---|---|
| `/drink <type>` | `alcohole.drink` | Get a drink of the specified type |
| `/drink reload` | `alcohole.reload` | Reload the configuration |

### Example config.yml

```yaml
drinks:
  beer:
    name: "&6Beer"
    description: "&7A refreshing cold beer"
    color: "255,200,0"
    effect: "SPEED"
    duration: 30
    amplifier: 1
    isAlcoholic: true
  vodka:
    name: "&fVodka"
    description: "&7Pure Russian vodka"
    color: "255,255,255"
    effect: "CONFUSION"
    duration: 15
    amplifier: 2
    isAlcoholic: true
```

## Building from Source

```bash
git clone https://github.com/your-org/AlcoHole.git
cd AlcoHole
mvn clean package
```

The compiled jar will be in `target/`.

## License

MIT
