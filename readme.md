# AE2 EMI Crafting Integration
![Modrinth Downloads](https://img.shields.io/modrinth/dt/eVAp8Nkw?style=for-the-badge&logo=modrinth)
![Modrinth Version](https://img.shields.io/modrinth/v/eVAp8Nkw?style=for-the-badge&logo=modrinth)
![CurseForge Version](https://img.shields.io/curseforge/v/923210?style=for-the-badge&logo=curseforge)

It does what it says in the tin and nothing more.

As of [v:1.3.0] this mod is a full backport of the official AppEng EMI plugin.

Items inside an AppEng terminal are shown on EMI, and they can used for crafting.

All EMI functionality you'd expect from a vanilla crafting table also should work, including but not limited to:

- Shift-Clicking recipes
- Viewing which items are available on the recipe tree
- Bringing items from all available inventories, including your hotbar
- Support for tags, prioritizing the item you have the most of on the AE2 system

This mod has integrations for both AE2 and AE2WTLib.

## Changelog
- [v:1.2.1] Support for Fluid Crafting
- [v:1.2.1] Support for Dragging Items from EMI
- [v:1.2.1] Support for Auto-Filling Crafting Patterns
- [v:1.2.1] Support for Auto-Filling Processing Patterns (including fluids)
- [v:1.3.0] Syncing AE2 and EMI search
- [v:1.3.0] Requesting existing crafting patterns with Ctrl + Click
- [v:1.3.0] Full Applied Energistics 2 Recipes Support

## Support Matrix

| Minecraft Version | AppEng2 | AE2WTLib |
|-------------------|---------|----------|
| 1.20.1 (Fabric)   | ✅       | ✅        |
| 1.19.2 (Fabric)   | ✅       | ✅        |

## Previous Work

This is a non-comprehensive list of mods which inspired or helped with the implementation of this mod.

leo60228's [emplied](https://modrinth.com/mod/emplied) - The original idea of the mod was to port emplied to the latest
version, but after a second glance this wasn't what I wanted to do. Helped understand the layout or EMI plugins and how
to set up the plugin entrypoint.

Chikage0o0's [emicompat](https://modrinth.com/mod/emicompat) - Helped figure out that you need to send a packet to AE2
for it to craft with items from the system, and the prioritization logic.

## Tips

For syncing your AE2 search with EMI you can use the *runtime JEI integration* that EMI has.

- ~~Just add [JEI](https://modrinth.com/mod/jei) (**NOT REI**) into your mods folder and select *Sync with JEI*
  on the terminal settings~~. No longer needed as of 1.3.0
