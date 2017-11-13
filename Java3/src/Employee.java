
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Employee {
    private String lastName;
    private String job;
    private int salary;

    public Employee(String lastName, String job, int salary) {
        this.lastName = lastName;
        this.job = job;
        this.salary = salary;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJob() {
        return job;
    }

    public int getSalary() {
        return salary;
    }

    public static int maxSalary(List<Employee> list) {
        return list.parallelStream()
                .map(Employee::getSalary)
                .max(Integer::compare)
                .orElse(0);
    }

    public static int minSalary(List<Employee> list) {
        return list.parallelStream()
                .map(Employee::getSalary)
                .min(Integer::compare)
                .orElse(0);
    }

    public static double averageSalary(List<Employee> list) {
        return list.parallelStream()
                .mapToInt(Employee::getSalary)
                .average().orElse(0);
    }

    public static Map<String, Integer> jobCount(List<Employee> list) {
        return list.parallelStream()
                .collect(Collectors.groupingBy(Employee::getJob, Collectors.summingInt(a -> 1)));
    }

    public static Map<Character, Integer> abc(List<Employee> list) {
        return list.parallelStream()
                .collect(Collectors.groupingBy(e -> e.getLastName().charAt(0), Collectors.summingInt(e -> 1)));
    }

}
