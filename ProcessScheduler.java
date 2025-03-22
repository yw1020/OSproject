import java.io.*;
import java.util.*;

class Process {
    int pid, arrivalTime, burstTime, remainingBurstTime, priority, startTime, completionTime, waitingTime, turnaroundTime;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime; // Initialize remaining burst time
        this.priority = priority;
    }
}

public class ProcessScheduler {

    // Read process data from file
    public static List<Process> readProcessData(String filename) {
        List<Process> processes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.trim().split("\\s+");
                int pid = Integer.parseInt(data[0]);
                int arrivalTime = Integer.parseInt(data[1]);
                int burstTime = Integer.parseInt(data[2]);
                int priority = Integer.parseInt(data[3]);

                processes.add(new Process(pid, arrivalTime, burstTime, priority));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return processes;
    }

    // FCFS Scheduling Algorithm
    public static void fcfs(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime)); // Sort by arrival time
        int currentTime = 0;

        System.out.println("Gantt Chart (FCFS):");
        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            p.startTime = currentTime;
            p.completionTime = p.startTime + p.burstTime;
            p.waitingTime = p.startTime - p.arrivalTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;

            currentTime = p.completionTime;
            System.out.print("| P" + p.pid + " ");
        }
        System.out.println("|");

        // Print process details
        printProcessDetails(processes);
    }

    // Round Robin Scheduling Algorithm
    public static void roundRobin(List<Process> processes, int timeQuantum) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime)); // Sort by arrival time
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        int completed = 0;

        System.out.println("Gantt Chart (Round Robin):");
        while (completed < processes.size()) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && !queue.contains(p) && p.remainingBurstTime > 0) {
                    queue.add(p);
                }
            }

            if (!queue.isEmpty()) {
                Process currentProcess = queue.poll();
                int executionTime = Math.min(timeQuantum, currentProcess.remainingBurstTime);
                currentProcess.remainingBurstTime -= executionTime;
                currentTime += executionTime;

                if (currentProcess.remainingBurstTime == 0) {
                    currentProcess.completionTime = currentTime;
                    currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                    completed++;
                }

                System.out.print("| P" + currentProcess.pid + " ");
                if (currentProcess.remainingBurstTime > 0) {
                    queue.add(currentProcess); // Add back to queue if not finished
                }
            } else {
                currentTime++; // Increment time if no process is available to execute
            }
        }
        System.out.println("|");

        // Print process details
        printProcessDetails(processes);
    }

    // Utility method to print process details
    private static void printProcessDetails(List<Process> processes) {
        System.out.println("\nPID\tArrival\tBurst\tStart\tCompletion\tWT\tTAT");
        int totalWaitingTime = 0, totalTurnaroundTime = 0;

        for (Process p : processes) {
            System.out.println(p.pid + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" +
                    p.startTime + "\t" + p.completionTime + "\t\t" +
                    p.waitingTime + "\t" + p.turnaroundTime);
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
        }

        // Calculate and print Average WT and TAT
        double avgWT = (double) totalWaitingTime / processes.size();
        double avgTAT = (double) totalTurnaroundTime / processes.size();

        System.out.println("\nAverage Waiting Time: " + avgWT);
        System.out.println("Average Turnaround Time: " + avgTAT);
    }
}
