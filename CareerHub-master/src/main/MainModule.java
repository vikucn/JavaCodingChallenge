package main;

import dbmanager.DatabaseManager;
import entity.JobListing;
import entity.Company;
import entity.Applicant;
import entity.JobApplication;
import exception.DatabaseConnectionException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static DatabaseManager dbManager = new DatabaseManager();

    public static void main(String[] args) throws DatabaseConnectionException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Menu:");
            System.out.println("1. Post Job");
            System.out.println("2. Create Applicant Profile");
            System.out.println("3. Apply for Job");
            System.out.println("4. List Jobs");
            System.out.println("5. Get Jobs by Salary Range"); // New option added
            System.out.println("6. Exit");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Post a job
                    System.out.print("Enter Company ID: ");
                    int companyId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Job Title: ");
                    String jobTitle = scanner.nextLine();
                    System.out.print("Enter Job Description: ");
                    String jobDescription = scanner.nextLine();
                    System.out.print("Enter Job Location: ");
                    String jobLocation = scanner.nextLine();
                    System.out.print("Enter Salary: ");
                    double salary = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter Job Type: ");
                    String jobType = scanner.nextLine();
                    JobListing job = new JobListing();
                    job.setCompanyID(companyId);
                    job.setJobTitle(jobTitle);
                    job.setJobDescription(jobDescription);
                    job.setJobLocation(jobLocation);
                    job.setSalary(salary);
                    job.setJobType(jobType);
                    job.setPostedDate(LocalDateTime.now());
                    dbManager.insertJobListing(job);
                    System.out.println("Job posted successfully.");
                    break;

                case 2:
                    // Create applicant profile
                    System.out.print("Enter First Name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter Last Name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter Resume: ");
                    String resume = scanner.nextLine();
                    Applicant applicant = new Applicant();
                    applicant.setFirstName(firstName);
                    applicant.setLastName(lastName);
                    applicant.setEmail(email);
                    applicant.setPhone(phone);
                    applicant.setResume(resume);
                    dbManager.insertApplicant(applicant);
                    System.out.println("Applicant profile created.");
                    break;

                case 3:
                    // Apply for a job
                    System.out.print("Enter Job ID: ");
                    int jobID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Applicant ID: ");
                    int applicantID = scanner.nextInt(); // Assuming the applicant ID is known
                    scanner.nextLine();
                    System.out.print("Enter Cover Letter: ");
                    String coverLetter = scanner.nextLine();

                    JobApplication application = new JobApplication();
                    application.setJobID(jobID);
                    application.setApplicantID(applicantID);
                    application.setApplicationDate(LocalDateTime.now());
                    application.setCoverLetter(coverLetter);

                    dbManager.insertJobApplication(application);
                    System.out.println("Application submitted successfully.");
                    break;

                case 4:
                    // List jobs
                    List<JobListing> jobs = dbManager.getJobListings();
                    for (JobListing j : jobs) {
                        System.out.println("Job ID: " + j.getJobID() + ", Title: " + j.getJobTitle() + ", Salary: " + j.getSalary());
                    }
                    break;

                case 5:
                    // Get jobs by salary range
                    System.out.print("Enter Minimum Salary: ");
                    double minSalary = scanner.nextDouble();
                    System.out.print("Enter Maximum Salary: ");
                    double maxSalary = scanner.nextDouble();
                    List<JobListing> filteredJobs = dbManager.getJobsBySalaryRange(minSalary, maxSalary);

                    if (filteredJobs.isEmpty()) {
                        System.out.println("No jobs found in this salary range.");
                    } else {
                        System.out.println("Jobs in the specified salary range:");
                        for (JobListing jobListing : filteredJobs) {
                            System.out.println("Job ID: " + jobListing.getJobID() +
                                    ", Title: " + jobListing.getJobTitle() +
                                    ", Salary: " + jobListing.getSalary() +
                                    ", Company ID: " + jobListing.getCompanyID());
                        }
                    }
                    break;

                case 6:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 6);

        scanner.close();
    }
}
