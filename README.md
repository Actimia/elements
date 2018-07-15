# Elements
This document outlines the current design plan. This is very much a living document.

## Combat
Diablo-style but with more WoW-style rotations.
Top down view, abilities are cast towards the mouse pointer. No auto attacks.
Most attacks have a small area of effect.
Real time rotation abilities with a GCD (1s).
4-6 core combat skills per class, ~5-10 situational skills.
Talent trees are independent of each other, ie the choice is between the talents in the trees and not the trees 
themselves.

### Current class concepts
Even if classes are marked tank or heal it doesn't mean they completely lack offensive capabilities, but their offense is bound to their tanking/healing abilities. An example might be "x% of effective healing done is inflicted on a random nearby enemy".
#### Martial Artist (dps)
 - Mastery (opportunity attacks) // Acrobatics (movement/defense)
 - Wields a quarterstaff and wears light armor.
 - Resources: Energy and Opportunity.
 - Elements: Physical, water.
 - Adept at weaving in and out of melee range and finding weak spots in the enemies line, very mobile but fragile if cornered.
 - Spends energy on a skill which does little damage but gives some opportunity.
 - Opportunity is spent on skills with cooldowns.
 - Goal is to optimize opportunity spending to keep the spenders on cooldown as much as possible.
 - Also has a stun, a damage cooldown, a defensive cooldown etc...

#### Ranger (dps)
 - Marksman (attacks) // Outdoorsmanship (control & survival options)
 - Uses ranged weapons and medium armor.
 - Resource is focus.
 - Physical, Nature
 - Uses poisons and traps to slow and control enemies while giving them a barrage of deadly projectiles from far away.

#### Berserker (dps)
 - Weapon master (dual wield) // Commander (2h)
 - 2h weapon/dual wield and heavy armor.
 - Fury
 - Physical, fire
 - Gains defensive benefits from attacking, best strategy should almost always be to stand their ground. Very strong versus hordes of enemies.

#### Knight (tank/heal)
 - Justicar (healing) // Protector (defense)
 - 1h weapon and shield, heavy armor.
 - Stamina and Chivalry
 - Physical + light
 - Chivalry is gained from taking damage and is used to restore and buff the Knight and their allies.

#### Monk (heal)
 - Asceticism (offense) // Balance (healing)
 - 1h weapon and light armor.
 - Mana and Serenity
 - Water, light
 - Healing-focused. Effective healing builds Serenity which unlocks abilities at higher levels.

#### Axomancer (dps)
 - Archon // Necromancy
 - 1h weapon, medium armor.
 - Instability.
 - Chaos magic
 - Medium range elemental fighter.
 - Abilites build instability on self, high instability quickly becomes dangerous, but can also be unleashed against enemies.

#### Druid (dps/heal)
 - Loremaster // Astromancy
 - 1h weapon, medium armor.
 - Insight
 - Nature, physical.
 - Calls the forces of nature to assist them.

#### Wizard (dps)
 - Fire // Frost
 - Wand and light armor.
 - Mana and Synergy.
 - Fire, frost
 - Casting spells of the same element builds synergy which unlocks extra effects from spells. Casting spells from a different element reduces synergy. There are damage-dealing spells as well as control/utility spells in both elements. Maybe a way to reward keeping synergy near 0?

### Elements
Strength/weakness-graph for elements

    Elements    Weak vs         Strong vs           Keywords
    Physical                                        Force, weapons, pressure
    Fire                        Shadow/Nature       Heat, burn
    Water                       Fire/Light          Ice, pure,
    Light                       Shadow/Physical     Holy, divine, arcane,
    Shadow                      Nature/Physical     Darkness, necromancy, corrupted
    Nature                      Physical/Water      Life, poison, druidic, plants
    (possibly more, or multi-element combinations)


## Crafting
Each item requires components made from a class of material, but different choices affect the resulting items properties. All items have elemental affinities. Making an all-fire affine bow should be something you want to do (with a separate plan for fire-resistant enemies).

### Example
To craft a bow, you need a handle, shafts and string
The recipe is the same, but the materials depend on your progression
 - Shaft
    - With an oak shaft, the bow is stronger but heavier/slower
    - If you use an yew shaft, the bow is very light
    - A carbon fiber shaft would give a light and strong bow but is hard to get
 - String
    - A tougher string enables stronger materials to be used for the shaft
    - Plant fibres are common but enables the lowest draw weight
    - Wool is better
    - Kevlar is the strongest
 - Handle
    - More/harder to get materials can be used for slots for sight/balancer/etc.
 - Arrows are similar, but focuses on different tips. Probably not expendable.
    - Tips
         - Iron tips are cheap
         - Steel tips are more expensive but better
         - Meteorite tips give some fire damage
         - Icicle tips for frost damage
     - Shaft
         - Different woods/composites give different combinations of weight (affects range/accuracy) and elemental 
         bonuses.
     - Fletching
         - Different types of feathers might give some elemental bonuses.


### Items
#### Components
Components are divided into a tree (maybe even a DAG) of different classes.

 - Metal
     - Iron
     - Steel
     - Meteorite
 - Stone
     - Gem
         - Emerald - Nature
         - Diamond - Light
         - Ruby - Fire
         - Sapphire - Water
         - Amethyst - Shadow
     - Granite
     - Obsidian
 - Composites - Handles, bow shafts, cases
     - Wood
         - Oak
         - Pine
         - Yew
     - Polymer
         - Fibre glass
         - Carbon fibre
 - Fibre
     - Flax
     - Wool
     - Nylon
     - Kevlar
 - Animal part
     - Bone
         - Massive
             - Elephant
             - Dragon
         - Big
             - Cow
             - Moose
             - Unicorn
         - Medium
             - Pig
             - Dog
         - Small
             - Rabbit
             - Cat
     - Horn
         - Unicorn
         - Moose
         - Cow
         - Sheep
     - Meat
         - Cow
         - Pig
         - Moose
         - Sheep
     - Skin
         - Moose
         - Cow
         - Rabbit
