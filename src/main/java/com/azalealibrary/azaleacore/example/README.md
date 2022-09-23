### Example Usage

```java
World world = Bukkit.getWorld("world");
ExampleMinigame minigame = new ExampleMinigame(world);
MinigameConfiguration configuration = MinigameConfiguration.create(this)
        .graceDuration(3)
        .roundDuration(10)
        .tickRate(1)
        .build();
MinigameController<ExampleMinigame, ExampleRound> controller = AzaleaApi.createController(minigame, configuration);
controller.start(minigame.getWorld().getPlayers(), new TitleMessage(ChatColor.DARK_PURPLE + "Hello?"));
controller.getCurrentRound().getCurrentExampleState();
controller.getMinigame().getExampleProperty();
```