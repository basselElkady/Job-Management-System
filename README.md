# Job-Management-System
High-Level System Design
Architecture Overview
The Job Management System follows key components:
1.	Job API Service: Handles RESTful API requests for job creation, status retrieval, retrying failed jobs and Scheduler jobs
2.	Job Type : I did not expose creation for job type as you need to create your own task, just make sure to implement JobExecutor interface and then the app assign job type for the database by Job Factory
3.	Job Executor Service: Processes jobs based on scheduling.
4.	Message Queue (RabbitMQ): Enables asynchronous job processing.
5.	Scheduler Module: Handles job execution timing (immediate or scheduled).
Workflow
1.	A client submits a job via the Job API Service.
2.	The job is stored in the database with a QUEUED status.
3.	The Job Executor Service picks up jobs based on scheduling rules.
4.	The job is executed, and its status is updated (RUNNING → SUCCESS/FAILED).
5.	If a job fails, it can be retried using the retry mechanism.





Prerequisites
Ensure you have the following installed before proceeding:
•	Java 21
•	Maven 3.2+
•	Docker (for rabbitMq)
Building the System Locally
1.	Clone the Repository
2.	git clone https://github.com/your-repo/job-management-system.git
cd job-management-system  --- my own ????????????????
3.	Configure the RabbitMq If using Docker, start a RabbitMq container:
docker run -d -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management

4.	# this is configuration for RabbitMq in Properties File
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
spring.rabbitmq.connection-timeout=PT10S
5.	Build the Project
mvn clean install
Running the System Locally
1.	Start the Application
mvn spring-boot:run
2.	Verify the API is Running Open your browser or PostMan:
http://localhost:8080/actuator/health
Running the Test Suite
# Running Tests

This project uses different types of tests organized in three main packages:
- UnitTest
- IntegrationTest
- E2ETest  // you should run the app to execute these tests correctly

You can run all the tests in these packages using Maven or Gradle.

## Prerequisites
- **Maven** or **Gradle** must be installed on your machine.
- Java 17 or higher is recommended.

---

## Running Tests with Maven

To run all tests (unit, integration, and E2E), use the following command:  mvn clean test



API Endpoints

Method	Endpoint	Description

POST	/v1/jobs	Create a new Job

POST	/v1/jobs/batch	Create a batch of Jobs

DELETE	/v1/jobs?name=desired name	Delete a Job (if not running)

GET	/v1/jobs?status=desired status	Get Job status

GET	v1/jobtype	Get a list of names for job types



API description
Post: http://localhost:8080/v1/jobs
Request body : {
    "name":"Enter your job name",
    "jobType": "Email", should be name of mentioned in job type database else it will fire an exception
    "schedule" : "2025-01-24T20:47:00"
}
response HTTP code 200.OK 
and it return Boolean True


Post: http://localhost:8080/v1/jobs/batch
Request body : 
[
    {
        "name": "104",
        "jobType": "DataLoad",
        "schedule": "2025-01-25T12:10:00"
    },
    {
        "name": "102",
        "jobType": "Email"
    }
]
response HTTP code 200.OK 
and it return Boolean 


Delete: http://localhost:8080/v1/jobs?id=desired job id
Response HTTP code 200.OK 
and it return Boolean 






GET: http://localhost:8080/v1/jobs/{id}/status
Response HTTP code 200.OK 
and it return String with Job Status 






GET : http://localhost:8080/v1/jobtype

response HTTP code 200.OK 
and it return Object contain List<JobTypes name>

{
    "jobTypeName": [
        "DataLoad",
        "Email"
    ]
}






Configuration Options
•	Logging levels can be adjusted in application.properties 
•	H2 database configuration
•	RabbitMq settings can be modified in application.properties 


What’s the technical debt generated (likely caused by the shortcuts in the previous point) by your solution and how would you address it in the future?

•	When we want to add new job type we don’t re-developing the Job Management System, but we need to create new class with the job type execution and make it implement the JobExecutor interface and then rebuild the application, yet this is not the best way if we need to manage larger jobs
we can make it a microservices-based architecture where new microservices (job executors) automatically register themselves with the Job Management System without requiring code restarts. Here’s how we can design it:
Architecture Overview
1.	Job Management Service (Central System)
o	Exposes APIs to register, schedule, and execute jobs.
o	Maintains a registry of available job executors.
o	Delegates job execution to the appropriate microservice.
2.	Job Executor Microservices (Independent Workers)
o	Each microservice is responsible for executing a specific type of job (e.g., Data Load, Email).
o	When a new job executor microservice starts, it automatically registers itself with the Job Management Service.
3.	Service Discovery & Dynamic Registration
o	Using Eureka: Executors register dynamically.


•	scheduling across different time zones as now ( scheduling only working on our time zone )
1.	Always save timestamps in Coordinated Universal Time
2.	When fetching job details for a user in a specific region, convert the UTC timestamp to their local time zone.
Now, the job execution time will be displayed correctly for each user based on their region




•	If user want to execute a lot of jobs together and one of fire an exception all of the batch will not execute as it may be a jobs related to each other.

•	I assumed that we can retry the same name of jobs multiple times so I did not make it unique

•	No Job Dependencies Support
Issue
If Job B depends on Job A, there is no mechanism to ensure Job A completes before Job B starts.
Future Fix
Make a jobs field as a parent jobs in each job
Ensure dependent jobs wait for their predecessors to complete.

•	No Webhook Notifications for Job Events
Issue
•	Users don’t get real-time updates when a job succeeds or fails.
Future Fix
•	Send Webhook notifications when job events occur.

•	No Role-Based Access Control (RBAC)
Issue
Currently, any user can delete, retry, or modify jobs.
No access control to restrict job management actions.
Future Fix
Implement Spring Security with role-based permissions.


•	No Rate Limiting for API Requests
Issue
No limits on how many jobs a user can submit per second.
This makes the system vulnerable to DDoS attacks.
Future Fix
Use Spring Bucket4j to limit job creation requests per user.


•	No Job Expiry or Auto-Cleanup
Issue
•	The database keeps storing old jobs forever.
•	Over time, this slows down queries and wastes storage.
Future Fix
•	Implement job expiration and auto-delete old jobs. (@Scheduled(corn job))







Deployment
For production deployment, consider using Docker Compose or Kubernetes or AWS to manage service instances.
Make your image using Dockers 
and the use K8S which enables auto-scaling, self-healing, and service discovery.
And then use AWS to get rid of the headache of all the infrastructure 
In addition to CICD 
Automates deployment for seamless updates.
 Ensures quality through automated testing.

These steps should allow you to quickly set up and run the Job Management System on your local machine.

