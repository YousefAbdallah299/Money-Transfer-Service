# Money Transfer System

Welcome to the **Money Transfer System**, a comprehensive banking platform designed to handle account management, financial transactions, and more.

## Overview
- **User Profiles**: Manage user accounts and link them to individual bank accounts.
- **Transfer Capabilities**: Execute both internal and external financial transfers.
- **Account Oversight**: Monitor account balances, review transaction histories, and modify account information.
- **Currency Exchange**: Convert between various supported currencies.
- **Favorite Recipients**: Store recipient details for expedited future transfers.
- **Transaction Logs**: Access detailed transaction history with sorting and pagination features.
- **Authentication**: Ensure secure user access with token-based authentication and session management.
- **Error Management**: Implement thorough validation and error handling throughout the system.

## Technology Stack
- **Backend Languages**: Java and Kotlin are utilized for core backend development.
- **Framework**: Spring Boot 3 powers the backend services.
- **Security**: Spring Security 6 provides robust user authentication and authorization.
- **Authentication**: JWT (JSON Web Tokens) is used for secure, token-based authentication.
- **API Documentation**: Swagger facilitates comprehensive API documentation and testing.
- **Performance Enhancement**: Caching mechanisms are incorporated to boost performance.
- **Database**: PostgreSQL serves as the relational database system.
- **Deployment**: Render is used as the deployment platform.

## API Documentation
Swagger is integrated for complete API documentation and testing capabilities.

## Installation & Setup
1. **Clone the Repository**:
    ```bash
    git clone https://github.com/your-username/money-transfer-service.git
    ```
2. **Navigate to the Project**:
    ```bash
    cd money-transfer-service
    ```
3. **Install Dependencies**:
    Ensure Java, Kotlin, and Maven are installed, then run:
    ```bash
    mvn install
    ```
4. **Configure Properties**:
    Edit the `application.properties` file located in `src/main/resources/` to configure database connections and other settings.

5. **Run the Application**:
    ```bash
    mvn spring-boot:run
    ```

## Running the Application
Once deployed, you can access the application via [Render](https://money-transfer-service.onrender.com). Utilize the provided API endpoints for managing accounts, processing transactions, and more.

## Contributing
We welcome contributions to enhance the project! To contribute:
1. Fork the repository.
2. Create a new branch:
    ```bash
    git checkout -b feature/your-feature
    ```
3. Commit your changes:
    ```bash
    git commit -am 'Add new feature'
    ```
4. Push to your branch:
    ```bash
    git push origin feature/your-feature
    ```
5. Open a Pull Request to merge your changes.

## Contact
- **Yousef Abdallah Ahmed**: [LinkedIn](https://www.linkedin.com/in/yousef-abdallah-9a6976232/) | [Email](mailto:yousefabdallah031@gmail.com)

**Repository Link**: [Money Transfer Application](https://github.com/YousefAbdallah299/Money-Transfer-Service)
