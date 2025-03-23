import java.util.*;

public class SchedulerController {

    public static void main(String[] args) {
        List<Process> processes = ProcessScheduler.readProcessData("processes.txt");

        //Ask for the algorithm to use
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a Scheduling Algorithm:");
        System.out.println("1. FCFS");
        System.out.println("2. Round Robin (RR)");

        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.println("Running FCFS Scheduling");
            ProcessScheduler.fcfs(processes);
        } else if (choice == 2) {
            System.out.print("Enter Time Quantum for Round Robin: ");
            int timeQuantum = scanner.nextInt();
            System.out.println("Running Round Robin Scheduling");
            ProcessScheduler.roundRobin(processes, timeQuantum);
        } else {
            System.out.println("Invalid choice, Please select 1 or 2.");
        }

        scanner.close();
    }
}
