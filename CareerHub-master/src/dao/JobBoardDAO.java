package dao;

import entity.JobListing;
import entity.Company;
import entity.Applicant;
import entity.JobApplication;
import exception.DatabaseConnectionException;

import java.util.List;

public interface JobBoardDAO {
    void insertJobListing(JobListing job) throws DatabaseConnectionException;
    void insertCompany(Company company) throws DatabaseConnectionException;
    void insertApplicant(Applicant applicant) throws DatabaseConnectionException;
    void insertJobApplication(JobApplication application) throws DatabaseConnectionException;
    List<JobListing> getJobListings() throws DatabaseConnectionException;
    List<Company> getCompanies() throws DatabaseConnectionException;
    List<Applicant> getApplicants() throws DatabaseConnectionException;
    List<JobApplication> getApplicationsForJob(int jobID) throws DatabaseConnectionException;

    List<JobListing> getJobsBySalaryRange(double minSalary, double maxSalary);
}
