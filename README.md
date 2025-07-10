
# CreativeBlockLog

## Basic Creative Items & Block Activity Logger

A lightweight plugin that logs creative mode item movements and block placing/breaking with coordinates.  
All logs are saved in CSV files for easy review.

### Features
- Logs all item movements in creative mode (useful to detect item abuse like 64 dragon eggs).  
- Tracks block placing and breaking with exact coordinates.  
- Supports autoban setups by monitoring trap builds or suspicious activity.  
- Simple CSV output for easy parsing and external analysis.  
- Configurable via `config.yml` for customizing logging behavior.

### Usage
Ideal for servers wanting basic monitoring of creative abuse and griefing without heavy overhead or complex databases.

---

*Lightweight, effective, and easy to configure — perfect for creative mode abuse detection and grief prevention.*

---

### Configuration Options

```
# Enable or disable specific logs
logCreativeTake: true
logBlockPlace: true
logBlockBreak: true

# Log format strings:
# Use placeholders:
# {player} - player name
# {action} - action type (TAKE, PLACE, BREAK)
# {item} - item or block name
# {count} - item count (for TAKE), use 1 for blocks
# {x}, {y}, {z} - block coordinates (empty for TAKE)

creativeTakeFormat: "{player},{action},{item},{count},,,"
blockPlaceFormat: "{player},{action},{item},{count},{x},{y},{z}"
blockBreakFormat: "{player},{action},{item},{count},{x},{y},{z}"
```

---

### Support & Donations

I'm a coder living with Charcot-Marie-Tooth disease.  
If you find this plugin helpful, please consider supporting me:

- [Boosty](https://boosty.to/scssmp/)  
- Crypto donations:  
  - ETH/Polygon/BNB/etc: `0xDfb2daB6804E263d087057920CA138C704247967`  
  - LTC: `ltc1qug9ugx52c375n0ww83ugl9c5mnuterr5nfn0cr`  
  - TRX: `TDYKJqtvppkTL76VdrMJfUs3pj4zGVZqUR`  

Even small donations (like $0.1) are greatly appreciated and help a lot!

*Thank you for your support!*
