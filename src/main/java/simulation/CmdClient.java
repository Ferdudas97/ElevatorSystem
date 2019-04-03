package simulation;

import elevator.Elevator;
import elevator.ElevatorSystem;
import elevator.ElevatorSystemImpl;
import lombok.val;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

public class CmdClient {

    private static final Scanner in = new Scanner(System.in);
    private boolean exit = false;
    private ElevatorSystem system;

    public void start() {
        out.println("Type number of elevators (min=1 max=16)");
        val numberOfElevators = inputInteger(16, 1);
        system = ElevatorSystemImpl.of(initElevators(numberOfElevators));

        while (!exit) {
            printMenu();
            val numberOfAction = inputInteger(5, 1);
            doSystemAction(numberOfAction);
        }
    }

    private void printMenu() {
        out.println("Possible actions (type number):");
        out.println("1. pickup elevator");
        out.println("2. update elevator");
        out.println("3. show simulation status");
        out.println("4. next simulation step ");
        out.println("5. exit");
    }


    private void doSystemAction(final int actionNumber) {
        switch (actionNumber) {
            case 1:
                out.println("Type target floor");
                var targetFloor = in.nextInt();
                out.println("Type direction ");
                out.println("-1 ->  down");
                out.println(" 1 -> up");
                var direction = in.nextInt();
                system.pickUp(targetFloor, direction);
                break;
            case 2:
                out.println("Type elevator id");
                val id = in.nextInt();
                out.println("Type current floor");
                val currentFloor = in.nextInt();
                out.println("Type target floor");
                targetFloor = in.nextInt();
                system.update(id,currentFloor, targetFloor);
                break;
            case 3:
                system.status().forEach(out::println);
                break;
            case 4:
                system.step();
                break;
            case 5:
                exit=true;
                break;

        }

    }

    private int inputInteger(final int upperBound, final int lowerBound) {
        var nextInt = in.nextInt();
        while (nextInt > upperBound || nextInt < lowerBound) nextInt = in.nextInt();
        return nextInt;
    }


    private List<Elevator> initElevators(final int number) {
        return Stream.iterate(1, i -> i + 1)
                .limit(number)
                .map(i -> Elevator.of(i, 0))
                .collect(Collectors.toList());
    }

}
