### Example Usage

```java
World playground = Bukkit.getWorld("playground");
ExampleMinigame minigame = new ExampleMinigame(playground);
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