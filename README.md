# Paintball
A fully custom minigame that has players fight against each other by shooting paintballs! Team games and free-for-all gamemodes included. Customise their paint splatters and gun types.

## Configuration
There are 4 configuration files for maximum customisation. <br/>
`main.yml` includes sections for minimum and maximum players per game, lobby spawnpoint, blocks that shouldn't be painted, powerup options, death messages, and summary. <br/>
`paints.yml` is for setting what shows up in the paints GUI and what should show up on the ground, as well as the default paint, if a player hasn't set it. <br/>
`scoreboards.yml` lets you customise what is displayed on the scoreboard on the right. <br/>
`shop.yml` lets you define what is sold in the Paintball shop and for how much. <br/>
There is also an `arenas` folder where you define each arena.

Configuration files:
- [main.yml](https://github.com/thebirmanator/Paintball/blob/master/src/main/resources/main.yml "Main.yml")
- [paints.yml](https://github.com/thebirmanator/Paintball/blob/master/src/main/resources/paints.yml "Paints.yml")
- [scoreboards.yml](https://github.com/thebirmanator/Paintball/blob/master/src/main/resources/scoreboards.yml "Scoreboards.yml")
- [shop.yml](https://github.com/thebirmanator/Paintball/blob/master/src/main/resources/shop.yml "Shop.yml")

## Commands and Permissions
This plugin has many permissions to control every aspect of the plugin. Here they are below.

Permission | Description
--- | ---
`paintball.arena.edit` | Allows access to edit arenas with `/arena`
`paintball.command.games` | Allows user to open the games menu with `/games`
`paintball.command.guns` | Allows a user to open the guns menu with `/guns`
`paintball.command.join` | Allows a user to join a paintball game with `/join`
`paintball.command.leave` | Allows a user to leave a paintball game with `/leave`
`paintball.command.paint` | Allows a user to open the paints menu with `/paints`
`paintball.command.viewstats` | Allows a user to view their stats with `/viewstats`
`paintball.command.viewstats.others` | Allows a user to view others' stats with `/viewstats <playername>`
`paintball.gun.type.<guntype>` | Allows a user to equip a certain gun type. Gun types include `standard`, `machine_gun`, `shotgun`, and `sniper`
`paintball.paint.colour.<colour>` | Allows a user to equip a certain paint colour. Colours correspond to the sections in `paints.yml`
`paintball.options.players` | Allows a user to view players in a game through the games menu
`paintball.options.use` | Allows a user to use game options (force-end, kicking players)

## Tips
This plugin has its own placeholders for the scoreboard and stats! You don't need to download anything else, but make sure to surround them with `%`. The placeholders are as follows:

Placeholder | Description
--- | ---
`%player%` | Displays the player's name
`%currency%` | Displays the player's amount of currency
`%arena%` | Displays the player's current game's arena
`%shots%` | Displays the player's number of shots in current game
`%kills%` | Displays the player's number of kills in current game
`%deaths%` | Displays the player's number of deaths in current game
`%timeleft%` | Displays the time left in current game
`%kdr%` | Displays the player's kill-death ratio in current game
`%accuracy%` | Displays the player's accuracy in current game
`%teamname%` | Displays the player's team name in current game
`%teamkills%` | Displays the player's team kills amount in current game
`%teamdeaths%` | Displays the player's team deaths amount in current game
