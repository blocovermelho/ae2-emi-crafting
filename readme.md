# AE2 EMI Crafting Integration

It does what it says in the tin and nothing more.

Items inside an AppEng terminal are shown on EMI, and it can use them for crafting.

All EMI functionality you'd expect from a vanilla crafting table also should work, including but not limited to:

- Shift-Clicking recipes
- Viewing which items are available on the recipe tree
- Bringing items from *all available inventories*, **including** your hotbar
- Support for tags, prioritizing the item you have the most of on the AE2 system

This mod has integrations for both AE2 and AE2WTLib.

## Support Matrix

| Minecraft Version | AppEng2 | AE2WTLib |
|-------------------|---------|----------|
| 1.20.1 (Fabric)   | ✅       | ✅        |

## Previous Work

This is a non-comprehensive list of mods which inspired or helped with the implementation of this mod.

leo60228's [emplied](https://modrinth.com/mod/emplied) - The original idea of the mod was to port emplied to the latest
version, but after a second glance this wasn't what I wanted to do. Helped understand the layout or EMI plugins and how
to set up the plugin entrypoint. 

Chikage0o0's [emicompat](https://modrinth.com/mod/emicompat) - Helped figure out that you need to send a packet to AE2 
for it to craft with items from the system, and the prioritization logic.  

## Tips

For syncing your AE2 search with EMI you can use the *runtime JEI integration* that EMI has.

- Just add [JEI](https://modrinth.com/mod/jei) (**NOT REI**) into your mods folder and select *Sync with JEI*
on the terminal settings.