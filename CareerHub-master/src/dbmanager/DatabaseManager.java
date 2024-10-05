package dbmanager;

import entity.JobListing;
import entity.Company;
import entity.Applicant;
import entity.JobApplication;
import dao.JobBoardDAO;
import dao.JobBoardDAOImpl;
import exception.DatabaseConnectionException;
import util.DBConnUtil;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class DatabaseManager {
    private JobBoardDAO jobBoardDAO;

    public DatabaseManager() {
        jobBoardDAO = new JobBoardDAOImpl();
    }

    public void initializeDatabase() throws DatabaseConnectionException {
        // SQL commands to create tables if they do not exist
        String createJobsTable = "CREATE TABLE IF NOT EXISTS Jobs (" +
                "JobID INT AUTO_INCREMENT PRIMARY KEY, " +
                "CompanyID INT, " +
                "JobTitle VARCHAR(255), " +
                "JobDescription TEXT, " +
                "JobLocation VARCHAR(255), " +
                "Salary DECIMAL(10, 2), " +
                "JobType VARCHAR(50), " +
                "PostedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        String createCompaniesTable = "CREATE TABLE IF NOT EXISTS Companies (" +
                "CompanyID INT AUTO_INCREMENT PRIMARY KEY, " +
                "CompanyName VARCHAR(255), " +
                "Location VARCHAR(255)" +
                ");";

        String createApplicantsTable = "CREATE TABLE IF NOT EXISTS Applicants (" +
                "ApplicantID INT AUTO_INCREMENT PRIMARY KEY, " +
                "FirstName VARCHAR(100), " +
                "LastName VARCHAR(100), " +
                "Email VARCHAR(255), " +
                "Phone VARCHAR(15), " +
                "Resume TEXT" +
                ");";

        String createApplicationsTable = "CREATE TABLE IF NOT EXISTS Applications (" +
                "ApplicationID INT AUTO_INCREMENT PRIMARY KEY, " +
                "JobID INT, " +
                "ApplicantID INT, " +
                "ApplicationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "CoverLetter TEXT" +
                ");";

        try (Connection connection = DBConnUtil.getConnection("db.properties")) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.execute(createJobsTable);
                statement.execute(createCompaniesTable);
                statement.execute(createApplicantsTable);
                statement.execute(createApplicationsTable);
                System.out.println("Database initialized successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Failed to initialize the database.");
        }
    }

    public void insertJobListing(JobListing job) throws DatabaseConnectionException {
        jobBoardDAO.insertJobListing(job);
    }

    public void insertCompany(Company company) throws DatabaseConnectionException {
        jobBoardDAO.insertCompany(company);
    }

    public void insertApplicant(Applicant applicant) throws DatabaseConnectionException {
        jobBoardDAO.insertApplicant(applicant);
    }

    public void insertJobApplication(JobApplication application) throws DatabaseConnectionException {
        jobBoardDAO.insertJobApplication(application);
    }

    public List<JobListing> getJobListings() throws DatabaseConnectionException {
        return jobBoardDAO.getJobListings();
    }

    public List<Company> getCompanies() throws DatabaseConnectionException {
        return jobBoardDAO.getCompanies();
    }

    public List<Applicant> getApplicants() throws DatabaseConnectionException {
        return jobBoardDAO.getApplicants();
    }

    public List<JobApplication> getApplicationsForJob(int jobID) throws DatabaseConnectionException {
        return jobBoardDAO.getApplicationsForJob(jobID);
    }

    // New method to get jobs by salary range
    public List<JobListing> getJobsBySalaryRange(double minSalary, double maxSalary) throws DatabaseConnectionException {
        return jobBoardDAO.getJobsBySalaryRange(minSalary, maxSalary);
    }
}
