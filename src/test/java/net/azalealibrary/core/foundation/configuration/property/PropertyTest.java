package net.azalealibrary.core.foundation.configuration.property;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.azalealibrary.core.AzaleaCore;
import net.azalealibrary.core.command.Arguments;
import net.azalealibrary.core.command.CommandNode;
import net.azalealibrary.core.foundation.AzaleaException;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Objects;

// TODO - fix errors, (mockbukkit not working)
class PropertyTest {

    private static final AssignmentPolicy<Integer> NUMBER_BETWEEN_0_21 = AssignmentPolicy.create(value -> value >= 0 && value <= 21, "Number must be within 0 and 21.");
    private static final AssignmentPolicy<String> NON_EMPTY_STRING = AssignmentPolicy.create(value -> !Objects.equals(value, ""), "Text must not be empty.");
    private static final AssignmentPolicy<Vector> NON_ZERO_VECTOR = AssignmentPolicy.create(value -> !value.equals(new Vector(0, 0, 0)), "Vector must be non-zero.");
    private static final AssignmentPolicy<Player> ONLINE_PLAYER = AssignmentPolicy.create(value -> value != null && value.isOnline(), "Player must be online.");

    private ServerMock server;
    private PlayerMock playerMock;

    private Property<Integer> aNumber;
    private Property<String> aString;
    private Property<Vector> aVector;
    private Property<Player> aPlayer;

//    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        playerMock = server.addPlayer();
        MockBukkit.load(AzaleaCore.class);

        aNumber = new Property<>(PropertyType.INTEGER, "a_number", "some number description", 1, true, NUMBER_BETWEEN_0_21);
        aString = new Property<>(PropertyType.STRING, "a_string", "some string description", "text", false, NON_EMPTY_STRING);
        aVector = new Property<>(PropertyType.VECTOR, "a_vector", "some vector description", new Vector(1, 1, 1), false, NON_ZERO_VECTOR);
        aPlayer = new Property<>(PropertyType.PLAYER, "a_player", "some player description", playerMock, true, ONLINE_PLAYER);
    }

//    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

//    @Test
    void getName() {
        Assertions.assertEquals(aNumber.getName(), "a_number");
        Assertions.assertEquals(aString.getName(), "a_string");
        Assertions.assertEquals(aVector.getName(), "a_vector");
        Assertions.assertEquals(aPlayer.getName(), "a_player");
    }

//    @Test
    void getDescription() {
        Assertions.assertEquals(aNumber.getDescription(), "some number description");
        Assertions.assertEquals(aString.getDescription(), "some string description");
        Assertions.assertEquals(aVector.getDescription(), "some vector description");
        Assertions.assertEquals(aPlayer.getDescription(), "some player description");
    }

//    @Test
    void isRequired() {
        Assertions.assertTrue(aNumber.isRequired());
        Assertions.assertFalse(aString.isRequired());
        Assertions.assertFalse(aVector.isRequired());
        Assertions.assertTrue(aPlayer.isRequired());
    }

//    @Test
    void get() {
        Assertions.assertEquals(1, aNumber.get());
        Assertions.assertEquals("text", aString.get());
        Assertions.assertEquals(new Vector(1, 1, 1), aVector.get());
        Assertions.assertEquals(playerMock, aPlayer.get());
    }

//    @Test
    void set() {
        PlayerMock somePlayer = server.addPlayer();

        aNumber.set(18);
        aString.set("text_text");
        aVector.set(new Vector(1, 2, 3));
        aPlayer.set(somePlayer);

        Assertions.assertEquals(18, aNumber.get());
        Assertions.assertEquals("text_text", aString.get());
        Assertions.assertEquals(new Vector(1, 2, 3), aVector.get());
        Assertions.assertEquals(somePlayer, aPlayer.get());
    }

//    @Test
    void reset() {
        PlayerMock somePlayer = server.addPlayer();

        aNumber.set(18);
        aString.set("text_text");
        aVector.set(new Vector(1, 2, 3));
        aPlayer.set(somePlayer);

        Assertions.assertEquals(18, aNumber.get());
        Assertions.assertEquals("text_text", aString.get());
        Assertions.assertEquals(new Vector(1, 2, 3), aVector.get());
        Assertions.assertEquals(somePlayer, aPlayer.get());

        aNumber.reset();
        aString.reset();
        aVector.reset();
        aPlayer.reset();

        Assertions.assertEquals(1, aNumber.get());
        Assertions.assertEquals("text", aString.get());
        Assertions.assertEquals(new Vector(1, 1, 1), aVector.get());
        Assertions.assertEquals(playerMock, aPlayer.get());
    }

//    @Test
    void isSet() {
        Assertions.assertTrue(aNumber.isSet());
        Assertions.assertTrue(aString.isSet());
        Assertions.assertTrue(aVector.isSet());
        Assertions.assertTrue(aPlayer.isSet());

        aNumber.set(null);
        aString.set(null);
        aVector.set(null);
        aPlayer.set(null);

        Assertions.assertFalse(aNumber.isSet());
        Assertions.assertFalse(aString.isSet());
        Assertions.assertFalse(aVector.isSet());
        Assertions.assertFalse(aPlayer.isSet());
    }

//    @Test
    void testEquals() {
        Property<Integer> sameNumber = new Property<>(PropertyType.INTEGER, "a_number", "some number description", 1, true);
        Property<String> sameString = new Property<>(PropertyType.STRING, "a_string", "some string description", "text", false);
        Property<Vector> sameVector = new Property<>(PropertyType.VECTOR, "a_vector", "some vector description", new Vector(1, 1, 1), false);
        Property<Player> samePlayer = new Property<>(PropertyType.PLAYER, "a_player", "some player description", playerMock, false);

        Assertions.assertEquals(aNumber, sameNumber);
        Assertions.assertEquals(aString, sameString);
        Assertions.assertEquals(aVector, sameVector);
        Assertions.assertEquals(aPlayer, samePlayer);

        Property<Integer> differentNumber = new Property<>(PropertyType.INTEGER, "different_number", "some number description", 1, true);
        Property<String> differentString = new Property<>(PropertyType.STRING, "different_string", "some string description", "text", false);
        Property<Vector> differentVector = new Property<>(PropertyType.VECTOR, "different_vector", "some vector description", new Vector(1, 1, 1), false);
        Property<Player> differentPlayer = new Property<>(PropertyType.PLAYER, "different_player", "some player description", playerMock, false);

        Assertions.assertNotEquals(aNumber, differentNumber);
        Assertions.assertNotEquals(aString, differentString);
        Assertions.assertNotEquals(aVector, differentVector);
        Assertions.assertNotEquals(aPlayer, differentPlayer);
    }

//    @Test
    void verify() {
        PlayerMock somePlayer = server.addPlayer();

        Assertions.assertDoesNotThrow(() -> aNumber.verify(18));
        Assertions.assertDoesNotThrow(() -> aString.verify("hello"));
        Assertions.assertDoesNotThrow(() -> aVector.verify(new Vector(1, 2, 3)));
        Assertions.assertDoesNotThrow(() -> aPlayer.verify(somePlayer));

        somePlayer.disconnect();

        Assertions.assertThrows(AzaleaException.class, () -> aNumber.verify(999));
        Assertions.assertThrows(AzaleaException.class, () -> aString.verify(""));
        Assertions.assertThrows(AzaleaException.class, () -> aVector.verify(new Vector(0, 0, 0)));
        Assertions.assertThrows(AzaleaException.class, () -> aPlayer.verify(somePlayer));
    }

//    @Test
    void getAssignmentPolicies() {
        Assertions.assertEquals(aNumber.getAssignmentPolicies(), List.of(NUMBER_BETWEEN_0_21));
        Assertions.assertEquals(aString.getAssignmentPolicies(), List.of(NON_EMPTY_STRING));
        Assertions.assertEquals(aVector.getAssignmentPolicies(), List.of(NON_ZERO_VECTOR));
        Assertions.assertEquals(aPlayer.getAssignmentPolicies(), List.of(ONLINE_PLAYER));
    }

//    @Test
    void fromCommand() {
        Command command = new CommandNode("/example");

        Assertions.assertDoesNotThrow(() -> aNumber.fromCommand(playerMock, new Arguments(command, List.of("18"))));
        Assertions.assertDoesNotThrow(() -> aString.fromCommand(playerMock, new Arguments(command, List.of("text_text"))));
        Assertions.assertDoesNotThrow(() -> aVector.fromCommand(playerMock, new Arguments(command, List.of("1 2 3"))));
        Assertions.assertDoesNotThrow(() -> aPlayer.fromCommand(playerMock, new Arguments(command, List.of(playerMock.getName()))));

        Assertions.assertEquals(18, aNumber.get());
        Assertions.assertEquals("text_text", aString.get());
        Assertions.assertEquals(new Vector(1, 2, 3), aVector.get());
        Assertions.assertEquals(playerMock, aPlayer.get());

        playerMock.disconnect();

        Assertions.assertThrows(AzaleaException.class, () -> aNumber.fromCommand(playerMock, new Arguments(command, List.of("100"))));
        Assertions.assertThrows(AzaleaException.class, () -> aString.fromCommand(playerMock, new Arguments(command, List.of(""))));
        Assertions.assertThrows(AzaleaException.class, () -> aVector.fromCommand(playerMock, new Arguments(command, List.of("0 0 0"))));
        Assertions.assertThrows(AzaleaException.class, () -> aPlayer.fromCommand(playerMock, new Arguments(command, List.of(playerMock.getName()))));
    }

//    @Test
    void suggest() {
        Command command = new CommandNode("/example");

        Assertions.assertEquals(List.of("18"), aNumber.suggest(playerMock, new Arguments(command, List.of())));
        Assertions.assertEquals(List.of("text_text"), aString.suggest(playerMock, new Arguments(command, List.of())));
        Assertions.assertEquals(PropertyType.VECTOR.suggest(playerMock, new Arguments(command, List.of()), null), aVector.suggest(playerMock, new Arguments(command, List.of())));
        Assertions.assertEquals(List.of(playerMock.getName()), aPlayer.suggest(playerMock, new Arguments(command, List.of())));
    }

//    @Test
    void serialize() {
        YamlConfiguration configuration = new YamlConfiguration();

        aNumber.serialize(configuration);
        aString.serialize(configuration);
        aVector.serialize(configuration);
        aPlayer.serialize(configuration);

        Assertions.assertNotNull(configuration.get(aNumber.getName()));
        Assertions.assertNotNull(configuration.get(aString.getName()));
        Assertions.assertNotNull(configuration.get(aVector.getName()));
        Assertions.assertNotNull(configuration.get(aPlayer.getName()));
    }

//    @Test
    void deserialize() {
        YamlConfiguration configuration = new YamlConfiguration();

        aNumber.serialize(configuration);
        aString.serialize(configuration);
        aVector.serialize(configuration);
        aPlayer.serialize(configuration);

        aNumber.set(null);
        aString.set(null);
        aVector.set(null);
        aPlayer.set(null);

        Assertions.assertFalse(aNumber.isSet());
        Assertions.assertFalse(aString.isSet());
        Assertions.assertFalse(aVector.isSet());
        Assertions.assertFalse(aPlayer.isSet());

        aNumber.deserialize(configuration);
        aString.deserialize(configuration);
        aVector.deserialize(configuration);
        aPlayer.deserialize(configuration);

        Assertions.assertEquals(1, aNumber.get());
        Assertions.assertEquals("text", aString.get());
        Assertions.assertEquals(new Vector(1, 1, 1), aVector.get());
        Assertions.assertEquals(playerMock, aPlayer.get());
    }
}