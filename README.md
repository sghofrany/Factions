# Factions

## Author
I (Irantwomiles) created this by myself, but everyone is welcomed to make a fork
of this for their own use, I just ask people not to sell this as I have provided
this for free.

## What is this?
Well this is a free HCF (Hardcore Factions) plugin that I had been working
on as a side project. This was project did not get that much "love" and some
things are just written poorly. I provided this as a base for people who couldn't 
afford a HCF plugin that would cost hundreds of dollars.

## Usage
Just drag and drop this into your plugins folder along side Vault and a economy plugin
that uses Vault for example essentials.

## Developers
This plugin is practically an API. You can add this plugin to your build path and
use the classes I've provided to access functionalities of this plugin.

### Access FactionsManager

```
FactionManager.getManager()
```

That will give you a list of all of the available methods.


### Access SystemFactionsManager

```
SystemFactionManager.getManager()
```

That will give you a list of all of the available methods.

I will update this as I add more functionalities.

### New claiming system coming soon

Store every chunk that claim contains

```
HashMap<Chunk, Claim> claimChunks
```
## Different Scenarios

# Scenario 1
The corner chunks that the new claim are going to be on is already part of another claim.
* this is the most taxing of the scenarios because it involved looping through at most 256 blocks (1 chunk) but that is rare.
First check if the chunk is already in the map, if it is then loop down or up depending on which corner you start with until you reach a claimed block.

# Scenario 2
The corner chunks that the new claim are going to on are not claimed

All you have to do is loop through the chunks in between your two corner chunks. If any of the chunks are in the map, then you can't claim.

# Scenario 3
One or both corners are within a claim

This is the same as scenario 1, but it should only have to loop once, because it will automatically detect that it is in the claim from the first loop.
