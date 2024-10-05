package dao;

import entity.JobListing;
import entity.Company;
import entity.Applicant;
import entity.JobApplication;
import exception.DatabaseConnectionException;
import util.DBConnUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JobBoardDAOImpl implements JobBoardDAO {
    private Connection getConnection() throws SQLException {
        return DBConnUtil.getConnection("db.properties");
    }

    @Override
    public void insertJobListing(JobListing job) throws DatabaseConnectionException {
        String sql = "INSERT INTO Jobs (CompanyID, JobTitle, JobDescription, JobLocation, Salary, JobType) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, job.getCompanyID());
            preparedStatement.setString(2, job.getJobTitle());
            preparedStatement.setString(3, job.getJobDescription());
            preparedStatement.setString(4, job.getJobLocation());
            preparedStatement.setDouble(5, job.getSalary());
            preparedStatement.setString(6, job.getJobType());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error inserting job listing: " + e.getMessage());
        }
    }

    @Override
    public void insertCompany(Company company) throws DatabaseConnectionException {
        String sql = "INSERT INTO Companies (CompanyName, Location) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, company.getCompanyName());
            preparedStatement.setString(2, company.getLocation());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error inserting company: " + e.getMessage());
        }
    }

    @Override
    public void insertApplicant(Applicant applicant) throws DatabaseConnectionException {
        String sql = "INSERT INTO Applicants (FirstName, LastName, Email, Phone, Resume) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, applicant.getFirstName());
            preparedStatement.setString(2, applicant.getLastName());
            preparedStatement.setString(3, applicant.getEmail());
            preparedStatement.setString(4, applicant.getPhone());
            preparedStatement.setString(5, applicant.getResume());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error inserting applicant: " + e.getMessage());
        }
    }

    @Override
    public void insertJobApplication(JobApplication application) throws DatabaseConnectionException {
        String sql = "INSERT INTO Applications (JobID, ApplicantID, CoverLetter) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, application.getJobID());
            preparedStatement.setInt(2, application.getApplicantID());
            preparedStatement.setString(3, application.getCoverLetter());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error inserting job application: " + e.getMessage());
        }
    }

    @Override
    public List<JobListing> getJobListings() throws DatabaseConnectionException {
        List<JobListing> jobListings = new ArrayList<>();
        String sql = "SELECT * FROM Jobs";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                JobListing job = new JobListing();
                job.setJobID(resultSet.getInt("JobID"));
                job.setCompanyID(resultSet.getInt("CompanyID"));
                job.setJobTitle(resultSet.getString("JobTitle"));
                job.setJobDescription(resultSet.getString("JobDescription"));
                job.setJobLocation(resultSet.getString("JobLocation"));
                job.setSalary(resultSet.getDouble("Salary"));
                job.setJobType(resultSet.getString("JobType"));
                job.setPostedDate(resultSet.getTimestamp("PostedDate").toLocalDateTime());
                jobListings.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error retrieving job listings: " + e.getMessage());
        }
        return jobListings;
    }

    @Override
    public List<Company> getCompanies() throws DatabaseConnectionException {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM Companies";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Company company = new Company();
                company.setCompanyID(resultSet.getInt("CompanyID"));
                company.setCompanyName(resultSet.getString("CompanyName"));
                company.setLocation(resultSet.getString("Location"));
                companies.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error retrieving companies: " + e.getMessage());
        }
        return companies;
    }

    @Override
    public List<Applicant> getApplicants() throws DatabaseConnectionException {
        List<Applicant> applicants = new ArrayList<>();
        String sql = "SELECT * FROM Applicants";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Applicant applicant = new Applicant();
                applicant.setApplicantID(resultSet.getInt("ApplicantID"));
                applicant.setFirstName(resultSet.getString("FirstName"));
                applicant.setLastName(resultSet.getString("LastName"));
                applicant.setEmail(resultSet.getString("Email"));
                applicant.setPhone(resultSet.getString("Phone"));
                applicant.setResume(resultSet.getString("Resume"));
                applicants.add(applicant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error retrieving applicants: " + e.getMessage());
        }
        return applicants;
    }

    @Override
    public List<JobApplication> getApplicationsForJob(int jobID) throws DatabaseConnectionException {
        List<JobApplication> applications = new ArrayList<>();
        String sql = "SELECT * FROM Applications WHERE JobID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, jobID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JobApplication application = new JobApplication();
                application.setApplicationID(resultSet.getInt("ApplicationID"));
                application.setJobID(resultSet.getInt("JobID"));
                application.setApplicantID(resultSet.getInt("ApplicantID"));
                application.setApplicationDate(resultSet.getTimestamp("ApplicationDate").toLocalDateTime());
                application.setCoverLetter(resultSet.getString("CoverLetter"));
                applications.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error retrieving applications for job: " + e.getMessage());
        }
        return applications;
    }

    @Override
    public List<JobListing> getJobsBySalaryRange(double minSalary, double maxSalary) {
        List<JobListing> jobListings = new ArrayList<>();
        String query = "SELECT j.*, c.CompanyName " +
                "FROM Jobs j " +
                "JOIN Companies c ON j.CompanyID = c.CompanyID " +
                "WHERE j.Salary BETWEEN ? AND ?";

        try (Connection connection = DBConnUtil.getConnection("db.properties");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, minSalary);
            preparedStatement.setDouble(2, maxSalary);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                JobListing job = new JobListing();
                job.setJobID(resultSet.getInt("JobID"));
                job.setJobTitle(resultSet.getString("JobTitle"));
                job.setSalary(resultSet.getDouble("Salary"));
                job.setCompanyID(resultSet.getInt("CompanyID"));
                job.setJobDescription(resultSet.getString("JobDescription"));
                job.setJobLocation(resultSet.getString("JobLocation"));
                job.setJobType(resultSet.getString("JobType"));

                // Use resultSet.getTimestamp() to get the Timestamp and convert to LocalDateTime
                Timestamp postedDate = resultSet.getTimestamp("PostedDate");
                if (postedDate != null) {
                    job.setPostedDate(postedDate.toLocalDateTime());
                }

                jobListings.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database query error: " + e.getMessage());
        }
        return jobListings;
    }

}
