
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * args[0] must has name of file, which has list of employees
 * each employee has name, job and salary
 */
public class Main {
    public static void main(String[] args) {
        List<Employee> list;
        try{
            list = getEmployees(args[0]);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("You didn't pass the file path");
            list = getEmployees("");
        }
        System.out.println("Max salary: " + Employee.maxSalary(list));
        System.out.println("Min salary: " + Employee.minSalary(list));
        System.out.println("Average salary: " + Employee.averageSalary(list));
        Employee.jobCount(list).forEach((s, c) ->
                System.out.println("Number of " + s + ": " + c));
        Employee.abc(list).forEach((l, c) ->
                System.out.println("On letter " + l + " are " + c +
                        (c == 1 ? " employee" : " employees")));
    }

    private static List<Employee> getEmployees(String path) {
        try {
            return Files.lines(Paths.get(path))
                    .map(s -> s.split("[,\\s]+"))
                    .map(s -> new Employee(s[0], s[1], Integer.parseInt(s[2])))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("File not found - will create empty list");
            return new ArrayList<>();
        }
    }

}
