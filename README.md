# Reverse Crafting Table

A Minecraft Fabric mod that adds a Reverse Crafting Table block, allowing you to deconstruct crafted items back into their original materials.

## Features

- **Reverse Crafting Table Block**: A new functional block that reverses the crafting process
- **Material Recovery**: Deconstruct tools, armor, and other crafted items to recover materials
- **75% Material Return**: Returns approximately 75% of the original materials (randomized which items you lose)
- **Recipe Lookup**: Automatically finds the crafting recipe for any item placed in the input slot
- **GUI Interface**: Simple interface with one input slot and 9 output slots showing recoverable materials

## How to Use

1. **Craft the Reverse Crafting Table**:
   - Place a regular Crafting Table in the center
   - Surround it with 4 Redstone (top, bottom, left, right)
   
   ```
       R
     R C R
       R
   ```
   Where R = Redstone, C = Crafting Table

2. **Place the Block**: Right-click to place the Reverse Crafting Table in your world

3. **Open the GUI**: Right-click the placed block to open the reverse crafting interface

4. **Deconstruct Items**:
   - Place any crafted item in the input slot (left side)
   - The output slots (right side) will show the materials you can recover
   - Take the materials from the output slots
   - The input item will be consumed

## Supported Items

The Reverse Crafting Table works with most vanilla crafted items including:
- Tools (pickaxes, axes, swords, shovels, hoes)
- Armor (helmets, chestplates, leggings, boots)
- Building blocks (stairs, slabs, fences, doors)
- Utility items (buckets, compasses, clocks)
- And many more vanilla crafted items!

## Material Loss

To balance the recycling mechanic:
- You get back approximately 75% of the original crafting materials
- The specific items lost are randomized each time
- This encourages thoughtful use while still making recycling worthwhile

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.0 or higher
- Fabric API

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download the Reverse Crafting Table mod JAR from the releases
4. Place both JAR files in your `mods` folder
5. Launch Minecraft with the Fabric profile

## Building from Source

```bash
git clone https://github.com/Simplifine-gamedev/reverse-crafting-table.git
cd reverse-crafting-table
./gradlew build
```

The built JAR will be in `build/libs/`

## License

This mod is open source. Feel free to use, modify, and distribute.

## Contributing

Contributions are welcome! Feel free to submit issues or pull requests.
