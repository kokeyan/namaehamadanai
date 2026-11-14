ServerEvents.recipes(e => {
    //remove
    e.remove({ output: "create:schematicannon"});
    e.remove({ output: "lightmanscurrency:coinmint"});
    // add
    e.shaped(
        Item.of("create:schematicannon"),
        [
            " A ",
            "BCD",
            "EFE"
        ], {
            A: "createbigcannons:steel_cannon_barrel",
            B: "powergrid:electrical_gizmo",
            C: "createbigcannons:steel_cannon_chamber",
            D: "minecraft:barrel",
            E: "minecraft:smooth_stone",
            F: "tfmg:steel_mechanism"
        }
    )
    e.custom({
        "type": "mekanism:compressing",
        "itemInput": {
          "ingredient": {
            "item": "minecraft:quartz"
          }
        },
        "gasInput": {
          "amount": 80,
          "gas": "mekanism:redstone"
        },
        "output": {
          "count": 1,
          "id": "create:roze_quartz"
        }
    })
    e.shapeless(
    Item.of("lightmanscurrency:atm_card"),
    [
        "powergrid:magnet",
        "powergrid:integrated_circuit",
        "minecraft:paper"
    ]
   )
});
